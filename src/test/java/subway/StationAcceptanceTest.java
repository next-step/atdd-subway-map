package subway;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
@Sql(scripts = "classpath:reset.sql", executionPhase = AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

	@DisplayName("지하철역을 생성한다.")
	@Test
	void createStation() {
		// when
		Map<String, String> params = new HashMap<>();
		params.put("name", "강남역");

		ExtractableResponse<Response> response =
			RestAssured.given().log().all()
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/stations")
				.then().log().all()
				.extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> stationNames =
			RestAssured.given().log().all()
				.when().get("/stations")
				.then().log().all()
				.extract().jsonPath().getList("name", String.class);
		assertThat(stationNames).containsAnyOf("강남역");
	}

	@DisplayName("지하철역의 목록 조회")
	@Test
	void getStations() {
		//given
		AcceptanceUtils.createStations(List.of("수유역", "강변역"));

		//when
		final List<String> resultStationNames =
			RestAssured.given()
				.log().all()
				.when().get("stations")
				.then().log().all()
				.statusCode(HttpStatus.OK.value())
				.extract()
				.jsonPath().getList("name", String.class);

		//then
		Assertions.assertEquals(2, resultStationNames.size());
	}

	@DisplayName("지하철역 삭제")
	@Test
	void deleteStation() {
		//given
		final long stationId = AcceptanceUtils.createStation("홍대입구역");

		//when
		RestAssured.given().log().all()
			.when().delete("stations/" + stationId)
			.then().log().all()
			.statusCode(HttpStatus.NO_CONTENT.value())
			.extract();

		//then
		final List<String> getStationsResponse = RestAssured.given()
			.log().all()
			.when().get("stations")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.jsonPath().getList("name", String.class);

		Assertions.assertEquals(0, getStationsResponse.size());
	}
}
