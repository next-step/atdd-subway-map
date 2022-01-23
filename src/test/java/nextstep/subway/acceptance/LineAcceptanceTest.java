package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.exception.DuplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.HttpRequestTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String 기본주소 = "/lines";
    private static final String 기존노선 = "기존노선";
    private static final String 기존색상 = "기존색상";
    private static final String 새로운노선 = "새로운노선";
    private static final String 새로운색상 = "새로운색상";
    private static final String 수정노선 = "수정 노선";
    private static final String 수정색상 = "수정 색상";

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void 노선생성_테스트() {
        //when
        ExtractableResponse<Response> response = 기존노선생성();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선을 생성한다.
     * Given 새로운 지하철 노선을 생성한다.
     * When 지하철 노선 조회를 요청한다.
     * Then 지하철 노선을 반환한다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void 노선목록조회_테스트() {
        //given
        기존노선생성();
        새로운노선생성();

        //when
        ExtractableResponse<Response> response = getRequest(기본주소);

        //then
        assertThat(response.jsonPath().getList("name")).contains(새로운노선, 기존노선);
        assertThat(response.jsonPath().getList("color")).contains(새로운색상, 새로운색상);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성된 지하철 노선을 요청한다.
     * Then 생성된 지하철 노선을 반환한다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void 노선조회_테스트() {
        //given
        ExtractableResponse<Response> createResponse = 기존노선생성();

        //when
        ExtractableResponse<Response> response = getRequest(createResponse.header(HttpHeaders.LOCATION));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(기존노선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선 수정을 요청한다,
     * Then 지하철 노선이 수정이 완료된다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void 노선업데이트_테스트() {
        //given
        ExtractableResponse<Response> createResponse = 기존노선생성();

        //when
        ExtractableResponse<Response> updateResponse = 노선수정(createResponse);

        //then
        ExtractableResponse<Response> response = getRequest(createResponse.header(HttpHeaders.LOCATION));

        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(수정노선);
    }


    /**
     * Given 지하철 노션을 생성을 요청한다.
     * When 생성된 지하철 노션을 삭제를 요청한다.
     * Then 지하철 노션이 삭제된다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void 노선삭제_테스트() {
        //given
        ExtractableResponse<Response> createResponse = 기존노선생성();

        //when
        ExtractableResponse<Response> response = deleteRequest(createResponse.header(HttpHeaders.LOCATION));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선을 생성한다.
     * When 중복된 이름의 지하철 노선 생성을 요청한다.
     * Then 지하철 노선 생성이 실패한다.
     */

    @DisplayName("중복된 노선 생성은 실패한다")
    @Test
    void duplicationLine() {
        //given
        기존노선생성();

        //when
        ExtractableResponse<Response> response = 기존노선생성();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(DuplicationException.MESSAGE);
    }

    private ExtractableResponse<Response> 기존노선생성() {
        Map<String, String> param = 노선파라미터생성(기존노선, 기존색상);
        return postRequest(기본주소, param);
    }

    private ExtractableResponse<Response> 새로운노선생성() {
        Map<String, String> param = 노선파라미터생성(새로운노선, 새로운색상);
        return postRequest(기본주소, param);
    }

    private ExtractableResponse<Response> 노선수정(ExtractableResponse<Response> createResponse) {
        Map<String, String> updateParam = 노선파라미터생성(수정노선, 수정색상);
        ExtractableResponse<Response> updateResponse = putRequest(createResponse.header(HttpHeaders.LOCATION), updateParam);
        return updateResponse;
    }

    private Map<String, String> 노선파라미터생성(String 노선, String 색상) {
        Map<String, String> param = new HashMap<>();
        param.put("name", 노선);
        param.put("color", 색상);
        return param;
    }
}
