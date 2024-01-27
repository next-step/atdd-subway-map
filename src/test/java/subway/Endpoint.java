package subway;

import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class Endpoint {

	// 지하철역 생성 Endpoint
	static ExtractableResponse<Response> createStation(Map<String, String> param) {
		return io.restassured.RestAssured.given().log().all()
			.body(param)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/stations")
			.then().log().all()
			.extract();
	}

	// 지하철역 목록 조회 Endpoint
	static ExtractableResponse<Response> showStations() {
		return io.restassured.RestAssured.given().log().all()
			.when().get("/stations")
			.then().log().all()
			.extract();
	}

	// 지하철역 삭제 Endpoint
	static ExtractableResponse<Response> deleteStation(Long id) {
		return io.restassured.RestAssured.given().log().all()
			.pathParam("id", id)
			.when().delete("/stations/{id}")
			.then().log().all()
			.extract();
	}
}
