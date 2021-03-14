package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static nextstep.subway.station.StationSteps.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
	@DisplayName("지하철역을 생성한다.")
	@Test
	void createStation() {
		// when
		final ExtractableResponse<Response> response = 지하철역_생성_요청("강남역");

		// then
		지하철역_생성_요청됨(response);
	}

	@DisplayName("지하철역을 조회한다.")
	@Test
	void getStations() {
		/// given
		List<ExtractableResponse<Response>> stationResponses = new ArrayList();
		stationResponses.add(지하철역_생성_요청("강남역"));
		stationResponses.add(지하철역_생성_요청("역삼역"));

		// when
		final ExtractableResponse<Response> response = 지하철역_목록_조회_요청();

		// then
		지하철역_목록_응답됨(response);

		지하철역_목록_포함됨(response, stationResponses);
	}

	@DisplayName("지하철역을 제거한다.")
	@Test
	void deleteStation() {
		// given
		final ExtractableResponse<Response> response = 지하철역_생성_요청("강남역");

		// when
		final ExtractableResponse<Response> deleteResponse = 지하철역_삭제_요청(response);

		// then
		지하철역_삭제됨(deleteResponse);
	}
}
