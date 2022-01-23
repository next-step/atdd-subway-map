package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.RequestBodyBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static java.util.Arrays.asList;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

	static final String 요청_주소 = "/stations";
	static final String 역이름_필드 = "name";
	static final String 역아이디_필드 = "id";

	StationAcceptanceTest() {
		super(요청_주소, 역아이디_필드);
	}

	/**
	 * When 지하철역 생성을 요청 하면
	 * Then 지하철역 생성이 성공한다.
	 */
	@DisplayName("지하철역 생성")
	@CsvSource({"강남역", "기양역", "분당역"})
	@ParameterizedTest
	void createStation(String 역_이름) {
		// given
		Map<String, String> 역_생성_요청_본문 = 역_요청_본문_생성(역_이름);

		// when
		ExtractableResponse<Response> 생성_응답 = 생성_요청(역_생성_요청_본문);

		// then
		생성_요청_검증(생성_응답);
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
		Map<String, String> 역_생성_요청_본문 = 역_요청_본문_생성(역_이름);
		생성_요청(역_생성_요청_본문);

		Map<String, String> 새_역_생성_요청_본문 = 역_요청_본문_생성(새로운_역_이름);
		생성_요청(새_역_생성_요청_본문);

		// when
		ExtractableResponse<Response> 목록_조회_응답 = 목록_조회_요청();

		조회_요청_목록_검증(목록_조회_응답, 역이름_필드, asList(역_이름, 새로운_역_이름));
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
		final Map<String, String> 역_요청_본문 = 역_요청_본문_생성(역_이름);
		ExtractableResponse<Response> 생성_응답 = 생성_요청(역_요청_본문);

		// when
		final ExtractableResponse<Response> 삭제_응답 = 삭제_요청(아이디_추출(생성_응답));

		// then
		삭제_요청_검증(삭제_응답);
	}

	/**
	 * Given 지하철역 생성을 요청하고
	 * When 같은 이름으로 지하역 생성을 요청하면
	 * Then 지하철 생성이 실패한다.
	 */
	@DisplayName("지하철 역 중복 생성")
	@CsvSource({"강남역", "판교역", "가양역"})
	@ParameterizedTest
	public void createDuplicateStation(String 역_이름) {
		// given
		final Map<String, String> 요청_본문 = 역_요청_본문_생성(역_이름);
		생성_요청(요청_본문);

		// when
		final ExtractableResponse<Response> 중복_생성_응답 = 생성_요청(요청_본문);

		// then
		중복_생성_요청_실패_검증(중복_생성_응답);
	}

	Map<String, String> 역_요청_본문_생성(String stationName) {
		return RequestBodyBuilder.builder()
				.put(역이름_필드, stationName)
				.build();
	}

}
