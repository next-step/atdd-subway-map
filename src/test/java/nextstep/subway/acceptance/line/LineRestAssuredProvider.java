package nextstep.subway.acceptance.line;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LineRestAssuredProvider {

	public static ExtractableResponse<Response> 지하철_노선_등록(String name, String color, String upStationId,
		String downStationId, String distance) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);
		params.put("distance", distance);

		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
		return RestAssured.given().log().all()
			.when().get("/lines")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_조회(String id) {
		return RestAssured.given().log().all()
			.when().get("/lines/", id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_수정(String id, String name, String color) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);

		return RestAssured.given().log().all()
			.body(params)
			.when().put("/lines/" + id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_제거(String id) {
		return RestAssured.given().log().all()
			.when().delete("/lines/" + id)
			.then().log().all()
			.extract();
	}
}
