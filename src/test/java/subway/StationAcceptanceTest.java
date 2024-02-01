package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.api.StationApi.createStation;
import static subway.api.StationApi.deleteStation;
import static subway.api.StationApi.getStations;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.fixture.StationFixture;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
	@DisplayName("지하철역을 생성한다.")
	@Test
	void create_station() {
		// when
		ExtractableResponse<Response> response = createStation(StationFixture.강남역_생성_요청());

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
		createStation(StationFixture.강남역_생성_요청());
		createStation(StationFixture.압구정역_생성_요청());

		// when
		List<String> stationNames = getStations();

		// then
		assertThat(stationNames).containsAnyOf(StationFixture.강남역_생성_요청().getName());
		assertThat(stationNames).containsAnyOf(StationFixture.압구정역_생성_요청().getName());
	}

	@DisplayName("지하철 역을 삭제한다.")
	@Test
	void delete_station() {
		// given
		ExtractableResponse<Response> response = createStation(StationFixture.강남역_생성_요청());
		long stationId = response.body().jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> deleteResponse = deleteStation(stationId);

		// then
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
