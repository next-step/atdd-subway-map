package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.station.StationAcceptanceTestRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

	final StationAcceptanceTestRequest stationAcceptanceTestRequest;
	final LineAcceptanceTestRequest lineAcceptanceTestRequest;
	final LineAcceptanceTestValidation lineAcceptanceTestValidation;

	public LineAcceptanceTest() {
		this.stationAcceptanceTestRequest = new StationAcceptanceTestRequest();
		this.lineAcceptanceTestRequest = new LineAcceptanceTestRequest();
		this.lineAcceptanceTestValidation = new LineAcceptanceTestValidation();
	}

	static Stream<Arguments> provideLineCreateParameterWithDownStationAndUpStation() {
		return Stream.of(
				Arguments.of(new LineAcceptanceTestParameter("9호선", "bg-red-600", "개화", "중앙보훈병원", 11)),
				Arguments.of(new LineAcceptanceTestParameter("신분당선", "bg-yellow-600", "강남", "광교", 17))
		);
	}

	static Stream<Arguments> provideTwoLineCreateParameters() {
		return Stream.of(
				Arguments.of(
						new LineAcceptanceTestParameter("9호선", "bg-red-600"),
						new LineAcceptanceTestParameter("신분당선", "bg-yellow-600")
				)
		);
	}

	static Stream<Arguments> provideLineParameters() {
		return Stream.of(
				Arguments.of(new LineAcceptanceTestParameter("9호선", "bg-red-600")),
				Arguments.of(new LineAcceptanceTestParameter("신분당선", "bg-yellow-600"))
		);
	}

	/**
	 * When 상행 종점역과 하행 종점역과 지하철 노선 생성을 요청 하면
	 * Then 지하철 노선 생성이 성공한다.
	 */
	@DisplayName("지하철 노선 생성")
	@MethodSource("provideLineCreateParameterWithDownStationAndUpStation")
	@ParameterizedTest
	void createLine(LineAcceptanceTestParameter 노선) {
		// given
		final long 상행_종점역_아이디 = stationAcceptanceTestRequest.역_생성요청_아이디_반환(노선.상행_종점역);
		final long 하행_종점역_아이디 = stationAcceptanceTestRequest.역_생성요청_아이디_반환(노선.하행_종점역);

		// when
		ExtractableResponse<Response> 생성_응답 = lineAcceptanceTestRequest.노선_생성_요청(노선, 상행_종점역_아이디, 하행_종점역_아이디);

		// then
		lineAcceptanceTestValidation.생성_요청_검증(생성_응답);
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * Given 새로운 지하철 노선 생성을 요청 하고
	 * When 지하철 노선 목록 조회를 요청 하면
	 * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
	 */
	@DisplayName("지하철 노선 목록 조회")
	@MethodSource("provideTwoLineCreateParameters")
	@ParameterizedTest
	void getLines(LineAcceptanceTestParameter 구호선, LineAcceptanceTestParameter 신분당선) {
		// given
		ExtractableResponse<Response> 생성_응답 = lineAcceptanceTestRequest.노선_생성_요청(구호선);

		lineAcceptanceTestRequest.노선_생성_요청(신분당선);

		// when
		ExtractableResponse<Response> 목록_조회_응답 = lineAcceptanceTestRequest.목록_조회_요청();

		// then
		lineAcceptanceTestValidation.노선_조회_요청_목록_검증(목록_조회_응답, 구호선.노선이름, 신분당선.노선이름);
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 생성한 지하철 노선 조회를 요청 하면
	 * Then 생성한 지하철 노선을 응답받는다
	 */
	@DisplayName("지하철 노선 조회")
	@MethodSource("provideTwoLineCreateParameters")
	@ParameterizedTest
	void getLine(LineAcceptanceTestParameter 구호선, LineAcceptanceTestParameter 신분당선) {
		// given
		ExtractableResponse<Response> 생성_응답 = lineAcceptanceTestRequest.노선_생성_요청(구호선);
		lineAcceptanceTestRequest.노선_생성_요청(신분당선);

		// when
		final ExtractableResponse<Response> 단건조회_응답 = lineAcceptanceTestRequest.단건_조회_요청(생성_응답);

		// then
		lineAcceptanceTestValidation.노선_조회_요청_단건_검증(단건조회_응답, 구호선.노선이름);
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 생성한 지하철 노선 삭제를 요청 하면
	 * Then 생성한 지하철 노선 삭제가 성공한다.
	 */
	@DisplayName("지하철 노선 삭제")
	@MethodSource("provideLineParameters")
	@ParameterizedTest
	void deleteLine(LineAcceptanceTestParameter 노선) {
		// given
		ExtractableResponse<Response> 생성_응답 = lineAcceptanceTestRequest.노선_생성_요청(노선);

		// when
		final ExtractableResponse<Response> 삭제_응답 = lineAcceptanceTestRequest.삭제_요청(생성_응답);

		// then
		lineAcceptanceTestValidation.삭제_요청_검증(삭제_응답);
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 지하철 노선의 정보 수정을 요청 하면
	 * Then 지하철 노선의 정보 수정은 성공한다.
	 */
	@DisplayName("지하철 노선 수정")
	@MethodSource("provideTwoLineCreateParameters")
	@ParameterizedTest
	void updateLine(LineAcceptanceTestParameter 노선, LineAcceptanceTestParameter 수정할_노선) {
		// given
		ExtractableResponse<Response> 생성_응답 = lineAcceptanceTestRequest.노선_생성_요청(노선);

		// when
		final ExtractableResponse<Response> 수정_응답 = lineAcceptanceTestRequest.노선_수정_요청(생성_응답, 수정할_노선);

		// then
		lineAcceptanceTestValidation.노선_수정_요청_검증(수정_응답, 수정할_노선);
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 같은 이름으로 지하철 노선 생성을 요청하면
	 * Then 지하철 노선 생성이 실패한다.
	 */
	@DisplayName("지하철 노선 중복이름 생성")
	@MethodSource("provideLineParameters")
	@ParameterizedTest
	void createDuplicateLine(LineAcceptanceTestParameter 노선) {
		// given
		ExtractableResponse<Response> 생성_응답 = lineAcceptanceTestRequest.노선_생성_요청(노선);

		// when
		ExtractableResponse<Response> 중복_생성_응답 = lineAcceptanceTestRequest.노선_생성_요청(노선);

		// then
		lineAcceptanceTestValidation.중복_생성_요청_실패_검증(중복_생성_응답);
	}

	/**
	 * Given 이름이 다른 두 지하철 노선 생성을 요청 하고
	 * When 하나의 지하철을 다른 지하철의 같은 이름으로 지하철 노선 변경을 요청하면
	 * Then 지하철 노선 생성이 실패한다.
	 */
	@DisplayName("지하철 노선 중복이름 변경")
	@MethodSource("provideTwoLineCreateParameters")
	@ParameterizedTest
	void updateDuplicateLine(LineAcceptanceTestParameter 노선, LineAcceptanceTestParameter 다른_노선) {
		// given
		ExtractableResponse<Response> 생성_응답 = lineAcceptanceTestRequest.노선_생성_요청(노선);
		ExtractableResponse<Response> 다른_노선_생성_응답 = lineAcceptanceTestRequest.노선_생성_요청(다른_노선);

		// when
		final ExtractableResponse<Response> 요청_응답 = lineAcceptanceTestRequest.노선_수정_요청(다른_노선_생성_응답, 노선);

		// then
		lineAcceptanceTestValidation.중복_생성_요청_실패_검증(요청_응답);
	}

}
