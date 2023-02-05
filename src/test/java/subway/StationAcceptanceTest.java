package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.api.StationTestApi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@Sql(scripts = "classpath:sql/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

	StationTestApi stationApi = new StationTestApi();
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
		stationApi.createStation(stationName);

		// then
		ExtractableResponse<Response> showResponse = stationApi.showStation();
		assertThat(showResponse.jsonPath().getList("name", String.class)).containsAnyOf(stationName);
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

		stationApi.createStation(stationName1);
		stationApi.createStation(stationName2);

		// when
		ExtractableResponse<Response> showResponse = stationApi.showStation();

		// then
		assertThat(showResponse.jsonPath().getList("name", String.class).size()).isEqualTo(2);
		assertThat(showResponse.jsonPath().getList("name", String.class)).containsAnyOf(stationName1, stationName2);
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
		long id = stationApi.createStation(stationName).jsonPath().getLong("id");

		// when
		stationApi.deleteStationById(id);

		// then
		ExtractableResponse<Response> showResponse = stationApi.showStation();
		assertThat(showResponse.jsonPath().getList("name", String.class)).doesNotContain(stationName);
	}

}
