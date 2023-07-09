package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.ApiTest;
import subway.station.StationApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@DisplayName("지하철노선 구간 관련 기능")
public class SectionAcceptanceTest extends ApiTest {

    public List<Long> stationIds = new ArrayList<>();

    @BeforeEach
    void addLine() {
        List.of("강남역", "역삼역", "선릉역", "잠실역", "삼성역", "강변역").forEach(StationApi::createStationByName);
        ExtractableResponse<Response> response = StationApi.retrieveStations();
        stationIds = response.body().jsonPath().getList("id", Long.class);
    }

    // POST /lines/1/sections

    /**
     * When 노선을 생성하면
     * Then 상행역과 하행역이 포함된 기본 구간이 생성되고
     * Then 노선 조회로 상행역과 하행역이 포함된 기본 구간이 있다.
     */
    @DisplayName("기본 구간을 생성 한다.")
    @Test
    void createSection() {
        // when
        Map<String, String> stringStringMap = RequestGenerator.이호선_요청_만들기(stationIds.get(0), stationIds.get(1));
        ExtractableResponse<Response> createLineResponse = LineApi.createLine(stringStringMap);
        final String location = createLineResponse.header("Location");

        // then
        assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> retrieveLineResponse = LineApi.retrieveLineByLocation(location);
        List<String> stationNames = retrieveLineResponse.jsonPath().getList("stations.name", String.class);
        assertThat(stationNames.size()).isEqualTo(2);

    }

    /**
     * When 기본 노선의 구간이 있을 때
     * Then 노선의 하행역을 새로운 구간의 상행역으로 지정한 구간을 추가 한다.
     */
    @DisplayName("노선의 구간에 새로운 구간을 추가 한다.")
    @Test
    void appendStationToSection() {
        // when
        Map<String, String> stringStringMap = RequestGenerator.이호선_요청_만들기(stationIds.get(0), stationIds.get(1));
        ExtractableResponse<Response> createLineResponse = LineApi.createLine(stringStringMap);
        final String location = createLineResponse.header("Location");

        // then
        Map<String, String> sectionRequest = RequestGenerator.generateSectionCreateRequest(stationIds.get(1), stationIds.get(2), 10L);
        final String appendLocation = location + "/sections";
        ExtractableResponse<Response> response = LineApi.appendSectionInLine(appendLocation, sectionRequest);
        LineApi.retrieveLineByLocation(location);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 3개의 역을 가진 구간의 노선을 생성하고
     * When 새로운 구간의 상행역을 기존 구간의 두번째 역으로 지정하면
     * Then 구간이 등록되지 않는다.
     */
    @DisplayName("새 구간의 상행역은 노선 내 구간의 중간 역이 될 수 없다.")
    @Test
    void appendStationToMiddleOfSection() {
        // given
        final String location = compositeCreateLineWithThreeStation();
        final String appendLocation = location + "/sections";

        // when
        Map<String, String> otherSectionRequest = RequestGenerator.generateSectionCreateRequest(stationIds.get(1), stationIds.get(3), 10L);
        ExtractableResponse<Response> response = LineApi.appendSectionInLine(appendLocation, otherSectionRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 3개의 역을 가진 구간의 노선을 생성하고
     * When 새로운 구간의 하행역을 기존 구간의 역으로 지정하면
     * Then 구간이 등록되지 않는다
     */
    @DisplayName("새 구간의 하행역은 노선 내 구간의 역이 될 수 없다.")
    @Test
    void appendSectionWithDownStation() {
        // given
        final String location = compositeCreateLineWithThreeStation();
        final String appendLocation = location + "/sections";

        // when
        Map<String, String> otherSectionRequest = RequestGenerator.generateSectionCreateRequest(stationIds.get(1), stationIds.get(3), 10L);
        ExtractableResponse<Response> response = LineApi.appendSectionInLine(appendLocation, otherSectionRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    // DELETE /lines/1/sections?stationId=2

    /**
     * Given 3개의 역이 등록된 구간을 가진 노선이 있고
     * When 노선의 하행역을 제거하면
     * Then 구간이 삭제되고
     * Then 2개의 역을 가진 노선이 된다
     */
    @DisplayName("노선의 구간을 삭제한다.")
    @Test
    void deleteStationInSection() {
        // given
        final String location = compositeCreateLineWithThreeStation();
        final String appendLocation = location + "/sections";

        // when
        final Long stationId = stationIds.get(2);
        ExtractableResponse<Response> response = LineApi.removeSectionInLine(appendLocation, stationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> retrieveLineResponse = LineApi.retrieveLineByLocation(location);
        List<String> stations = retrieveLineResponse.jsonPath().getList("stations.name", String.class);
        assertThat(stations.size()).isEqualTo(2);
    }

    /**
     * Given 3개의 역이 등록된 구간을 가진 노선이 있고
     * When 노선의 두번째 역을 제거하면
     * Then 역이 제거되지 않는다.
     */
    @DisplayName("구간의 중간 역을 제거할 수 없다.")
    @Test
    void deleteStationInMiddleOfSection() {
        // given
        final String location = compositeCreateLineWithThreeStation();
        final String appendLocation = location + "/sections";

        // when
        final Long stationId = stationIds.get(1);
        ExtractableResponse<Response> response = LineApi.removeSectionInLine(appendLocation, stationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 노선이 있고
     * When 노선의 역이 2개 이하 일 때
     * When 노선의 하행역을 제거하면
     * Then 역이 제거되지 않는다.
     */
    @DisplayName("노선의 구간이 1개 뿐일때 역을 제거할 수 없다.")
    @Test
    void deleteStationFromMinimalSection() {
        // given
        String location = compositeCreateLineWithThreeStation();
        final String appendLocation = location + "/sections";

        // when
        final Long stationId = stationIds.get(2);
        LineApi.removeSectionInLine(appendLocation, stationId);

        // when
        final Long additionalDeleteLocation = stationIds.get(1);
        ExtractableResponse<Response> response = LineApi.removeSectionInLine(appendLocation, additionalDeleteLocation);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private String compositeCreateLineWithThreeStation() {
        Map<String, String> stringStringMap = RequestGenerator.이호선_요청_만들기(stationIds.get(0), stationIds.get(1));
        ExtractableResponse<Response> createLineResponse = LineApi.createLine(stringStringMap);
        final String location = createLineResponse.header("Location");
        final String appendLocation = location + "/sections";
        Map<String, String> sectionRequest = RequestGenerator.generateSectionCreateRequest(stationIds.get(1), stationIds.get(2), 10L);
        LineApi.appendSectionInLine(appendLocation, sectionRequest);
        LineApi.retrieveLines();
        return location;
    }

}

