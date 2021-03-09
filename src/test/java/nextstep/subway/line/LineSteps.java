package nextstep.subway.line;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LineSteps {
	public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> map) {
		return RestAssured
			.given().log().all()
			.body(map)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();
	}

	public static Map<String, String> 노선_생성(
		String name, String color, String upStationId,
		String downStationId, String distance) {
		Map<String, String> map = new HashMap<>();
		map.put("name", name);
		map.put("color", color);
		map.put("upStationId", upStationId);
		map.put("downStationId", downStationId);
		map.put("distance", distance);
		return map;
	}
}
