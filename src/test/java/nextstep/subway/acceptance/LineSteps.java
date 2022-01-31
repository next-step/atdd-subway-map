package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;

public class LineSteps {

	private static Map<String, LineRequest> initDataMap = new HashMap<String, LineRequest>() {
		{
			put("신분당선", new LineRequest("신분당선", "bg-red-600"));
			put("1호선", new LineRequest("1호선", "bg-blue-600"));
			put("2호선", new LineRequest("2호선", "bg-green-600"));
		}
	};

	public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name) {
		LineRequest request = initDataMap.getOrDefault(name, initDataMap.get("신분당선"));
		return 노선_공통_생성_요청(request);
	}

	public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest request) {
		return 노선_공통_생성_요청(request);
	}

	private static ExtractableResponse<Response> 노선_공통_생성_요청(Object object) {
		return RestAssured.given().log().all()
			.body(object)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/lines")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
		return 지하철_노선_조회("/lines");
	}

	public static ExtractableResponse<Response> 지하철_노선_조회(String uri) {
		return RestAssured.given().log().all()
			.given().log().all()
			.when().get(uri)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_수정(String uri, LineRequest request) {
		return RestAssured.given().log().all()
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.put(uri)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철_노선_삭제(String uri) {
		return RestAssured.given().log().all()
			.when()
			.delete(uri)
			.then().log().all()
			.extract();
	}

	public static LineRequest 노선_데이터(String name) {
		return new LineRequest(initDataMap.getOrDefault(name, initDataMap.get("신분당선")));
	}

	public enum Line {
		신분당선("신분당선"),
		일호선("1호선"),
		이호선("2호선")
		;

		private String name;

		Line(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
