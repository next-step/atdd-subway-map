package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.common.exception.errorcode.EntityErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;

@DisplayName("구간등록 테스트")
public class SectionAcceptanceTest extends AcceptanceTest {
	private static final String baseUrlPrefix = "/lines";
	/**
	 * 1. 구간 등록
	 * 	1.1 노선등록이 되어 있어야만함
	 * 	1.2 upStationId가 기존 구간의 downStationId와 동일해야 한다.
	 * 	1.3 downStationId가 기존 구간에 등록된(upStationId, downStationId) 역이면 안된다
	 * 	1.4 에러처리 해야함
	 * 	결론 : 노선 마지막에만 새로운 구간을 추가할수 있다
	 *
	 *
	 * 	2. 구간 제거
	 * 	 2.1 station 에 등록된 역인지 체크
	 * 	 2.2 해당 Line 의 마지막 section 의 downStationId와 동일한지 체크
	 * 	 2.3 stationLine 이 등록된 section 이 없는 경우 삭제 불가능
	 */

	/**
	 * given 지하철 노선 생성하고
	 * when 지하철 구간을 생성하면
	 * then 지하철 구간 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철구간 생성")
	@Test
	void createSection() {
		//given
		long lineId = 지하철_노선_생성(SIN_BOONDANG_LINE, LINE_COLOR_RED);
		//when
		long sectionId = 지하철_구간_생성(lineId, 2, 3, 1);
		//then
		assertEquals(지하철_구간_조회_BY_ID(sectionId, HttpStatus.OK).get("sectionId"), (int)sectionId);

	}

	/**
	 * given 지하철 노선 생성하고
	 * when 지하철 구간을 2개 생성하고
	 * then 지하철 구간목록 조회시 등록한 2개의 구간을 조회 할 수 있다
	 */
	@DisplayName("지하철구간 목록 조회")
	@Test
	void getSections() {
		//given
		long lineId = 지하철_노선_생성(SIN_BOONDANG_LINE, LINE_COLOR_RED);

		//when
		long section1 = 지하철_구간_생성(lineId, 2, 3, 1);
		long section2 = 지하철_구간_생성(lineId, 3, 4, 1);

		//then
		assertThat(지하철_구간_목록_조회(lineId)).hasSize(2)
			.containsExactly((int)section1, (int)section2);
	}

	/**
	 * given 지하철 노선 생성하고
	 * when 지하철 구간을 생성하고
	 * then 생성한 지하철 구간 정보를 응답 받을 수 있다.
	 */
	@DisplayName("지하철구간 조회")
	@Test
	void 지하철구간_조회() {
		//given
		long lineId = 지하철_노선_생성(SIN_BOONDANG_LINE, LINE_COLOR_RED);
		//when
		long sectionId = 지하철_구간_생성(lineId, 2, 3, 1);
		//then
		assertEquals(지하철_구간_조회_BY_ID(sectionId, HttpStatus.OK).get("sectionId"), (int)sectionId);
	}

	/**
	 * given 지하철 노선 생성하고
	 * given 지하철 구간을 생성하고
	 * when 지하철 구간을 삭제하면
	 * then 지하철 구간정보는 삭제된다
	 */
	@DisplayName("지하철구간 삭제")
	@Test
	void deleteSection() {
		//given
		long lindId = 지하철_노선_생성(SIN_BOONDANG_LINE, LINE_COLOR_RED);
		long sectionId = 지하철_구간_생성(lindId, 2, 3, 1);
		//when
		지하철_구간_삭제(lindId, 3);
		//then
		Map<String, Object> response = 지하철_구간_조회_BY_ID(sectionId, HttpStatus.OK);
		assertEquals(ENTITY_NOT_FOUND.toString(), response.get("errorCode"));
	}

	private long 지하철_구간_생성(long lineId, long upStationId, long downStationId, long distance) {
		String postUrl = String.format("%s/%s/section", baseUrlPrefix, lineId);
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(지하철_구간_생성_파라미터생성(upStationId, downStationId, distance))
			.when().post(postUrl)
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract().jsonPath().getLong("sectionId");
	}

	private List<Integer> 지하철_구간_목록_조회(long lineId) {
		String selectPath = String.format("%s/%s/section", baseUrlPrefix, lineId);
		return RestAssured.given().log().all()
			.when().get(selectPath)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath().getList("sectionId");
	}

	private Map<String, Object> 지하철_구간_조회_BY_ID(long sectionId, HttpStatus httpStatus) {
		String selectPath = String.format("%s/section/%s", baseUrlPrefix, sectionId);
		ExtractableResponse extractableResponse = RestAssured.given().log().all()
			.when().get(selectPath)
			.then().log().all()
			.statusCode(httpStatus.value())
			.extract();

		if (httpStatus == HttpStatus.OK) {
			return extractableResponse.jsonPath().get();
		}
		return null;
	}

	private void 지하철_구간_삭제(long lineId, long stationId) {
		String deleteUrl = String.format("%s/%s/section?stationId=%s", baseUrlPrefix, lineId, stationId);
		RestAssured.given().log().all()
			.when().delete(deleteUrl)
			.then().log().all()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

	private Map<String, Object> 지하철_구간_생성_파라미터생성(long upStationId, long downStationId, long distance) {
		Map<String, Object> requestParameter = new HashMap<>();
		requestParameter.put("upStationId", upStationId);
		requestParameter.put("downStationId", downStationId);
		requestParameter.put("distance", distance);
		return requestParameter;
	}
}
