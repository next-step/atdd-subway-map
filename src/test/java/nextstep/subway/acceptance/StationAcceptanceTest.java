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
		// given
		String stationName = "강남역";
		Map<String, String> params = new HashMap<>();
		params.put("name", stationName);

		// when
		ExtractableResponse<Response> response = create(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> stationNames = getAll().jsonPath().getList("name", String.class);
		assertThat(stationNames).containsAnyOf(stationName);
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
		String stationName1 = "한티역";
		Map<String, String> params1 = new HashMap<>();
		params1.put("name", stationName1);
		create(params1);

		String stationName2 = "선릉역";
		Map<String, String> params2 = new HashMap<>();
		params2.put("name", stationName2);
		create(params2);

		// when
		ExtractableResponse<Response> response = getAll();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		List<String> stationNames = response.jsonPath().getList("name", String.class);
		assertThat(stationNames).size().isEqualTo(2);
		assertThat(stationNames).containsOnlyOnce(stationName1, stationName2);
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
		String stationName = "역삼역";
		Map<String, String> params = new HashMap<>();
		params.put("name", stationName);
		long stationId = create(params).jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> response = delete(stationId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		// then
		ExtractableResponse<Response> allStationsResponse = getAll();
		List<String> allStationNames = allStationsResponse.jsonPath().getList("name", String.class);
		List<Long> allStationIds = allStationsResponse.jsonPath().getList("id", Long.class);

		assertThat(allStationNames).doesNotContain(stationName);
		assertThat(allStationIds).doesNotContain(stationId);
	}


	private ExtractableResponse<Response> create(Map<String, String> params) {
		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/stations")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> getAll() {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/stations")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> delete(long stationId) {
		return RestAssured.given().log().all()
			.pathParam("id", stationId)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/stations/{id}")
			.then().log().all()
			.extract();
	}
}
