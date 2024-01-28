package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
	@DisplayName("지하철역을 생성한다.")
	@Test
	void create_station() {
		// when
		Map<String, String> body = createBody("강남역");
		ExtractableResponse<Response> response = createStation(body);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> stationNames = getStations();
		assertThat(stationNames).containsAnyOf("강남역");
	}

	@DisplayName("생성한 지하철역을 조회한다.")
	@Test
	void get_stations() {
		// given
		createStation(createBody("강남역1"));
		createStation(createBody("강남역2"));

		// when
		List<String> stationNames = getStations();

		// then
		assertThat(stationNames).containsAnyOf("강남역1");
		assertThat(stationNames).containsAnyOf("강남역2");
	}

	@DisplayName("지하철 역을 삭제한다.")
	@Test
	void delete_station() {
		// given
		ExtractableResponse<Response> response = createStation(createBody("강남역1"));
		long stationId = response.body().jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> deleteResponse = deleteStation(stationId);

		// then
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private Map<String, String> createBody(String stationName) {
		Map<String, String> params = new HashMap<>();
		params.put("name", stationName);

		return params;
	}

	private ExtractableResponse<Response> createStation(Map<String, String> params) {
		return RestAssured.given()
				.log()
				.all()
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/stations")
				.then()
				.log()
				.all()
				.extract();
	}

	private List<String> getStations() {
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

	private ExtractableResponse<Response> deleteStation(Long id) {
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
