package testhelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/01/26
 */
public class StationRequestExecutor {

	private static final String URL_PATH = "/stations";

	public static ExtractableResponse<Response> executeCreateStationRequest(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);

		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post(URL_PATH)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> executeDeleteStationRequest(Long id) {
		return RestAssured.given().log().all()
			.when().delete(URL_PATH + id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> executeGetStationRequest() {
		return RestAssured.given().log().all()
			.when().get(URL_PATH)
			.then().log().all()
			.extract();
	}
}
