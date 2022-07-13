package nextstep.subway.acceptance.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationUtils {

	public static ExtractableResponse<Response> 지하철역을_등록한다(String stationName) {
		Map<String, String> param = new HashMap<>();
		param.put("name", stationName);

		return RestAssured
				.given()
					.log().all()
					.body(param)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.post("/stations")
				.then()
					.log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 지하철역_목록을_찾는다() {
		return RestAssured
				.given()
					.log().all()
				.when()
					.get("/stations")
				.then()
					.log().all()
				.extract();
	}

	public static ExtractableResponse<Response> 지하철역을_삭제한다(Integer id) {
		return RestAssured
				.given()
					.log().all()
				.when()
					.delete("/stations/{id}", id)
				.then()
					.log().all()
				.extract();
	}
}
