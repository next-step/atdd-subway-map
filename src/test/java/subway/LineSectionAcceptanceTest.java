package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.LineAcceptanceTest.지하철_노선_생성;
import static subway.LineAcceptanceTest.지하철_노선_조회;
import static subway.StationAcceptanceTest.지하철역_생성;
import static subway.common.ResponseUtils.ID_추출;
import static subway.common.ResponseUtils.적절한_응답_코드를_받을_수_있다;
import static subway.fixtures.LineFixtures.신분당선_파라미터_생성;
import static subway.fixtures.LineSectionFixtures.구간_등록_파라미터_생성;
import static subway.fixtures.StationFixtures.강남역;
import static subway.fixtures.StationFixtures.방배역;
import static subway.fixtures.StationFixtures.역삼역;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.common.DatabaseCleanup;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineSectionAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private String 강남역_ID;
    private String 방배역_ID;
    private String 역삼역_ID;

    private Long 신분당선_ID;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        강남역_ID = Long.toString(ID_추출(지하철역_생성(강남역)));
        방배역_ID = Long.toString(ID_추출(지하철역_생성(방배역)));
        역삼역_ID = Long.toString(ID_추출(지하철역_생성(역삼역)));
        신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터_생성(강남역_ID, 방배역_ID)));

    }

    /**
     * When 지하철 구간을 등록하면
     * Then 지하철 노선 조회 시 등록된 구간의 역을 순서대로 찾을 수 있다
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void createLine() {
        // when
        Map<String, String> 방배역_역삼역_구간_파라미터 = 구간_등록_파라미터_생성(방배역_ID, 역삼역_ID);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_구간_등록(신분당선_ID, 방배역_역삼역_구간_파라미터);

        // then
        적절한_응답_코드를_받을_수_있다(지하철_노선_생성_응답, HttpStatus.CREATED);

        ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회(신분당선_ID);

        적절한_응답_코드를_받을_수_있다(지하철_노선_조회_응답, HttpStatus.OK);
        등록된_구간의_역을_순서대로_찾을_수_있다(지하철_노선_조회_응답, Arrays.asList(강남역_ID, 방배역_ID, 역삼역_ID));
    }

    /**
     * When 해당 노선의 하행 종점역이 아닌 상행역 파라미터로 지하철 구간을 등록하면
     * Then 등록에 실패한다.
     */
    @DisplayName("지하철 구간 등록에 실패한다-잘못된 상행역")
    @Test
    void createLineWithInvalidUpStationOfParameter() {
        // when
        Map<String, String> 강남역_역삼역_구간_파라미터 = 구간_등록_파라미터_생성(강남역_ID, 역삼역_ID);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_구간_등록(신분당선_ID, 강남역_역삼역_구간_파라미터);

        // then
        등록에_실패한다(지하철_노선_생성_응답);
    }

    /**
     * When 해당 노선에 등록된 역을 하행역 파라미터로 지하철 구간을 등록하면
     * Then 등록에 실패한다.
     */
    @DisplayName("지하철 구간 등록에 실패한다-잘못된 하행역")
    @Test
    void createLineWithInvalidDownStationOfParameter() {
        // when
        Map<String, String> 방배역_강남역_구간_파라미터 = 구간_등록_파라미터_생성(방배역_ID, 강남역_ID);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_구간_등록(신분당선_ID, 방배역_강남역_구간_파라미터);

        // then
        등록에_실패한다(지하철_노선_생성_응답);
    }

    private ExtractableResponse<Response> 지하철_구간_등록(Long lineId, Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{lineId}/sections", lineId)
            .then().log().all()
            .extract();
    }

    private void 등록된_구간의_역을_순서대로_찾을_수_있다(ExtractableResponse<Response> response,
        List<String> stationIds) {

        for (int i = 0; i < stationIds.size(); i++) {
            assertThat(response.jsonPath().getString("stations.id[" + i + "]"))
                .isEqualTo(stationIds.get(i));
        }
    }

    private void 등록에_실패한다(ExtractableResponse<Response> response) {
        적절한_응답_코드를_받을_수_있다(response, HttpStatus.BAD_REQUEST);
    }

}