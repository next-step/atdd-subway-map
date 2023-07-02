package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 지하철역 관련 api 스펙을 함수로 정의한다.
 */
@DisplayName("지하철역 관련 기능")
public abstract class StationAcceptanceTest extends AcceptanceTest {

    /**
     * 지하철 노선 생성 요청을 합니다
     * @param name 지하철 노선 이름
     * @return 지하철 노선 생성 요청 결과
     */
    protected ExtractableResponse<Response> 지하철역_생성(String name) {
        ParamBuilder params = new ParamBuilder()
                .add("name", name);

        return RestAssured
                .given().log().all()
                .body(params.build())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 노선 조회 요청합니다
     * @return 지하철 노선 조회 요청 결과
     */
    protected ExtractableResponse<Response> 지하철역_목록_조회() {

        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 노선 조회 요청을 합니다
     * @param name 확인할 지하철 노선 이름
     */
    protected void 지하철역_목록_포함_여부_확인(String name) {

        ExtractableResponse<Response> response = 지하철역_목록_조회();

        List<String> names = response.jsonPath().getList("name", String.class);
        Assertions.assertThat(names).containsExactly(name);
    }

    /**
     * 지하철 노선 삭제 요청을 합니다
     * @param name 지하철 노선 이름
     * @return 지하철 노선 삭제 요청 결과
     */
    protected ExtractableResponse<Response> 지하철역_삭제(String name) {
        ExtractableResponse<Response> response = 지하철역_목록_조회();

        long id = response.jsonPath().param("name", name).getLong("find { node -> node.name == name }.id");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 노선 조회 요청을 합니다
     * @param name 확인할 지하철 노선 이름
     */
    protected void 지하철역_목록_미포함_여부_확인(String name) {

        ExtractableResponse<Response> response = 지하철역_목록_조회();

        List<String> names = response.jsonPath().getList("name", String.class);
        Assertions.assertThat(names).doesNotContain(name);
    }
}