package nextstep.subway.acceptance.section;

import static nextstep.subway.acceptance.line.LineProvider.*;
import static nextstep.subway.acceptance.section.SectionProvider.*;
import static nextstep.subway.acceptance.station.StationProvider.*;
import static nextstep.subway.section.SectionErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.common.AcceptanceTestBase;
import nextstep.subway.section.SectionErrorCode;

@DisplayName("구간 관리 인수테스트")
class SectionAcceptanceTest extends AcceptanceTestBase {

	private static final String 신분당선 = "신분당선";
	private static final String 신분당선_COLOR = "신분당선_COLOR";

	private static final String 처음_상행_종점역 = "처음_상행_종점역";
	private static final String 처음_하행_종점역 = "처음_하행_종점역";
	private static final String 새로운_하행_종점역 = "새로운_하행_종점역";

	String 처음_상행_종점역_ID;
	String 처음_하행_종점역_ID;
	String 새로운_하행_종점역_ID;

	String 지하철_노선_Id;

	@BeforeEach
	void initData() {
		// Given 1개의 지하철 노선을 생성하고
		처음_상행_종점역_ID = 지하철역_Id_추출(지하철역_생성_성공(처음_상행_종점역));
		처음_하행_종점역_ID = 지하철역_Id_추출(지하철역_생성_성공(처음_하행_종점역));
		새로운_하행_종점역_ID = 지하철역_Id_추출(지하철역_생성_성공(새로운_하행_종점역));

		지하철_노선_Id = 지하철_노선_Id_추출(지하철_노선_생성_성공(신분당선, 신분당선_COLOR, 처음_상행_종점역_ID, 처음_하행_종점역_ID, 10));
	}

	/*
	 * When 해당 노선의 하행 종점역을 상행역으로 하는 새로운 구간을 추가하면
	 * Then 해당 노선의 하행 종점역이 새로 추가한 구간의 하행역으로 변경된다.
	 */
	@Test
	void 구간_추가_성공() {
		// When
		지하철_구간_추가_성공(지하철_노선_Id, 처음_하행_종점역_ID, 새로운_하행_종점역_ID, 10);

		// Then
		노선의_하행_종점_확인(지하철_노선_Id, 새로운_하행_종점역_ID);
	}

	/*
	 * When 해당 노선의 하행 종점역을 상행역으로 하지 않는 새로운 구간을 추가하면
	 * Then 상행역 입력이 유효하지 않다는 예외가 발생한다.
	 */
	@Test
	void 구간_추가_시_유효하지_않은_상행역_입력_예외() {
		// When
		ExtractableResponse<Response> response = 지하철_구간_추가_실패(지하철_노선_Id, 처음_상행_종점역_ID, 새로운_하행_종점역_ID, 10);

		// Then
		에러코드_확인(response, INVALID_UP_STATION);
	}

	/*
	 * When 해당 노선의 상행 종점역을 하행역으로 하는 새로운 구간을 추가하면
	 * Then 하행역 입력이 유효하지 않다는 예외가 발생한다.
	 */
	@Test
	void 구간_추가_시_유효하지_않은_하행역_입력_예외() {
		// When
		ExtractableResponse<Response> response = 지하철_구간_추가_실패(지하철_노선_Id, 처음_하행_종점역_ID, 처음_상행_종점역_ID, 10);

		// Then
		에러코드_확인(response, INVALID_DOWN_STATION);
	}

	/*
	 * Given 1 개의 구간을 추가하고,
	 * When 해당 노선의 하행 종점역을 하행역으로 하는 구간을 제거하면
	 * Then 해당 노선의 하행 종점역이 추가했던 구간의 상행역으로 변경된다.
	 */
	@Test
	void 구간_제거_성공() {
		// Given
		지하철_구간_추가_성공(지하철_노선_Id, 처음_하행_종점역_ID, 새로운_하행_종점역_ID, 10);

		// When
		지하철_구간_제거_성공(지하철_노선_Id, 새로운_하행_종점역_ID);

		// Then
		노선의_하행_종점_확인(지하철_노선_Id, 처음_하행_종점역_ID);
	}

	/*
	 * When 해당 노선의 하행 종점역을 하행역으로 하는 구간을 제거하면
	 * Then 최소 구간 예외가 발생한다.
	 */
	@Test
	void 구간_제거_시_최소_구간_예외() {
		// When
		ExtractableResponse<Response> 구간_제거_실패_응답 = 지하철_구간_제거_실패(지하철_노선_Id, 처음_하행_종점역_ID);

		// Then
		에러코드_확인(구간_제거_실패_응답, MINIMUM_SECTION_COUNT);
	}

	/*
	 * Given 1 개의 구간을 추가하고,,
	 * When 해당 노선에 등록되지 않은 지하철역을 하행역으로하는 구간을 제거하면
	 * Then 노선에 등록되지 않은 구간 제거 예외가 발생한다.
	 */
	@Test
	void 구간_제거_시_노선에_등록되지_않은_구간_제거_예외() {
		// Given
		지하철_구간_추가_성공(지하철_노선_Id, 처음_하행_종점역_ID, 새로운_하행_종점역_ID, 10);

		// When
		String 노선에_등록되지_않은_지하철역_Id = "999";
		ExtractableResponse<Response> 구간_제거_실패_응답 = 지하철_구간_제거_실패(지하철_노선_Id, 노선에_등록되지_않은_지하철역_Id);

		// Then
		에러코드_확인(구간_제거_실패_응답, NOT_INCLUDED_STATION);
	}

	/*
	 * Given 1 개의 구간을 추가하고,
	 * When 해당 노선의 하행 종점역을 하행역으로 하지 않는 구간을 제거하면
	 * Then 유효하지 않은 하행역 입력 예외가 발생한다.
	 */
	@Test
	void 구간_제거_시_유효하지_않은_하행역_입력_예외() {
		// Given
		지하철_구간_추가_성공(지하철_노선_Id, 처음_하행_종점역_ID, 새로운_하행_종점역_ID, 10);

		// When
		ExtractableResponse<Response> 구간_제거_실패_응답 = 지하철_구간_제거_실패(지하철_노선_Id, 처음_하행_종점역_ID);

		// Then
		에러코드_확인(구간_제거_실패_응답, INVALID_DOWN_STATION);
	}

	private void 노선의_하행_종점_확인(String 지하철_노선_Id, String 새로운_하행_종점역_ID) {
		assertThat(하행_종점역_Id_추출(지하철_노선_조회_성공(지하철_노선_Id))).isEqualTo(새로운_하행_종점역_ID);
	}

	private void 에러코드_확인(ExtractableResponse<Response> response, SectionErrorCode invalidUpStation) {
		assertThat(에러코드_추출(response)).isEqualTo(invalidUpStation);
	}
}
