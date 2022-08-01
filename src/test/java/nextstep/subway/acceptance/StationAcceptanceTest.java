package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

	private final String STATION_GANGNAM = "강남역";
	private final String STATION_HANTI = "한티역";

    @LocalServerPort
    int port;

	@Autowired
	private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
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
		ExtractableResponse<Response> response = callCreateStation(makeCreateStationParams(STATION_GANGNAM));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> stationNames = callGetStations().jsonPath().getList("name", String.class);
		assertThat(stationNames).containsAnyOf(STATION_GANGNAM);
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
		callCreateStation(makeCreateStationParams(STATION_GANGNAM));
		callCreateStation(makeCreateStationParams(STATION_HANTI));

		// when
		ExtractableResponse<Response> response = callGetStations();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		List<String> stationNames = response.jsonPath().getList("name", String.class);
		assertThat(stationNames).hasSize(2);
		assertThat(stationNames).containsOnlyOnce(STATION_GANGNAM, STATION_HANTI);
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
		long stationId = callCreateStation(makeCreateStationParams(STATION_GANGNAM))
			.jsonPath()
			.getLong("id");

		// when
		ExtractableResponse<Response> response = callDeleteStation(stationId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		// then
		ExtractableResponse<Response> allStationsResponse = callGetStations();
		List<String> allStationNames = allStationsResponse.jsonPath().getList("name", String.class);
		List<Long> allStationIds = allStationsResponse.jsonPath().getList("id", Long.class);

		assertThat(allStationNames).doesNotContain(STATION_GANGNAM);
		assertThat(allStationIds).doesNotContain(stationId);
	}

	private Map<String, String> makeCreateStationParams(String stationName) {
		Map<String, String> params = new HashMap<>();
		params.put("name", stationName);
		return params;
	}

	public ExtractableResponse<Response> callCreateStation(String stationName) {
		return callCreateStation(makeCreateStationParams(stationName));
	}

	private ExtractableResponse<Response> callCreateStation(Map<String, String> params) {
		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/stations")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> callGetStations() {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/stations")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> callDeleteStation(long stationId) {
		return RestAssured.given().log().all()
			.pathParam("id", stationId)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/stations/{id}")
			.then().log().all()
			.extract();
	}
}
