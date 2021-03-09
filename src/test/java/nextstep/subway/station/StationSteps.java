package nextstep.subway.station;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StationSteps {
	public static Map<String, String> 지하철역_생성(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		return params;
	}

	public static ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> params) {
		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/stations")
			.then().log().all()
			.extract();
	}
}
