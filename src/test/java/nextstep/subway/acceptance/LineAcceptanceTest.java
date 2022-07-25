package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.provider.StationProvider;
import nextstep.subway.utils.DatabaseCleanUp;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    @Autowired
    private StationProvider stationProvider;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();
        stationProvider.createStations(List.of("지하철역", "새로운지하철역", "또다른지하철역"));
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * CreateParam 정보: ( name, color, upStationId, downStationId, distance )
     */
    @DisplayName("지하철 노선을 생성합니다.")
    @Test
    void createSubwayLine() throws Exception {
        // when
        final String 생성할_지하철노선_이름 = "신분당선";
        final ExtractableResponse<Response> 지하철노선_생성_응답 = 지하철노선_생성_요청(생성할_지하철노선_이름);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철노선_생성_응답, CREATED);
        지하철노선이_정상적으로_생성되었는지_확인(지하철노선_생성_응답);
        지하철노선_목록에_생성한_지하철노선이_있는지_확인(생성할_지하철노선_이름);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회합니다.")
    @Test
    void getSubwayLines() throws Exception {
        // given
        final List<String> 생성할_지하철노선들_이름 = Lists.newArrayList("신분당선", "분당선");
        지하철노선_생성_요청(생성할_지하철노선들_이름);

        // when
        final ExtractableResponse<Response> 지하철노선_목록_조회_응답 = 지하철노선_목록_조회_요청();

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철노선_목록_조회_응답, OK);
        지하철노선_목록에_생성한_지하철노선이_있는지_확인(생성할_지하철노선들_이름);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    @DisplayName("지하철 노선을 조회합니다.")
    @Test
    void getSubwayLine() throws Exception {
        // given
        final String 생성할_지하철노선_이름 = "신분당선";
        final ExtractableResponse<Response> 지하철노선_생성_응답 = 지하철노선_생성_요청(생성할_지하철노선_이름);
        final long 생성된_지하철노선_아이디 = 지하철노선_생성_응답.jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> 지하철노선_상세_정보_조회_응답 = 지하철노선_상세_정보_조회_요청(생성된_지하철노선_아이디);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철노선_상세_정보_조회_응답, OK);
        조회한_지하철노선이_생성한_지하철노선인지_확인(생성된_지하철노선_아이디, 지하철노선_상세_정보_조회_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정합니다.")
    @Test
    void updateSubwayLine() throws Exception {
        // given
        final ExtractableResponse<Response> 지하철노선_생성_응답 = 지하철노선_생성_요청("신분당선");
        final long 생성된_지하철노선_아이디 = 지하철노선_생성_응답.jsonPath().getLong("id");

        final Map<String, Object> 변경될_지하철노선_정보 = 요청보낼_파라미터_생성(
                List.of("name", "color"),
                List.of("다른분당선", "bg-green-600")
        );

        // when
        final ExtractableResponse<Response> 지하철노선_정보_변경_응답 = 지하철노선_정보_변경_요청(생성된_지하철노선_아이디, 변경될_지하철노선_정보);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철노선_정보_변경_응답, OK);

        final ExtractableResponse<Response> 지하철노선_상세_정보_조회_응답 = 지하철노선_상세_정보_조회_요청(생성된_지하철노선_아이디);
        지하철노선_변경이_잘_이루어졌는지_확인(변경될_지하철노선_정보, 지하철노선_상세_정보_조회_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제합니다.")
    @Test
    void deleteSubwayLine() throws Exception {
        // given
        final ExtractableResponse<Response> 지하철노선_생성_응답 = 지하철노선_생성_요청("신분당선");
        final long createdSubwayLineId = 지하철노선_생성_응답.jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> 지하철노선_삭제_응답 = 지하철노선_삭제_요청(createdSubwayLineId);

        // then
        요청이_정상적으로_이루어졌는지_확인(지하철노선_삭제_응답, NO_CONTENT);
        지하철노선이_정상적으로_삭제되었는지_확인();
    }

    private Map<String, Object> 요청보낼_파라미터_생성(List<String> keys, List<Object> values) {
        if (keys.size() != values.size()) {
            throw new RuntimeException("생성하려는 key 와 value 의 length 가 같아야 합니다.");
        }

        final Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            params.put(keys.get(i), values.get(i));
        }

        return params;
    }

    private ExtractableResponse<Response> 지하철노선_생성_요청(String subwayLineName) {
        final Map<String, Object> params = 요청보낼_파라미터_생성(
                List.of("name", "color", "upStationId", "downStationId", "distance"),
                List.of(subwayLineName, "bg-red-600", 1, 2, 10));
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/subway-lines")
                .then().log().all()
                .extract();

        return response;
    }

    private void 지하철노선_생성_요청(List<String> subwayLineNames) {
        for (final String subwayLineName : subwayLineNames) {
            지하철노선_생성_요청(subwayLineName);
        }
    }

    private ExtractableResponse<Response> 지하철노선_목록_조회_요청() {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/subway-lines")
                .then().log().all()
                .extract();

        return response;
    }

    private ExtractableResponse<Response> 지하철노선_상세_정보_조회_요청(Long subwayLineId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/subway-lines/{subwayLineId}", subwayLineId)
                .then().log().all()
                .extract();

        return response;
    }

    private ExtractableResponse<Response> 지하철노선_정보_변경_요청(Long subwayLineId, Map<String, Object> params) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/subway-lines/{subwayLineId}", subwayLineId)
                .then().log().all()
                .extract();

        return response;
    }

    private ExtractableResponse<Response> 지하철노선_삭제_요청(Long subwayLineId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/subway-lines/{subwayLineId}", subwayLineId)
                .then().log().all()
                .extract();

        return response;
    }

    private void 요청이_정상적으로_이루어졌는지_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    private void 지하철노선이_정상적으로_생성되었는지_확인(ExtractableResponse<Response> response) {
        final List<Object> stations = response.jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(2);
    }

    private void 지하철노선_목록에_생성한_지하철노선이_있는지_확인(String createdSubwayLineName) {
        final ExtractableResponse<Response> getSubwayLinesResponse = 지하철노선_목록_조회_요청();
        final List<String> subwayLineNames = getSubwayLinesResponse.jsonPath().getList("name", String.class);
        assertThat(subwayLineNames).contains(createdSubwayLineName);
    }

    private void 지하철노선_목록에_생성한_지하철노선이_있는지_확인(List<String> createdSubwayLineNames) {
        final ExtractableResponse<Response> getSubwayLinesResponse = 지하철노선_목록_조회_요청();
        final List<String> subwayLineNames = getSubwayLinesResponse.jsonPath().getList("name", String.class);
        assertThat(subwayLineNames).containsAll(createdSubwayLineNames);
    }

    private void 조회한_지하철노선이_생성한_지하철노선인지_확인(Long 생성된_지하철노선_아이디, ExtractableResponse<Response> 지하철노선_상세_정보_조회_응답) {
        final long 조회한_지하철노선_고유_번호 = 지하철노선_상세_정보_조회_응답.jsonPath().getLong("id");
        assertThat(조회한_지하철노선_고유_번호).isEqualTo(생성된_지하철노선_아이디);
    }

    private void 지하철노선_변경이_잘_이루어졌는지_확인(Map<String, Object> 변경될_지하철노선_정보, ExtractableResponse<Response> 지하철노선_상세_정보_조회_응답) {
        assertThat(지하철노선_상세_정보_조회_응답.jsonPath().getString("name")).isEqualTo(변경될_지하철노선_정보.get("name"));
        assertThat(지하철노선_상세_정보_조회_응답.jsonPath().getString("color")).isEqualTo(변경될_지하철노선_정보.get("color"));
    }

    private void 지하철노선이_정상적으로_삭제되었는지_확인() {
        final ExtractableResponse<Response> 지하철노선_목록_조회_응답 = 지하철노선_목록_조회_요청();
        final List<Object> subwayLineIdList = 지하철노선_목록_조회_응답.jsonPath().getList("id");
        assertThat(subwayLineIdList).hasSize(0);
    }
}
