package nextstep.subway.acceptance.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public abstract class CommonAcceptanceTest extends ConfigAcceptanceTest {

	public ExtractableResponse<Response> 지하철_역_생성(String name) {
		Map<String, String> station = new HashMap<>();
		station.put("name", name);

		return RestAssured.given().log().all()
				.body(station)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/stations")
				.then().log().all()
				.extract();
	}

	public ExtractableResponse<Response> 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Integer distance) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);
		params.put("distance", distance);

		return RestAssured
				.given().log().all()
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/lines")
				.then().log().all()
				.extract();
	}

	public ExtractableResponse<Response> 지하철_노선_조회(Long id) {
		return RestAssured
				.given().log().all()
				.when().get("/lines/{lineId}", id)
				.then().log().all()
				.extract();
	}
}
