package nextstep.subway.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.ui.StationController;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationStep {

	public static ExtractableResponse<Response> saveStation(final String name) {
		Map<String, String> request = request(name);

		return RestAssured
						.given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
						.when().post(StationController.STATIONS)
						.then().log().all()
						.extract();
	}

	public static ExtractableResponse<Response> showStation() {

		return RestAssured.given().log().all()
						.when()
						.get(StationController.STATIONS)
						.then().log().all()
						.extract();
	}

	public static ExtractableResponse<Response> deleteStation(final String uri) {

		return RestAssured.given().log().all()
						.when()
						.delete(uri)
						.then().log().all()
						.extract();
	}

	// 요청을 생성하는 메소드
	public static Map<String, String> request(final String name) {
		Map<String, String> request = new HashMap<>();
		request.put("name", name);

		return request;
	}
}
