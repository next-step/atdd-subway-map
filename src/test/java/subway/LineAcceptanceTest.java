package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.LineSteps.지하철_구간_등록;
import static subway.LineSteps.지하철_구간_삭제;
import static subway.LineSteps.지하철_노선_목록_조회;
import static subway.LineSteps.지하철_노선_삭제;
import static subway.LineSteps.지하철_노선_생성;
import static subway.LineSteps.지하철_노선_수정;
import static subway.LineSteps.지하철_노선_조회;
import static subway.StationAcceptanceTest.지하철역_생성;
import static subway.common.ResponseUtils.ID_추출;
import static subway.common.ResponseUtils.적절한_응답_코드를_받을_수_있다;
import static subway.common.ResponseUtils.n개의_이름_목록을_조회할_수_있다;
import static subway.fixtures.LineFixtures.구간_등록_파라미터_생성;
import static subway.fixtures.LineFixtures.분당선_파라미터_생성;
import static subway.fixtures.LineFixtures.신분당선_수정_파라미터_생성;
import static subway.fixtures.LineFixtures.신분당선_파라미터_생성;
import static subway.fixtures.StationFixtures.강남역;
import static subway.fixtures.StationFixtures.방배역;
import static subway.fixtures.StationFixtures.역삼역;

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
import subway.common.DatabaseCleanup;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private String 강남역_ID;
    private String 방배역_ID;
    private String 역삼역_ID;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        강남역_ID = Long.toString(ID_추출(지하철역_생성(강남역)));
        방배역_ID = Long.toString(ID_추출(지하철역_생성(방배역)));
        역삼역_ID = Long.toString(ID_추출(지하철역_생성(역삼역)));
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성(강남역_ID, 방배역_ID);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성(신분당선_파라미터);

        지하철_노선_생성됨(지하철_노선_생성_응답, 신분당선_파라미터);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void showLines() {
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성(강남역_ID, 방배역_ID);
        Map<String, String> 분당선_파라미터 = 분당선_파라미터_생성(강남역_ID, 역삼역_ID);
        List<Map<String, String>> params = Arrays.asList(신분당선_파라미터, 분당선_파라미터);
        params.forEach(LineSteps::지하철_노선_생성);

        ExtractableResponse<Response> 지하철_노선_목록_조회_응답 = 지하철_노선_목록_조회();

        지하철_노선_목록_조회됨(지하철_노선_목록_조회_응답, params);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다")
    @Test
    void showLine() {
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성(강남역_ID, 방배역_ID);
        long 신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터));

        ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회(신분당선_ID);

        지하철_노선_조회됨(지하철_노선_조회_응답, 신분당선_파라미터);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void updateLine() {
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성(강남역_ID, 방배역_ID);
        Long 신분당선_ID = 지하철_노선_생성(신분당선_파라미터).jsonPath().getLong("id");

        Map<String, String> 신분당선_수정_파라미터 = 신분당선_수정_파라미터_생성();
        ExtractableResponse<Response> 지하철_노선_수정_응답 = 지하철_노선_수정(신분당선_ID, 신분당선_수정_파라미터);

        지하철_노선_수정됨(지하철_노선_수정_응답, 신분당선_ID, 신분당선_수정_파라미터);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void deleteLine() {
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성(강남역_ID, 방배역_ID);
        Long 신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터));

        ExtractableResponse<Response> 지하철_노선_삭제_응답 = 지하철_노선_삭제(신분당선_ID);

        지하철_노선_삭제됨(지하철_노선_삭제_응답, 신분당선_ID);
    }

    /**
     * Given 구간을 하나 가진 노선을 생성하고
     * When 지하철 구간을 등록하면
     * Then 지하철 노선 조회 시 등록된 구간의 역을 순서대로 찾을 수 있다
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void createSection() {
        Long 신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터_생성(강남역_ID, 방배역_ID)));

        Map<String, String> 방배역_역삼역_구간_파라미터 = 구간_등록_파라미터_생성(방배역_ID, 역삼역_ID);
        ExtractableResponse<Response> 지하철_구간_생성_응답 = 지하철_구간_등록(신분당선_ID, 방배역_역삼역_구간_파라미터);

        지하철_구간_등록됨(지하철_구간_생성_응답, 신분당선_ID, Arrays.asList(강남역_ID, 방배역_ID, 역삼역_ID));
    }

    /**
     * Given 구간을 하나 가진 노선을 생성하고
     * When 해당 노선의 하행 종점역이 아닌 상행역 파라미터로 지하철 구간을 등록하면
     * Then 등록에 실패한다.
     */
    @DisplayName("지하철 구간 등록에 실패한다-잘못된 상행역")
    @Test
    void createSectionWithInvalidUpStationOfParameter() {
        Long 신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터_생성(강남역_ID, 방배역_ID)));

        Map<String, String> 강남역_역삼역_구간_파라미터 = 구간_등록_파라미터_생성(강남역_ID, 역삼역_ID);
        ExtractableResponse<Response> 지하철_구간_생성_응답 = 지하철_구간_등록(신분당선_ID, 강남역_역삼역_구간_파라미터);

        실패함(지하철_구간_생성_응답);
    }

    /**
     * Given 구간을 하나 가진 노선을 생성하고
     * When 해당 노선에 등록된 역을 하행역 파라미터로 지하철 구간을 등록하면
     * Then 등록에 실패한다.
     */
    @DisplayName("지하철 구간 등록에 실패한다-잘못된 하행역")
    @Test
    void createSectionWithInvalidDownStationOfParameter() {
        Long 신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터_생성(강남역_ID, 방배역_ID)));

        Map<String, String> 방배역_강남역_구간_파라미터 = 구간_등록_파라미터_생성(방배역_ID, 강남역_ID);
        ExtractableResponse<Response> 지하철_구간_생성_응답 = 지하철_구간_등록(신분당선_ID, 방배역_강남역_구간_파라미터);

        실패함(지하철_구간_생성_응답);
    }

    /**
     * Given 지하철 노선에 구간을 두 개 등록하고
     * When 노선에 등록된 하행 종점역을 제거하면
     * Then 구간은 제거된다.
     */
    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void deleteSection() {
        Long 신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터_생성(강남역_ID, 방배역_ID)));
        Map<String, String> 방배역_역삼역_구간_파라미터 = 구간_등록_파라미터_생성(방배역_ID, 역삼역_ID);
        지하철_구간_등록(신분당선_ID, 방배역_역삼역_구간_파라미터);

        ExtractableResponse<Response> 지하철_구간_삭제_응답 = 지하철_구간_삭제(신분당선_ID, 역삼역_ID);

        구간_제거됨(지하철_구간_삭제_응답, 신분당선_ID, 역삼역_ID);
    }

    /**
     * Given 지하철 노선에 구간을 두 개 등록하고
     * When 노선에 등록된 하행 종점역이 아닌 역을 제거하면
     * Then 실패한다.
     */
    @DisplayName("지하철 구간을 삭제에 실패한다-하행종점역이 아닌 경우")
    @Test
    void deleteSectionWithStationThatIsNotDownwardEndPoint() {
        Long 신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터_생성(강남역_ID, 방배역_ID)));
        Map<String, String> 방배역_역삼역_구간_파라미터 = 구간_등록_파라미터_생성(방배역_ID, 역삼역_ID);
        지하철_구간_등록(신분당선_ID, 방배역_역삼역_구간_파라미터);

        ExtractableResponse<Response> 지하철_구간_삭제_응답 = 지하철_구간_삭제(신분당선_ID, 방배역_ID);

        실패함(지하철_구간_삭제_응답);
    }

    /**
     * Given 구간이 하나인 노선이 생성하고
     * When 하나뿐인 구간을 제거하면
     * Then 실패한다.
     */
    @DisplayName("지하철 구간을 삭제에 실패한다-구간이 하나인 경우")
    @Test
    void deleteSectionWhenLineHasOnlyOneSection() {
        Long 신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터_생성(강남역_ID, 방배역_ID)));

        ExtractableResponse<Response> 지하철_구간_삭제_응답 = 지하철_구간_삭제(신분당선_ID, 방배역_ID);

        실패함(지하철_구간_삭제_응답);
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response, Map<String, String> params) {
        적절한_응답_코드를_받을_수_있다(response, HttpStatus.CREATED);
        ExtractableResponse<Response> 지하철_노선_목록_조회_응답 = 지하철_노선_목록_조회();
        assertThat(지하철_노선_목록_조회_응답.jsonPath().getList("id").get(0)).isNotNull();
        assertThat(지하철_노선_목록_조회_응답.jsonPath().getList("name").get(0))
            .isEqualTo(params.get("name"));
        assertThat(지하철_노선_목록_조회_응답.jsonPath().getList("color").get(0))
            .isEqualTo(params.get("color"));
        assertThat(지하철_노선_목록_조회_응답.jsonPath().getString("stations[0].id[0]"))
            .isEqualTo(params.get("upStationId"));
        assertThat(지하철_노선_목록_조회_응답.jsonPath().getString("stations[0].id[1]"))
            .isEqualTo(params.get("downStationId"));
    }

    private void 지하철_노선_목록_조회됨(ExtractableResponse<Response> response,
        List<Map<String, String>> params) {
        적절한_응답_코드를_받을_수_있다(response, HttpStatus.OK);
        n개의_이름_목록을_조회할_수_있다(response,
            params.stream().map(p -> p.get("name")).toArray(String[]::new));
    }

    private void 지하철_노선_조회됨(ExtractableResponse<Response> response, Map<String, String> params) {
        적절한_응답_코드를_받을_수_있다(response, HttpStatus.OK);
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("name")).isEqualTo(params.get("name"));
        assertThat(response.jsonPath().getString("color")).isEqualTo(params.get("color"));
        assertThat(response.jsonPath().getString("stations.id[0]"))
            .isEqualTo(params.get("upStationId"));
        assertThat(response.jsonPath().getString("stations.id[1]"))
            .isEqualTo(params.get("downStationId"));
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response,
        Long lineId, Map<String, String> params) {
        적절한_응답_코드를_받을_수_있다(response, HttpStatus.OK);
        ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회(lineId);
        적절한_응답_코드를_받을_수_있다(지하철_노선_조회_응답, HttpStatus.OK);
        assertThat(지하철_노선_조회_응답.jsonPath().getString("name"))
            .isEqualTo(params.get("name"));
        assertThat(지하철_노선_조회_응답.jsonPath().getString("color"))
            .isEqualTo(params.get("color"));
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response, Long lineId) {
        적절한_응답_코드를_받을_수_있다(response, HttpStatus.NO_CONTENT);

        ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회(lineId);
        적절한_응답_코드를_받을_수_있다(지하철_노선_조회_응답, HttpStatus.NOT_FOUND);
    }


    private void 지하철_구간_등록됨(ExtractableResponse<Response> response, Long lineId,
        List<String> stationIds) {
        적절한_응답_코드를_받을_수_있다(response, HttpStatus.CREATED);
        ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회(lineId);
        for (int i = 0; i < stationIds.size(); i++) {
            assertThat(지하철_노선_조회_응답.jsonPath().getString("stations.id[" + i + "]"))
                .isEqualTo(stationIds.get(i));
        }
    }

    private void 구간_제거됨(ExtractableResponse<Response> response, Long lineId, String stationId) {
        적절한_응답_코드를_받을_수_있다(response, HttpStatus.NO_CONTENT);
        ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회(lineId);
        assertThat(지하철_노선_조회_응답.jsonPath().getList("stations.id")).doesNotContain(stationId);
    }

    private void 실패함(ExtractableResponse<Response> response) {
        적절한_응답_코드를_받을_수_있다(response, HttpStatus.BAD_REQUEST);
    }

}