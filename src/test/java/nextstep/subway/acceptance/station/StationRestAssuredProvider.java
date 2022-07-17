package nextstep.subway.acceptance.station;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StationRestAssuredProvider {

	public static ExtractableResponse<Response> 지하철역_생성(String 역이름) {
		Map<String, String> params = new HashMap<>();
		params.put("name", 역이름);

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

	public static ExtractableResponse<Response> 지하철역_제거(String id) {
		return RestAssured.given().log().all()
			.when().delete("/stations/{id}", id)
			.then().log().all()
			.extract();
	}
}
