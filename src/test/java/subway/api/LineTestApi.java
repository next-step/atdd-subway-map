package subway.api;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.line.LineCreateRequest;

public class LineTestApi {

	public static ExtractableResponse<Response> createLine(String name, String color, Long upStationId, Long downStationId, int distance) {
		LineCreateRequest lineRequest = new LineCreateRequest(name, color, upStationId, downStationId, distance);

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return response;
	}

	public static ExtractableResponse<Response> showLines() {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().get("/lines")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		return response;
	}

	public static ExtractableResponse<Response> showLineById(Long id) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().get("/lines/{id}", id)
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		return response;
	}

	public static ExtractableResponse<Response> updateLine(Long id, String name, String color) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put("/lines/{id}", id)
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		return response;
	}

	public static ExtractableResponse<Response> deleteLine(Long id) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().delete("/lines/{id}", id)
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		ExtractableResponse<Response> afterDeleteResponse = RestAssured
			.given().log().all()
			.when().delete("/lines/{id}", id)
			.then().log().all()
			.extract();

		return afterDeleteResponse;
	}
}
