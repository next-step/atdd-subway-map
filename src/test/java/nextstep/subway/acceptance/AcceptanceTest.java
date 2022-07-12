package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

	ExtractableResponse<Response> get(String path) {
		return RestAssured.given().log().all()
			.when().get(path)
			.then().log().all().extract();
	}

	ExtractableResponse<Response> post(String path, Object params) {
		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post(path)
			.then().log().all().extract();
	}

	ExtractableResponse<Response> delete(String path) {
		return RestAssured.given().log().all()
			.when().delete(path)
			.then().log().all().extract();
	}

	ExtractableResponse<Response> put(String path, Object params) {
		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put(path)
			.then().log().all().extract();
	}
}
