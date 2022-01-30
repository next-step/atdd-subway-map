package nextstep.subway.acceptance.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

	final StationAcceptanceTestValidation stationAcceptanceTestValidation;
	final StationAcceptanceTestRequest stationAcceptanceTestRequest;

	StationAcceptanceTest() {
		this.stationAcceptanceTestValidation = new StationAcceptanceTestValidation();
		this.stationAcceptanceTestRequest = new StationAcceptanceTestRequest();
	}

	/**
	 * When 지하철역 생성을 요청 하면
	 * Then 지하철역 생성이 성공한다.
	 */
	@DisplayName("지하철역 생성")
	@CsvSource({"강남역", "기양역", "분당역"})
	@ParameterizedTest
	void createStation(String 역_이름) {
		// when
		ExtractableResponse<Response> 생성_응답 = stationAcceptanceTestRequest.역_생성요청(역_이름);

		// then
		stationAcceptanceTestValidation.생성_요청_검증(생성_응답);
	}

	/**
	 * Given 지하철역 생성을 요청 하고
	 * Given 새로운 지하철역 생성을 요청 하고
	 * When 지하철역 목록 조회를 요청 하면
	 * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
	 */
	@DisplayName("지하철역 목록 조회")
	@CsvSource({"강남역, 역삼역"})
	@ParameterizedTest
	void getStations(String 역_이름, String 새로운_역_이름) {
		/// given
		stationAcceptanceTestRequest.역_생성요청(역_이름);
		stationAcceptanceTestRequest.역_생성요청(새로운_역_이름);

		// when
		ExtractableResponse<Response> 목록_조회_응답 = stationAcceptanceTestRequest.목록_조회_요청();

		stationAcceptanceTestValidation.역_조회_요청_목록_검증(목록_조회_응답, 역_이름, 새로운_역_이름);
	}

	/**
	 * Given 지하철역 생성을 요청 하고
	 * When 생성한 지하철역 삭제를 요청 하면
	 * Then 생성한 지하철역 삭제가 성공한다.
	 */
	@DisplayName("지하철역 삭제")
	@CsvSource({"강남역", "역삼역"})
	@ParameterizedTest
	void deleteStation(String 역_이름) {
		// given
		final ExtractableResponse<Response> 역_생성_응답 = stationAcceptanceTestRequest.역_생성요청(역_이름);

		// when
		final ExtractableResponse<Response> 삭제_응답 = stationAcceptanceTestRequest.삭제_요청(역_생성_응답);

		// then
		stationAcceptanceTestValidation.삭제_요청_검증(삭제_응답);
	}

	/**
	 * Given 지하철역 생성을 요청하고
	 * When 같은 이름으로 지하역 생성을 요청하면
	 * Then 지하철 생성이 실패한다.
	 */
	@DisplayName("지하철 역 중복 생성")
	@CsvSource({"강남역", "판교역", "가양역"})
	@ParameterizedTest
	void createDuplicateStation(String 역_이름) {
		// given
		final ExtractableResponse<Response> 역_생성_응답 = stationAcceptanceTestRequest.역_생성요청(역_이름);

		// when
		final ExtractableResponse<Response> 중복_생성_응답 = stationAcceptanceTestRequest.역_생성요청(역_이름);

		// then
		stationAcceptanceTestValidation.중복_생성_요청_실패_검증(중복_생성_응답);
	}


}
