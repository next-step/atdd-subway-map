package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineAcceptanceTest.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;

@DisplayName("구간등록 테스트")
public class SelectionAcceptanceTest extends AcceptanceTest {
	private static final String baseUrlPrefix = "/lines";
	/**
	 * 1. 구간 등록
	 * 	1.1 노선등록이 되어 있어야만함
	 * 	1.2 upStationId가 기존 구간의 downStationId와 동일해야 한다.
	 * 	1.3 downStationId가 기존 구간에 등록된(upStationId, downStationId) 역이면 안된다
	 * 	1.4 에러처리 해야함
	 * 	결론 결국 노선 마지막에만 새로운 구간을 추가할수 있는거군?
	 *
	 * 	SELECTION - Domain
	 * 	  id
	 * 	  lineId
	 * 	  upStationId
	 * 	  downStationId
	 * 	  distance
	 *
	 * 	2. 구간 제거
	 * 	 2.1 station 에 등록된 역인지 체크
	 * 	 2.2 해당 Line 의 마지막 selection 의 downStationId와 동일한지 체크
	 * 	 2.3 stationLine 이 등록된 selection이 없는 경우 삭제 불가능
	 */

	/**
	 * given 지하철 노선 생성하고
	 * when 지하철 구간을 생성하면
	 * then 지하철 구간 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철구간 생성")
	@Test
	void createSelection() {
		//given
		지하철_노선_생성(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		//when
		지하철_구간_생성(1, 1, 1, 1);
		//then
		지하철_구간_목록_조회();
	}

	/**
	 * given 지하철 노선 생성하고
	 * given 지하철 구간을 2개 생성하고
	 * when  지하철 구간 목록을 조회하면
	 * then 지하철 구간목록 조회시 등록한 2개의 구간을 조회 할 수 있다
	 */
	@DisplayName("지하철구간 목록 조회")
	@Test
	void getSelections() {
		//given
		지하철_노선_생성(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		지하철_구간_생성(1, 1, 1, 1);
		지하철_구간_생성(1, 1, 1, 1);
		//when
		지하철_구간_목록_조회();
		//then

	}

	/**
	 * given 지하철 노선 생성하고
	 * given 지하철 구간을 생성하고
	 * when 생성한 지하철 구간을 조회하면
	 * then 생성한 지하철 구간 정보를 응답 받을 수 있다.
	 */
	@DisplayName("지하철구간 조회")
	@Test
	void 지하철구간_조회() {
		//given
		지하철_노선_생성(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		지하철_구간_생성(1, 1, 1, 1);
		//when
		지하철_구간_조회_BY_ID();
		//then
	}

	/**
	 * given 지하철 노선 생성하고
	 * given 지하철 구간을 생성하고
	 * when 지하철 구간을 수정하면
	 * then 지하철 구간정보는 수정된다
	 */
	@DisplayName("지하철구간 수정")
	@Test
	void updateSelection() {
		//given
		지하철_노선_생성(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		지하철_구간_생성(1, 1, 1, 1);
		//when
		지하철_구간_수정();
	}

	/**
	 * given 지하철 노선 생성하고
	 * given 지하철 구간을 생성하고
	 * when 지하철 구간을 삭제하면
	 * then 지하철 구간정보는 삭제된다
	 */
	@DisplayName("지하철구간 삭제")
	@Test
	void deleteSelection() {
		//given
		지하철_노선_생성(SIN_BOONDANG_LINE, LINE_COLOR_RED, 1, 2, 10);
		지하철_구간_생성(1, 1, 1, 1);
		//when
		지하철_구간_삭제();
	}

	private void 지하철_구간_생성(long lineId, long upStationId, long downStationId, long distance) {
		String postUrl = String.format("%s/%s/selection", baseUrlPrefix, lineId);
		RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(지하철_구간_생성_파라미터생성(upStationId, downStationId, distance))
			.when().post(postUrl)
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract();

	}

	private void 지하철_구간_목록_조회() {

	}

	private void 지하철_구간_조회_BY_ID() {

	}

	private void 지하철_구간_수정() {

	}

	private void 지하철_구간_삭제() {

	}

	private Map<String, Object> 지하철_구간_생성_파라미터생성(long upStationId, long downStationId, long distance) {
		Map<String, Object> requestParameter = new HashMap<>();
		requestParameter.put("upStationId", upStationId);
		requestParameter.put("downStationId", downStationId);
		requestParameter.put("distance", distance);
		return requestParameter;
	}
}
