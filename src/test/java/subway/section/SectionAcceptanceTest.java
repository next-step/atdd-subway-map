package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.controller.request.LineRequest;
import subway.controller.request.SectionRequest;
import subway.exception.message.SubwayErrorCode;
import subway.util.AcceptanceTestHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.LineFixture.삼호선_요청;
import static subway.fixture.LineFixture.신분당선_요청;
import static subway.fixture.SectionFixture.강남역_양재역_구간_요청;
import static subway.fixture.SectionFixture.교대역_양재역_구간_요청;
import static subway.fixture.SectionFixture.양재역_양재역_구간_요청;
import static subway.fixture.SectionFixture.양재역_판교역_구간_요청;
import static subway.fixture.SectionFixture.역삼역_교대역_구간_요청;
import static subway.fixture.StationFixture.강남역_ID;
import static subway.fixture.StationFixture.강남역_이름;
import static subway.fixture.StationFixture.교대역_ID;
import static subway.fixture.StationFixture.교대역_이름;
import static subway.fixture.StationFixture.양재역_ID;
import static subway.fixture.StationFixture.양재역_이름;
import static subway.fixture.StationFixture.역삼역_ID;
import static subway.fixture.StationFixture.역삼역_이름;

@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTestHelper {


    @BeforeEach
    void setup() {
        List<String> 역_목록 = List.of(강남역_이름, 역삼역_이름, 교대역_이름, 양재역_이름);
        for (String stationName : 역_목록) {
            지하철역_생성(stationName);
        }

        지하철노선_생성(신분당선_요청);
    }

    /**
     * Given -> 노선을 생성하고
     * When -> 구간을 등록하면
     * Then -> 해당 노선 조회 시 추가된 구간이 포함되어 있다.
     */
    @DisplayName("지하철 노선에 구간등록 기능 테스트")
    @Test
    void addSection() {
        // given
        var createResponse = 지하철노선_생성(신분당선_요청);
        String path = 경로_추출(createResponse);

        // when
        구간_등록(path, 역삼역_교대역_구간_요청);

        // then
        assertThat(노선_지하철역_ID_목록_조회(path)).containsExactly(강남역_ID, 역삼역_ID, 교대역_ID);
    }

    /**
     * When -> 노선을 생성하고,
     * Then -> [구간의 상행역] 이 등록한 [노선의 하행 종점역] 이 아닐경우 예외가 발생한다.
     */
    @DisplayName("구간 등록시, 구간의 상행역과 노선의 하행 종점역이 상이 할 경우 예외가 발생한다.")
    @Test
    void addSectionException() {
        // When
        var createResponse = 지하철노선_생성(신분당선_요청);
        String path = 경로_추출(createResponse);

        // Then
        assertThat(
                상태코드_추출(구간_등록(path, 교대역_양재역_구간_요청))
        ).isEqualTo(HttpStatus.BAD_REQUEST.value());

        assertThat(
                에러메시지_추출(구간_등록(path, 교대역_양재역_구간_요청))
        ).isEqualTo(SubwayErrorCode.VALIDATE_SAME_UPPER_STATION.getMessage());
    }

    /**
     * When -> 노선을 생성하고,
     * Then -> 구간의 [상행역] 과 [하행역] 이 같을경우 예외가 발생한다.
     */
    @Test
    @DisplayName("구간 등록시, 상행역과 하행역이 같을경우 예외발생.")
    void addSectionException2() {
        // When
        var createResponse = 지하철노선_생성(신분당선_요청);
        String path = 경로_추출(createResponse);

        assertThat(
                상태코드_추출(구간_등록(path, 양재역_양재역_구간_요청))
        ).isEqualTo(HttpStatus.BAD_REQUEST.value());

        assertThat(
                에러메시지_추출(구간_등록(path, 양재역_양재역_구간_요청))
        ).isEqualTo(SubwayErrorCode.VALIDATE_SECTION_SAME_STATION.getMessage());
    }

    /**
     * Given -> 지하철 노선 생성 후
     * When -> 해당 노선에 구간을 등록하고
     * Then -> 해당 노선에서 마지막 구간(하행 종점역)을 제거하면 해당 노선 조회 시 삭제된 구간을 찾을 수 없다.
     */
    @DisplayName("구간을 제거한다.")
    @Test
    void deleteSection() {
        // 노선을 생성하고
        var createResponse = 지하철노선_생성(신분당선_요청);
        String location = 경로_추출(createResponse);

        // 해당 노선에 구간을 등록하고
        구간_등록(location, 역삼역_교대역_구간_요청);

        // 해당 노선에서 마지막 구간(하행 종점역)을 제거하면
        구간_삭제(location, 교대역_ID);

        // 해당 노선 조회 시 삭제된 구간을 찾을 수 없다.
        assertThat(노선_지하철역_ID_목록_조회(location)).doesNotContain(교대역_ID);
    }

    /**
     * Given -> 지하철 노선 생성 후
     * When -> 구간을 등록하고
     * Then -> 마지막 구간이 아닌 경우 예외가 발생한다.
     */
    @Test
    @DisplayName("구간 삭제 예외 - 마지막 구간만 제거할 수 있다. (상행역) 을 삭제할경우 예외가 발생한다. ")
    void lastLineDeleteTest() {
        // Given -> 지하철 노선 생성 후
        var createResponse = 지하철노선_생성(삼호선_요청);
        String location = 경로_추출(createResponse);

        // When -> 구간을 등록하고
        구간_등록(location, 교대역_양재역_구간_요청);

        // Then -> 해당 노선에서 하행역이 아닌, 상행역 을 제거하면 예외가 발생한다.
        var response = 구간_삭제(location, 교대역_ID).extract();
        assertThat(상태코드_추출(response)).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(에러메시지_추출(response)).isEqualTo(SubwayErrorCode.ONLY_LAST_SEGMENT_CAN_BE_REMOVED.getMessage());
    }
    /**
     * Given -> 지하철 노선 생성 후
     * When -> 구간을 등록하고
     * Then -> 해당 노선에서 하행 종점역을 제거하면 해당 노선 조회 시 삭제된 구간을 찾을 수 없다.
     */
    @Test
    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.")
    void lastLineDeleteTest2() {
        // Given -> 지하철 노선 생성 후
        var createResponse = 지하철노선_생성(삼호선_요청);
        String location = 경로_추출(createResponse);

        // Then -> 해당 노선에서 하행 종점역을 제거하면 해당 노선 조회 시 삭제된 구간을 찾을 수 없다.
        var response = 구간_삭제(location, 양재역_ID).extract();
        assertThat(상태코드_추출(response)).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(에러메시지_추출(response)).isEqualTo(SubwayErrorCode.CANNOT_DELETE_LAST_STATION.getMessage());
    }

    private ExtractableResponse<Response> 구간_등록(String path, SectionRequest request) {
        return post(path + SECTION_PATH, request);
    }

    private ValidatableResponse 구간_삭제(String path, long stationId) {
        return RestAssured.given()
                .param("stationId", stationId).
                when()
                .delete(path + SECTION_PATH).
                then().log().all();
    }

    private List<Long> 노선_지하철역_ID_목록_조회(String location) {
        return get(location)
                .jsonPath()
                .getList("stations.id", Long.class);
    }

    public ExtractableResponse<Response> 지하철노선_생성(LineRequest req) {
        return post(LINE_PATH, req);
    }
}
