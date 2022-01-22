package nextstep.subway.acceptance;

import static java.util.Arrays.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.RequestBodyBuilder;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

	static final String 요청_주소 = "/lines";
	static final String 노선이름_필드 = "name";
	static final String 노선색상_필드 = "color";
	static final String 노선_아이디_필드 = "id";

	LineAcceptanceTest() {
		super(요청_주소, 노선_아이디_필드);
	}

	/**
	 * When 지하철 노선 생성을 요청 하면
	 * Then 지하철 노선 생성이 성공한다.
	 */
	@DisplayName("지하철 노선 생성")
	@CsvSource({"9호선, bg-red-600", "신분당선, bg-yellow-600"})
	@ParameterizedTest
	void createLine(String 노선이름, String 노선색상) {
		// given
		final Map<String, String> 요청_본문 = 노선_요청_본문_생성(노선이름, 노선색상);

		// when
		ExtractableResponse<Response> 생성_응답 = 생성_요청(요청_본문);

		// then
		생성_요청_검증(생성_응답);
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * Given 새로운 지하철 노선 생성을 요청 하고
	 * When 지하철 노선 목록 조회를 요청 하면
	 * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
	 */
	@DisplayName("지하철 노선 목록 조회")
	@CsvSource({"9호선, bg-red-600, 신분당선, bg-yellow-600"})
	@ParameterizedTest
	void getLines(String 구호선, String 구호선_노선색상, String 신분당선, String 신분당선_노선색상) {
		// given
		생성_요청(노선_요청_본문_생성(구호선, 구호선_노선색상));
		생성_요청(노선_요청_본문_생성(신분당선, 신분당선_노선색상));

		// when
		ExtractableResponse<Response> 목록_조회_응답 = 목록_조회_요청();

		// then
		조회_요청_목록_검증(목록_조회_응답, 노선이름_필드, asList(구호선, 신분당선));
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 생성한 지하철 노선 조회를 요청 하면
	 * Then 생성한 지하철 노선을 응답받는다
	 */
	@DisplayName("지하철 노선 조회")
	@CsvSource({"9호선, bg-red-600, 신분당선, bg-yellow-600"})
	@ParameterizedTest
	void getLine(String 구호선, String 구호선_노선색상, String 신분당선, String 신분당선_노선색상) {
		// given
		final Map<String, String> 구호선_요청_본문 = 노선_요청_본문_생성(구호선, 구호선_노선색상);
		final Map<String, String> 신분당선_요청_본문 = 노선_요청_본문_생성(신분당선, 신분당선_노선색상);

		final long 구호선_아이디 = 아이디_추출(생성_요청(구호선_요청_본문));
		생성_요청(신분당선_요청_본문);

		// when
		final ExtractableResponse<Response> 단건조회_응답 = 단건_조회_요청(구호선_아이디);

		// then
		조회_요청_단건_검증(단건조회_응답, 노선이름_필드, 구호선);
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 지하철 노선의 정보 수정을 요청 하면
	 * Then 지하철 노선의 정보 수정은 성공한다.
	 */
	@DisplayName("지하철 노선 수정")
	@CsvSource({"9호선, bg-red-600, 신분당선, bg-yellow-600"})
	@ParameterizedTest
	void updateLine(String 노선이름, String 노선색상, String 수정된_노선이름, String 수정된_노선색상) {
		// given
		final Map<String, String> 요청_본문 = 노선_요청_본문_생성(노선이름, 노선색상);
		final long 노선_아이디 = 아이디_추출(생성_요청(요청_본문));

		final Map<String, String> 수정된_요청_본문 = 노선_요청_본문_생성(수정된_노선이름, 수정된_노선색상);
		// when
		final ExtractableResponse<Response> 수정_응답 = 수정_요청(노선_아이디, 수정된_요청_본문);

		// then
		수정_요청_검증(수정_응답, 노선이름_필드, 수정된_노선이름);
		수정_요청_검증(수정_응답, 노선색상_필드, 수정된_노선색상);
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 생성한 지하철 노선 삭제를 요청 하면
	 * Then 생성한 지하철 노선 삭제가 성공한다.
	 */
	@DisplayName("지하철 노선 삭제")
	@CsvSource({"9호선, bg-red-600"})
	@ParameterizedTest
	void deleteLine(String 노선이름, String 노선색상) {
		// given
		final Map<String, String> 요청_본문 = 노선_요청_본문_생성(노선이름, 노선색상);
		final long 노선_아이디 = 아이디_추출(생성_요청(요청_본문));

		// when
		final ExtractableResponse<Response> 삭제_응답 = 삭제_요청(노선_아이디);

		// then
		삭제_요청_검증(삭제_응답);
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 같은 이름으로 지하철 노선 생성을 요청하면
	 * Then 지하철 노선 생성이 실패한다.
	 */
	@DisplayName("지하철 노선 중복 생성")
	@ParameterizedTest
	@CsvSource({"9호선, bg-yellow-600", "신분당선, bg-green-400", "김포골드라인, bg-gold-100"})
	void createDuplicateLine(String 노선이름, String 노선색상) {
		// given
		final Map<String, String> 요청_본문 = 노선_요청_본문_생성(노선이름, 노선색상);
		생성_요청(요청_본문);

		// when
		final ExtractableResponse<Response> 요청_응답 = 생성_요청(요청_본문);

		// then
		중복_생성_요청_실패_검증(요청_응답);
	}

	Map<String, String> 노선_요청_본문_생성(String 노선이름, String 노선색상) {
		return RequestBodyBuilder.builder()
			.put(노선이름_필드, 노선이름)
			.put(노선색상_필드, 노선색상)
			.build();
	}

}
