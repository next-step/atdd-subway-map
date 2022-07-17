package nextstep.subway.acceptance.station;

import static nextstep.subway.acceptance.station.StationProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.common.AcceptanceTestBase;

@DisplayName("지하철역 인수테스트")
class StationAcceptanceTest extends AcceptanceTestBase {

	private static final String 강남역 = "강남역";
	private static final String 역삼역 = "역삼역";

	/**
	 * When 지하철역을 생성하면
	 * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
	 */
	@DisplayName("지하철역을 생성한다.")
	@Test
	void createStation() {
		// When 지하철역을 생성하면
		지하철역_생성_성공(강남역);

		// Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
		assertThat(지하철역_이름_목록_조회()).containsAnyOf(강남역);
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	@DisplayName("지하철역 목록을 조회한다.")
	@Test
	void getStations() {

		// Given 2개의 지하철역을 생성하고
		지하철역_생성_성공(강남역);
		지하철역_생성_성공(역삼역);

		// When 지하철역 목록을 조회하면
		List<String> 지하철역_이름_목록 = 지하철역_이름_목록_조회();

		// Then 2개의 지하철역을 응답 받는다
		assertAll(
			() -> assertThat(지하철역_이름_목록).hasSize(2),
			() -> assertThat(지하철역_이름_목록).containsAnyOf(강남역),
			() -> assertThat(지하철역_이름_목록).containsAnyOf(역삼역)
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
		ExtractableResponse<Response> response = 지하철역_생성_성공(강남역);

		// When 그 지하철역을 삭제하면
		지하철역_제거_성공(지하철역_Id_추출(response));

		// Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
		assertThat(지하철역_이름_목록_조회()).isEmpty();
	}

	private List<String> 지하철역_이름_목록_조회() {
		return 지하철역_목록_조회_성공()
			.jsonPath()
			.getList("name", String.class);
	}
}