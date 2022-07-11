package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.StationAcceptanceStatic.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.acceptance_infra.AcceptanceTest;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

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

		ExtractableResponse<Response> response = 지하철_생성_요청(params);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> stationNames = 지하철역_이름_조회();
		assertThat(stationNames).containsAnyOf("강남역");
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	@DisplayName("지하철역을 조회한다.")
	@Test
	void getStations() {
		//given
		지하철_생성_요청(Map.of("name", "서현역"));
		지하철_생성_요청(Map.of("name", "이매역"));

		//when
		ExtractableResponse<Response> stationsResponse = 지하철_목록_조회_요청();

		//then
		List<String> stationNameList = 지하철역_이름_조회();

		assertThat(stationsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(stationNameList).containsAnyOf("서현역");
		assertThat(stationNameList).containsAnyOf("이매역");
	}

	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */
	@DisplayName("지하철역을 제거한다.")
	@Test
	void deleteStation() {
		//given
		Long stationId = 지하철_생성_요청(Map.of("name", "서현역")).jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> deleteResponse = 지하철역_삭제_요청(stationId);

		//then
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		List<String> stationNameList = 지하철역_이름_조회();
		assertThat(stationNameList).doesNotContain("서현역");
	}

}
