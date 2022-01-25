package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationRequest;

public class StationSteps {
	private static Map<String, StationRequest> initDataMap = new HashMap<String, StationRequest>(){
		{
			put("강남역", new StationRequest("강남역"));
			put("역삼역", new StationRequest("역삼역"));
		}
	};

	public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
		StationRequest request = initDataMap.getOrDefault(name, initDataMap.get("강남역"));
		return 지하철역_공통_생성_요청(request);
	}

	public static ExtractableResponse<Response> 지하철역_생성_요청(StationRequest request) {
		return 지하철역_공통_생성_요청(request);
	}

	private static ExtractableResponse<Response> 지하철역_공통_생성_요청(StationRequest object) {
		return RestAssured.given().log().all()
				.body(object)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/stations")
				.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철역_목록_조회() {
		return 지하철역_조회("/stations");
	}

	public static ExtractableResponse<Response> 지하철역_조회(String uri) {
		return RestAssured.given().log().all()
				.given().log().all()
				.when().get(uri)
				.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철역_수정(String uri, StationRequest request) {
		return RestAssured.given().log().all()
				.body(request)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.put("/stations")
				.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철역_삭제(String uri) {
		return RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.delete(uri)
				.then().log().all()
			.extract();
	}

	public static StationRequest 지하철역_데이터(String name) {
		return new StationRequest(initDataMap.getOrDefault(name, initDataMap.get("강남역")));
	}
}
