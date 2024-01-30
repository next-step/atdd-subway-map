package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.TableTruncate;
import subway.line.LineApiRequester;
import subway.line.dto.LineCreateRequest;
import subway.station.StationApiRequester;
import subway.station.StationResponse;
import subway.util.JsonPathUtil;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철노선 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    @Autowired
    private TableTruncate tableTruncate;

    private final StationApiRequester stationApiRequester = new StationApiRequester();

    private final LineApiRequester lineApiRequester = new LineApiRequester();

    private final SectionApiRequester sectionApiRequester = new SectionApiRequester();

    @BeforeEach
    void setUp() {
        tableTruncate.truncate();
        stationApiRequester.createStationApiCall("잠실역");
        stationApiRequester.createStationApiCall("용산역");
        stationApiRequester.createStationApiCall("건대입구역");
        stationApiRequester.createStationApiCall("성수역");

        LineCreateRequest request = new LineCreateRequest("2호선", "green", 1L, 2L, 10);
        lineApiRequester.createLineApiCall(request);
    }

    /**
     * When 노선에 구간을 등록하면
     * Then 노선을 조회 했을때 등록한 구간이 조회된다
     */
    @DisplayName("지하철 노선 구간 등록")
    @Test
    void generateSection() {
        //when
        SectionCreateRequest request = new SectionCreateRequest(2L, 3L, 5);

        ExtractableResponse<Response> response = sectionApiRequester.generateSection(request, 1L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> findLine = lineApiRequester.findLineApiCall(1L);
        assertThat(getStationIds(findLine)).containsExactly(1L, 2L, 3L);
    }

    /**
     * When 등록할 구간의 상행역이 노선에 등록되어있는 하행종점역이 아닌 구간을 등록하면
     * Then 예외가 발생한다
     */
    @DisplayName("등록할 구간의 상행역이 노선에 등록되어있는 하행종점역이 아닌 경우 구간을 등록할 수 없다")
    @Test
    void generateSectionException() {
        //when
        SectionCreateRequest request = new SectionCreateRequest(3L, 4L, 5);

        ExtractableResponse<Response> response = sectionApiRequester.generateSection(request, 1L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString()).isEqualTo("등록할 구간의 상행역이 노선에 등록되어있는 하행종점역이 아닌 경우 구간 등록이 불가능합니다.");
    }

    /**
     * When 등록할 구간의 하행역이 이미 해당 노선에 등록되어있으면
     * Then 예외가 발생한다
     */
    @DisplayName("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.")
    @Test
    void generateAlreadySection() {
        //when
        SectionCreateRequest request = new SectionCreateRequest(2L, 1L, 5);

        ExtractableResponse<Response> response = sectionApiRequester.generateSection(request, 1L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString()).isEqualTo("이미 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.");
    }

    /**
     * Given 지하철 구간을 등록하고
     * When 구간이 2개인 노선의 구간중 1개를 삭제하면
     * Then 삭제한 1개의 구간이 삭제된다
     */
    @DisplayName("지하철 노선 구간 삭제")
    @Test
    void deleteSection() {
        //given
        SectionCreateRequest request = new SectionCreateRequest(2L, 3L, 5);

        sectionApiRequester.generateSection(request, 1L);

        //when
        ExtractableResponse<Response> response = sectionApiRequester.deleteSection(1L, 3L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> findLine = lineApiRequester.findLineApiCall(1L);
        assertThat(getStationIds(findLine)).containsExactly(1L, 2L);
    }

    /**
     * Given 지하철 구간을 등록하고
     * When 노선의 하행종점역이 아닌 구간을 삭제하면
     * Then 예외가 발생한다
     */
    @DisplayName("노선의 하행종점역이 아닌 구간은 삭제할 수 없다")
    @Test
    void deleteNotDownSection() {
        //given
        SectionCreateRequest request = new SectionCreateRequest(2L, 3L, 5);

        sectionApiRequester.generateSection(request, 1L);

        //when
        ExtractableResponse<Response> response = sectionApiRequester.deleteSection(1L, 2L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString()).isEqualTo("노선의 하행종점역만 제거할 수 있습니다.");
    }

    /**
     * When 구간이 1개인 노선의 구간을 삭제하면
     * Then 예외가 발생한다
     */
    @DisplayName("구간이 1개인 노선의 구간은 삭제할 수 없다")
    @Test
    void deleteSectionException() {
        //when
        ExtractableResponse<Response> response = sectionApiRequester.deleteSection(1L, 1L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString()).isEqualTo("구간이 1개인 노선의 구간은 삭제할 수 없습니다.");
    }

    private static List<Long> getStationIds(ExtractableResponse<Response> findLine) {
        return JsonPathUtil.getList(findLine, "stations", StationResponse.class)
                .stream().map(StationResponse::getId).collect(Collectors.toList());
    }
}
