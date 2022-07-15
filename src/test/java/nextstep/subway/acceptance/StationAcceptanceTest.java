package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.isolation.TestIsolationUtil;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationAcceptanceTest {

	private static final int CREATED = HttpStatus.CREATED.value();

	@LocalServerPort
	int port;

	@Autowired
	TestIsolationUtil testIsolationUtil;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		testIsolationUtil.clean();
	}

	/**
	 * When 지하철역을 생성하면
	 * Then 지하철역이 생성된다
	 * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
	 */
	@DisplayName("지하철역을 생성한다.")
	@Test
	void createStation() {
		// When 지하철역을 생성하면
		String stationName = "강남역";
		ExtractableResponse<Response> response = createStation(stationName);

		// Then 지하철역이 생성된다
		assertThat(response.statusCode()).isEqualTo(CREATED);

		// Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
		List<String> allStationNames = getAllStationNames();
		assertThat(allStationNames).containsAnyOf(stationName);
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	@DisplayName("지하철역을 조회한다.")
	@Test
	void getStations() {

		// Given 2개의 지하철역을 생성하고
		String stationFirst = "강남역";
		String stationSecond = "역삼역";
		createStation(stationFirst);
		createStation(stationSecond);

		// When 지하철역 목록을 조회하면
		List<String> allStationNames = getAllStationNames();

		// Then 2개의 지하철역을 응답 받는다
		assertAll(
			() -> assertThat(allStationNames).hasSize(2),
			() -> assertThat(allStationNames).containsAnyOf(stationFirst),
			() -> assertThat(allStationNames).containsAnyOf(stationSecond)
		);

	}

	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */
	@DisplayName("지하철역을 제거한다.")
	@Test
	void deleteStation() {
		// Given 지하철역을 생성하고
		String stationName = "강남역";
		ExtractableResponse<Response> createResponse = createStation(stationName);

		// When 그 지하철역을 삭제하면
		deleteStation(createResponse.jsonPath().getString("id"));

		// Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
		List<String> allStationNames = getAllStationNames();
		assertThat(allStationNames).isEmpty();
	}

	private ExtractableResponse<Response> createStation(String stationName) {
		Map<String, String> params = new HashMap<>();
		params.put("name", stationName);

		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/stations")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> getAllStations() {
		return RestAssured.given().log().all()
			.when().get("/stations")
			.then().log().all()
			.extract();
	}

	private List<String> getAllStationNames() {
		return getAllStations()
			.jsonPath()
			.getList("name", String.class);
	}

	private void deleteStation(String id) {
		RestAssured.given().log().all()
			.when().delete("/stations/" + id)
			.then().log().all()
			.extract();
	}

}