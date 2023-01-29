package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestFixtureStation {

    public static void 지하철역_조회됨(ExtractableResponse<Response> response, final String station) {
        final JsonPath 지하철역_응답_경로 = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철역_응답_경로.getList("")).hasSize(1),
                () -> assertThat(지하철역_응답_경로.getLong("[0].id")).isEqualTo(1L),
                () -> assertThat(지하철역_응답_경로.getString("[0].name")).isEqualTo(station)
        );
    }

    public static void 지하철역_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 지하철역_목록_조회됨(final ExtractableResponse<Response> 지하철역_목록_응답, final String...station) {
        final JsonPath 지하철역_응답_경로 = 지하철역_목록_응답.response().body().jsonPath();

        assertAll(
                () -> assertThat(지하철역_목록_응답.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철역_응답_경로.getList("")).hasSize(2),
                () -> assertThat(지하철역_응답_경로.getLong("[0].id")).isEqualTo(1L),
                () -> assertThat(지하철역_응답_경로.getString("[0].name")).isEqualTo(station[0]),
                () -> assertThat(지하철역_응답_경로.getLong("[1].id")).isEqualTo(2L),
                () -> assertThat(지하철역_응답_경로.getString("[1].name")).isEqualTo(station[1])
        );
    }

    public static ExtractableResponse<Response> 지하철역_목록_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청함(String name) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static Long 지하철역_생성_요청(final String name) {
        final ExtractableResponse<Response> response = 지하철역_생성_요청함(name);
        지하철역_생성됨(response);
        return response.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(ExtractableResponse<Response> response) {
        final String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철역_조회되지_않음(final ExtractableResponse<Response> response) {
        final JsonPath 지하철역_응답_경로 = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철역_응답_경로.getList("")).isEmpty()

        );
    }
}
