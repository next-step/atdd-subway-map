package subway;

import static testhelper.StationRequestExecutor.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
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
		ExtractableResponse<Response> request = executeCreateStationRequest("강남역");

		// then
		assertThat(request.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		ExtractableResponse<Response> response = executeGetStationRequest();
		List<String> stationNames = parseSubwayNames(response);
		assertThat(stationNames).containsAnyOf("강남역");
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	@DisplayName("지하철역 목록을 조회한다.")
	@Test
	void fetchStation() {
		// given
		executeCreateStationRequest("신논현역");
		executeCreateStationRequest("서초역");

		// when
		ExtractableResponse<Response> response = executeGetStationRequest();
		List<String> stationNames = parseSubwayNames(response);
		// then
		assertThat(stationNames).containsAnyOf("신논현역", "서초역");
	}

	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */
	@DisplayName("지하철역을 삭제한다.")
	@Test
	void deleteStation() {
		// given
		ExtractableResponse<Response> createResponse = executeCreateStationRequest("신사역");
		long stationId = createResponse.jsonPath().getLong("id");

		// when
		executeDeleteStationRequest(stationId);

		// then
		ExtractableResponse<Response> response = executeGetStationRequest();
		List<String> stationNames = parseSubwayNames(response);
		assertThat(stationNames).doesNotContain("신사역");
	}

	public List<String> parseSubwayNames(ExtractableResponse<Response> response){
		return response.jsonPath().getList("name", String.class);
	}
}