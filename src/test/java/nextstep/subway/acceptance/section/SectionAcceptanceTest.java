package nextstep.subway.acceptance.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.line.LineAcceptanceTestParameter;
import nextstep.subway.acceptance.line.LineAcceptanceTestRequest;
import nextstep.subway.acceptance.station.StationAcceptanceTestRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {

	final SectionAcceptanceTestRequest sectionAcceptanceTestRequest;
	final SectionAcceptanceTestValidation sectionAcceptanceTestValidation;

	final LineAcceptanceTestRequest lineAcceptanceTestRequest;
	final StationAcceptanceTestRequest stationAcceptanceTestRequest;

	SectionAcceptanceTest() {
		this.sectionAcceptanceTestRequest = new SectionAcceptanceTestRequest();
		this.sectionAcceptanceTestValidation = new SectionAcceptanceTestValidation();
		this.lineAcceptanceTestRequest = new LineAcceptanceTestRequest();
		this.stationAcceptanceTestRequest = new StationAcceptanceTestRequest();
	}

	static Stream<Arguments> provideSectionParameters() {
		return Stream.of(
				Arguments.of(
						new LineAcceptanceTestParameter("신분당선", "bg-red-600", "강남역", "양재역", 300),
						new SectionAcceptanceTestParameter("양재역", "판교역", 10)
				)
		);
	}

	/**
	 * Given 등록하려는 구간의 상행역이 현재 등록된 노선의 하행 종점역일 때
	 * Given 등록하려는 구간의 하행역이 노선에 등록되지 않은 역일 때
	 * When 지하철 노선에 지하철 구간 등록을 요청하면
	 * Then 지하철 노선에 구간 등록이 성공한다
	 */
	@DisplayName("지하철역 구간 생성")
	@MethodSource("provideSectionParameters")
	@ParameterizedTest
	void createSection(LineAcceptanceTestParameter 노선, SectionAcceptanceTestParameter 구간) {
		// given
		final long 상행역_종점_아이디 = stationAcceptanceTestRequest.역_생성요청_아이디_반환(노선.상행_종점역);
		final long 하행역_종점_아이디 = stationAcceptanceTestRequest.역_생성요청_아이디_반환(노선.하행_종점역);
		final long 노선_아이디 = lineAcceptanceTestRequest.노선_생성_요청_아이디_반환(노선, 상행역_종점_아이디, 하행역_종점_아이디);

		final long 구간_하행역_아이디 = stationAcceptanceTestRequest.역_생성요청_아이디_반환(구간.하행역);

		// when
		final ExtractableResponse<Response> 구간_생성_응답 = sectionAcceptanceTestRequest.구간_생성_요청(노선_아이디, 구간, 하행역_종점_아이디, 구간_하행역_아이디);

		// then
		sectionAcceptanceTestValidation.구간_생성_요청_검증(구간_생성_응답, 구간, 하행역_종점_아이디, 구간_하행역_아이디);
	}

	/**
	 * Given 지하철 노선에 등록된 마지막 역(하행 종점역) 이며
	 * Given 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우 (구간이 1개인 경우) 가 아닐 때
	 * When 지하철 노선에 지하철 구간 삭제를 요청하면
	 * Then 지하철 노선에 구간 삭제가 성공한다
	 */
	@DisplayName("지하철역 구간 제거")
	@MethodSource("provideSectionParameters")
	@ParameterizedTest
	void removeSection(LineAcceptanceTestParameter 노선, SectionAcceptanceTestParameter 구간) {
		// given
		final long 상행역_종점_아이디 = stationAcceptanceTestRequest.역_생성요청_아이디_반환(노선.상행_종점역);
		final long 하행역_종점_아이디 = stationAcceptanceTestRequest.역_생성요청_아이디_반환(노선.하행_종점역);
		final long 노선_아이디 = lineAcceptanceTestRequest.노선_생성_요청_아이디_반환(노선, 상행역_종점_아이디, 하행역_종점_아이디);
		final long 구간_하행역_아이디 = stationAcceptanceTestRequest.역_생성요청_아이디_반환(구간.하행역);
		sectionAcceptanceTestRequest.구간_생성_요청_아이디_반환(노선_아이디, 구간, 하행역_종점_아이디, 구간_하행역_아이디);

		// when
		final ExtractableResponse<Response> 삭제_응답 = sectionAcceptanceTestRequest.구간_삭제_요청(노선_아이디, 구간_하행역_아이디);

		// then
		sectionAcceptanceTestValidation.삭제_요청_검증(삭제_응답);
	}

}
