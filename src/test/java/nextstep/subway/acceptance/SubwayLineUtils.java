package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SubwayLineUtils {

	public static ExtractableResponse<Response> 지하철노선을_등록한다(String name, String color, Long upStationId, Long downStationId, Integer distance) {

		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);
		params.put("distance", distance);

		return RestAssured
				.given()
					.log().all()
					.body(params)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.post("/lines")
				.then()
					.log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 지하철노선_목록을_조회한다() {
		return RestAssured
				.given()
					.log().all()
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.get("/lines")
				.then()
					.log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 지하철노선_하나를_조회한다(Long subwayLineId) {
		return RestAssured
				.given()
					.log().all()
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.get("/lines/{id}", subwayLineId)
				.then()
					.log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 지하철노선을_수정한다(Long subwayLineId, String name, String color) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);

		return RestAssured
				.given()
					.log().all()
					.body(params)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.put("/lines/{id}", subwayLineId)
				.then()
					.log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선을_삭제한다(Long subwayLineId) {
		return RestAssured
				.given()
					.log().all()
				.when()
					.delete("/lines/{id}", subwayLineId)
				.then()
					.log().all()
				.extract();
	}
}
