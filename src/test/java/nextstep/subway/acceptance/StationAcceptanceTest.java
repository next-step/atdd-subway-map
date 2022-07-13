package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

	/**
	 * When 지하철역을 생성하면
	 * Then 지하철역이 생성된다
	 * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
	 */
	@DisplayName("지하철역을 생성한다.")
	@Test
	void createStation() {
		// when
		Long stationId = 지하철역_생성(SIN_BOONDANG_LINE);

		// then
		assertThat(stationId).isNotNull();

		// then
		List<String> stations = 지하철역_조회();
		assertThat(stations).containsAnyOf(SIN_BOONDANG_LINE);
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	@DisplayName("지하철역을 조회한다.")
	@Test
	void getStations() {
		//Given
		지하철역_생성(SAMSUNG_STATION);
		지하철역_생성(SIN_BOONDANG_LINE);

		//When
		List<String> stations = 지하철역_조회();

		//then
		assertThat(stations).hasSize(2)
			.containsAnyOf(SAMSUNG_STATION)
			.containsAnyOf(SIN_BOONDANG_LINE);

	}

	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */
	@DisplayName("지하철역을 제거한다.")
	@Test
	void deleteStation() {
		//Given
		long stationId = 지하철역_생성(SAMSUNG_STATION);

		//when
		지하철역_삭제(stationId);

		//then
		List<String> stations = 지하철역_조회();
		assertThat(stations).isEmpty();
	}

	private Long 지하철역_생성(String stationName) {

		Map<String, String> searchParameter = new HashMap<>();
		searchParameter.put("name", stationName);

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(searchParameter)
			.when().post("/stations")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract()
			.jsonPath().getLong("id");

	}

	private List<String> 지하철역_조회() {
		return RestAssured.given().log().all()
			.when().get("/stations")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath().getList("name", String.class);
	}

	private void 지하철역_삭제(long stationId) {
		RestAssured.given().log().all()
			.when().delete("/stations/" + stationId)
			.then().log().all()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
}