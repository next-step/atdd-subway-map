package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@Sql(scripts = "classpath:sql/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
	/**
	 * When 지하철역을 생성하면
	 * Then 지하철역이 생성된다
	 * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
	 */
	@DisplayName("지하철역을 생성한다.")
	@Test
	void createStation() {
		// when
		String stationName = "강남역";
		createStation(stationName);

		// then
		assertThat(showStation().jsonPath().getList("name", String.class)).containsAnyOf(stationName);
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	// TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
	@DisplayName("지하철역 목록 조회")
	@Test
	void showStations() {
		// given
		String stationName1 = "강남역";
		String stationName2 = "교대역";

		createStation(stationName1);
		createStation(stationName2);

		// when
		// then
		assertThat(showStation().jsonPath().getList("name", String.class).size()).isEqualTo(2);
		assertThat(showStation().jsonPath().getList("name", String.class)).containsAnyOf(stationName1, stationName2);
	}

	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */
	// TODO: 지하철역 제거 인수 테스트 메서드 생성
	@DisplayName("지하철역 제거")
	@Test
	void deleteStation() {
		// given
		String stationName = "강남역";
		long id = createStation(stationName).jsonPath().getLong("id");

		// when
		deleteStationById(id);

		// then
		assertThat(showStation().jsonPath().getList("name", String.class)).doesNotContain(stationName);
	}

	private ExtractableResponse<Response> createStation(String stationName) {
		Map<String, String> params = new HashMap<>();
		params.put("name", stationName);

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/stations")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return response;
	}

	private ExtractableResponse<Response> showStation() {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().get("/stations")
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		return response;
	}

	private ExtractableResponse<Response> deleteStationById(Long id) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().delete("/stations/{id}", id)
			.then().log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		return response;
	}
}
