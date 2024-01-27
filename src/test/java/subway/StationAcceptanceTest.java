package subway;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

	@LocalServerPort
	int port;

	@BeforeEach
	void init() {
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
		Map<String, String> params = new HashMap<>();
		params.put("name", "강남역");

		ExtractableResponse<Response> response = Endpoint.createStation(params);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> stationNames = Endpoint.showStations().jsonPath().getList("name", String.class);
		assertThat(stationNames).containsAnyOf("강남역");
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	@DisplayName("지하철역 목록을 조회한다.")
	@Test
	void showStations() {
		// given
		// 생성할 2개의 지하철역 이름을 작성한다.
		List<String> givenStationNames = Arrays.asList("지하철역1", "지하철역2");
		// 지하철역을 생성하기 위한 params 를 생성한다.
		List<Map<String, String>> params = new ArrayList<>();
		givenStationNames.forEach(name -> {
			Map<String, String> param = new HashMap<>();
			param.put("name", name);
			params.add(param);
		});
		// 2개의 지하철역을 생성한다.
		params.forEach(param -> Endpoint.createStation(param));

		// when
		// 지하철역 목록을 조회한다.
		ExtractableResponse<Response> response = Endpoint.showStations();

		// then
		// 조회 응답 코드를 검증한다.
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		// then
		// 생성한 지하철역이 조회되는지 검증한다.
		List<String> stationNames = response.jsonPath().getList("name", String.class);
		assertThat(stationNames).containsAll(givenStationNames);
	}

	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */
	@DisplayName("지하철역을 삭제한다")
	@Test
	void deleteStation() {
		// given
		// 생성할 지하철역 이름을 작성한다.
		String givenStationName = "지하철역1";
		// 지하철역을 생성하기 위한 param 을 생성한다.
		Map<String, String> param = new HashMap<>();
		param.put("name", givenStationName);
		// 지하철역을 생성한 후 id를 가져온다.
		Long stationId = Endpoint.createStation(param).jsonPath().getLong("id");

		// when
		// 지하철역을 삭제한다.
		ExtractableResponse<Response> deleteResponse = Endpoint.deleteStation(stationId);

		// then
		// 지하철역 삭제 응답 코드를 검증한다.
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		// then
		// 삭제한 지하철역이 조회 목록에 나타나지 않는지 확인한다.
		ExtractableResponse<Response> showResponse = Endpoint.showStations();
		List<String> stationNames = showResponse.jsonPath().getList("name", String.class);
		assertThat(stationNames).isNotIn(givenStationName);
	}
}
