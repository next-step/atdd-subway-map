package nextstep.subway.acceptance;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StationAcceptanceStatic {

	public static ExtractableResponse<Response> 지하철_생성_요청(Map<String, String> params) {
		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/stations")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_목록_조회_요청() {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/stations")
			.then().log().all()
			.extract();
	}

	public static Long 지하철역_생성되어_있음(Map<String, String> param) {
		return 지하철_생성_요청(param).jsonPath().getLong("id");
	}

	public static List<String> 지하철역_이름_조회() {
		return 지하철_목록_조회_요청().jsonPath().getList("name", String.class);
	}

	public static ExtractableResponse<Response> 지하철역_삭제_요청(Long stationId) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/stations/{stationId}", stationId)
			.then().log().all().extract();
	}

}
