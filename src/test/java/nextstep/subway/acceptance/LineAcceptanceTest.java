package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineStep.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.AcceptanceTestUtils;
import nextstep.subway.utils.DatabaseCleanup;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest extends AcceptanceTest {
    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given, when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(지하철_생성_수정_요청_Params.이호선.getName(), 지하철_생성_수정_요청_Params.이호선.getColor());
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 생성 실패 - 중복 이름")
    @Test
    void createLineThatFailing() {
        // given
        지하철_노선_생성_요청(지하철_생성_수정_요청_Params.이호선.getName(), 지하철_생성_수정_요청_Params.이호선.getColor());

        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(지하철_생성_수정_요청_Params.이호선.getName(), 지하철_생성_수정_요청_Params.이호선.getColor());

        // then
        assertThat(createResponse.statusCode()).isNotEqualTo(HttpStatus.CREATED.value());
        assertThat(AcceptanceTestUtils.getLocation(createResponse)).isNull();
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
        // given
        지하철_노선_생성_요청(지하철_생성_수정_요청_Params.이호선.getName(), 지하철_생성_수정_요청_Params.이호선.getColor());
        지하철_노선_생성_요청(지하철_생성_수정_요청_Params.삼호선.getName(), 지하철_생성_수정_요청_Params.삼호선.getColor());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when()
                                                            .get("/lines")
                                                            .then().log().all()
                                                            .extract();

        //then
        List<String> lineNames = response.jsonPath()
                                         .getList("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineNames).containsExactly(지하철_생성_수정_요청_Params.이호선.getName(), 지하철_생성_수정_요청_Params.삼호선.getName());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(지하철_생성_수정_요청_Params.이호선.getName(), 지하철_생성_수정_요청_Params.이호선.getColor());

        // when
        ExtractableResponse<Response> response = AcceptanceTestUtils.requestLocation(createResponse, Method.GET);

        // then
        String lineName = response.jsonPath()
                                  .get("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineName).isEqualTo(지하철_생성_수정_요청_Params.이호선.getName());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(지하철_생성_수정_요청_Params.이호선.getName(), 지하철_생성_수정_요청_Params.이호선.getColor());

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", 지하철_생성_수정_요청_Params.삼호선.getName());
        params.put("color", 지하철_생성_수정_요청_Params.삼호선.getColor());
        ExtractableResponse<Response> editResponse = AcceptanceTestUtils.requestLocation(createResponse, Method.PUT, params);

        // then
        ExtractableResponse<Response> findResponse = AcceptanceTestUtils.requestLocation(createResponse, Method.GET);

        assertThat(editResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat((String)findResponse.jsonPath()
                                       .get("name")).isEqualTo(지하철_생성_수정_요청_Params.삼호선.getName());
        assertThat((String)findResponse.jsonPath()
                                       .get("color")).isEqualTo(지하철_생성_수정_요청_Params.삼호선.getColor());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(지하철_생성_수정_요청_Params.이호선.getName(), 지하철_생성_수정_요청_Params.이호선.getColor());

        // when
        ExtractableResponse<Response> deleteResponse = AcceptanceTestUtils.requestLocation(createResponse, Method.DELETE);

        // then
        ExtractableResponse<Response> findResponse = AcceptanceTestUtils.requestLocation(createResponse, Method.GET);

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThatThrownBy(() -> findResponse.jsonPath().get("name"))
            .isInstanceOf(JsonPathException.class);
    }
}
