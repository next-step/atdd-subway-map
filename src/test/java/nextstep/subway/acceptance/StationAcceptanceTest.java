package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
	@LocalServerPort
	int port;

	private static final String STATION_NAME = "강남역";

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

	/**
	 * When 지하철역을 생성하면
	 * Then 지하철역이 생성된다
	 * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
	 */
	@DisplayName("지하철역을 생성한다.")
	@Test
	void createStation() {
		// when
		ExtractableResponse<Response> response = 지하철역_생성(STATION_NAME);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> stationNames = 지하철역_목록_조회();
		assertThat(stationNames).containsAnyOf(STATION_NAME);
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	@DisplayName("지하철역을 조회한다.")
	@Test
	void getStations() {
		// given
		지하철역_생성(STATION_NAME);
		지하철역_생성("역삼역");

		// when
		List<String> stationNames = 지하철역_목록_조회();

		// then
		assertThat(stationNames).hasSize(2);
	}

	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */
	@DisplayName("지하철역을 제거한다.")
	@Test
	void deleteStation() {
		// given
		ExtractableResponse<Response> responseOfCreate = 지하철역_생성(STATION_NAME);
		Long id = responseOfCreate.jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> responseOfDelete = 지하철역_삭제(id);
		assertThat(responseOfDelete.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		// then
		List<String> stationNames = 지하철역_목록_조회();
		assertThat(stationNames).doesNotContain(STATION_NAME);
	}

	private ExtractableResponse<Response> 지하철역_생성(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);

		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/stations")
			.then().log().all()
			.extract();
	}

	private List<String> 지하철역_목록_조회() {
		return RestAssured.given().log().all()
			.when().get("/stations")
			.then().log().all()
			.extract().jsonPath().getList("name", String.class);
	}

	private ExtractableResponse<Response> 지하철역_삭제(Long id) {
		return RestAssured.given().log().all()
			.when().delete("/stations/" + id)
			.then().log().all()
			.extract();
	}

}