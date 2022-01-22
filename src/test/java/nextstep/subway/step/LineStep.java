package nextstep.subway.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineStep {

	// 생성
	public static ExtractableResponse<Response> saveLine(final String color, final String name) {
		Map<String, String> request = request(color, name);

		return RestAssured
						.given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
						.when().post("/lines")
						.then().log().all()
						.extract();
	}

	// 노선 조회
	public static ExtractableResponse<Response> showLine(final Integer id) {

		return RestAssured
						.given().log().all()
						.when().get("/lines/{id}", id)
						.then().log().all()
						.extract();

	}

	// 노선들 조회
	public static ExtractableResponse<Response> showLines() {

		return RestAssured
						.given().log().all()
						.when().get("/lines")
						.then().log().all()
						.extract();
	}


	// 수정
	public static ExtractableResponse<Response> updateLine(final String color, final String name, final Integer id) {
		Map<String, String> request = request(color, name);

		return RestAssured
						.given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
						.when().put("/lines/{id}", id)
						.then().log().all()
						.extract();
	}

	// 삭제
	public static ExtractableResponse<Response> deleteLine(final Integer id) {

		return RestAssured
						.given().log().all()
						.when().delete("/lines/{id}", 1)
						.then().log().all()
						.extract();
	}

	// 요청을 생성하는 메소드
	public static Map<String, String> request(final String color, final String name) {
		Map<String, String> request = new HashMap<>();
		request.put("color", color);
		request.put("name", name);

		return request;
	}
}
