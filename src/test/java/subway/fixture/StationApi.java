package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.MediaType;
import subway.dto.StationRequest;

public class StationApi {
	public static ExtractableResponse<Response> createStation(StationRequest request) {
		return RestAssured.given()
				.log()
				.all()
				.body(request)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/stations")
				.then()
				.log()
				.all()
				.extract();
	}

	public static List<String> getStations() {
		return RestAssured.given()
				.log()
				.all()
				.when()
				.get("/stations")
				.then()
				.log()
				.all()
				.extract()
				.jsonPath()
				.getList("name", String.class);
	}

	public static ExtractableResponse<Response> deleteStation(Long id) {
		return RestAssured.given()
				.log()
				.all()
				.when()
				.delete("/stations/" + id)
				.then()
				.log()
				.all()
				.extract();
	}
}
