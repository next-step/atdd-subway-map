package nextstep.subway.acceptance.station;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StationRestAssuredProvider {

	public static ExtractableResponse<Response> 지하철역_생성(String stationName) {
		Map<String, String> params = new HashMap<>();
		params.put("name", stationName);

		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/stations")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철역_목록_조회() {
		return RestAssured.given().log().all()
			.when().get("/stations")
			.then().log().all()
			.extract();
	}

	public static void 지하철역_삭제(String id) {
		RestAssured.given().log().all()
			.when().delete("/stations/" + id)
			.then().log().all()
			.extract();
	}
}
