package nextstep.subway.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineStep {

	private ExtractableResponse<Response> saveLine(final String color, final String name) {
		Map<String, String> request = new HashMap<>();
		request.put("color", color);
		request.put("name", name);

		return RestAssured
						.given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
						.when().post("/lines")
						.then().log().all()
						.extract();
	}
}
