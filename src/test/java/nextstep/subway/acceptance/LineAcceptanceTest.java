package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.utils.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String DEFAULT_PATH = "/lines";

    private static final String PARAM1_NAME_VALUE = "신분당선";
    private static final String PARAM1_COLOR_VALUE = "bg-red-600";
    private static final Long PARAM1_UPSTATION_ID = 1L;
    private static final Long PARAM1_DOWNSTATION_ID = 2L;
    private static final int PARAM1_DISTANCE_VALUE = 5;

    private static final String PARAM2_NAME_VALUE = "2호선";
    private static final String PARAM2_COLOR_VALUE = "bg-green-600";
    private static final Long PARAM2_UPSTATION_ID = 10L;
    private static final Long PARAM2_DOWNSTATION_ID = 20L;
    private static final int PARAM2_DISTANCE_VALUE = 10;

    private LineRequest param1;
    private LineRequest param2;

    @BeforeEach
    void paramsInit() {
        param1 = new LineRequest(PARAM1_NAME_VALUE, PARAM1_COLOR_VALUE,
            PARAM1_UPSTATION_ID, PARAM1_DOWNSTATION_ID, PARAM1_DISTANCE_VALUE);

        param2 = new LineRequest(PARAM2_NAME_VALUE, PARAM2_COLOR_VALUE,
            PARAM2_UPSTATION_ID, PARAM2_DOWNSTATION_ID, PARAM2_DISTANCE_VALUE);
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        // when
        ExtractableResponse<Response> response = RequestMethod.post(DEFAULT_PATH, param1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given
     * When 노선 생성 시 두 종점역을 모두 입력하지 않으면
     * Then 생성에 실패한다
     */
    @Test
    @DisplayName("지하철 노선 생성 실패")
    void createLineExceptionTest() {
        //given
        // when
        ExtractableResponse<Response> response = RequestMethod.post(DEFAULT_PATH, param1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        //given
        RequestMethod.post(DEFAULT_PATH, param1);
        RequestMethod.post(DEFAULT_PATH, param2);

        //when
        ExtractableResponse<Response> response = RequestMethod.get(DEFAULT_PATH);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).contains(PARAM1_NAME_VALUE, PARAM2_NAME_VALUE);
        assertThat(response.jsonPath().getList("color")).contains(PARAM1_COLOR_VALUE, PARAM2_COLOR_VALUE);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        ExtractableResponse<Response> createResponse = RequestMethod.post(DEFAULT_PATH, param1);

        // when
        ExtractableResponse<Response> response = RequestMethod.get(
           DEFAULT_PATH + "/" + createResponse.jsonPath().getString("id"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given
        ExtractableResponse<Response> createResponse = RequestMethod.post(DEFAULT_PATH, param1);

        // when
        ExtractableResponse<Response> response = RequestMethod.put(
            DEFAULT_PATH + "/" + createResponse.jsonPath().getString("id"), param2);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        ExtractableResponse<Response> createResponse = RequestMethod.post(DEFAULT_PATH, param1);

        // when
        ExtractableResponse<Response> response = RequestMethod.delete(
             DEFAULT_PATH + "/" + createResponse.jsonPath().getString("id"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @Test
    @DisplayName("중복이름으로 지하철 노선 생성 실패")
    void duplicationLineNameExceptionTest() {
        // given
        RequestMethod.post(DEFAULT_PATH, param1);

        // when
        ExtractableResponse<Response> response = RequestMethod.post(DEFAULT_PATH, param1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

}
