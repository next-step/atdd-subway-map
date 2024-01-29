package subway.create;

import java.util.HashMap;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;

public class StationCreator {
	public static Long action(String stationName) {
		HashMap<String, String> param = new HashMap<>();
		param.put("name", stationName);

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(param)
			.when().post("/stations")
			.then().log().all()
			.extract().jsonPath().getLong("id");
	}
}
