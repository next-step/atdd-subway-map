package nextstep.subway.acceptance;

import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LineAcceptanceStatic {
	public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, Object> param) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(param)
			.when()
			.post("/lines")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
		return RestAssured.given().log().all()
			.when()
			.get("/lines")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_조회_요청(long lineId) {
		return RestAssured.given().log().all()
			.when()
			.get("/lines/{id}", lineId)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_삭제_요청(long lineId) {
		return RestAssured.given().log().all()
			.when()
			.delete("/lines/{lineId}", lineId)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_수정_요청(long lineId, Map<String, Object> param) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(param)
			.when()
			.put("/lines/{lineId}", lineId)
			.then().log().all().extract();
	}

	public static Long 지하철_노선_생성되어_있음(String lineName, String lineColor, Long upStationId, Long downStationId, int distance) {
		Map<String, Object> param =
			Map.of("name", lineName, "color", lineColor, "upStationId", upStationId, "downStationId", downStationId, "distance", distance);
		return 지하철_노선_생성_요청(param).jsonPath().getLong("id");
	}

	public static ExtractableResponse<Response> 구간_등록_요청(Long 신분당선_노선_번호, Map<String, Object> param) {
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(param)
			.when()
			.post("/lines/{lineId}/sections", 신분당선_노선_번호)
			.then().log().all().extract();
		return response;
	}

}
