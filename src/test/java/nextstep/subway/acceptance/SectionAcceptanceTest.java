package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.common.exception.errorcode.EntityErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
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
	 * 	 2.2 해당 Line 의 마지막 section 의 downStationId와 동일한지 체크
	 * 	 2.3 등록된 section 이 1개인 경우 삭제 불가능
	 */
	long lineId;

	@BeforeEach
	void setupLineData() {
		lineId = 지하철_노선_생성(SIN_BOONDANG_LINE, LINE_COLOR_RED);
	}

	/**
	 * given
	 * when 지하철 구간을 생성하면
	 * then 지하철 구간  조회 시 생성한 노선정보와 일치 한다.
	 */
	@DisplayName("지하철구간 생성")
	@Test
	void createSection() {
		//given

		//when
		int sectionId = (int)지하철_구간_생성(lineId, UP_STATION_ID_1, DOWN_STATION_ID_1, DISTANCE_1, HttpStatus.CREATED,
			"sectionId");
		//then
		assertTrue(등록된_구간정보와_조회된_구간정보_일치여부(sectionId, UP_STATION_ID_1, DOWN_STATION_ID_1, DISTANCE_1));

	}

	/**
	 * given
	 * when  지하철 구간 생성하고
	 * then 지하철 신규 구간 상행역이 기존구간 하행역이 아닌 경우로 등록하면 INVALID_STATUS 코드 응답 받는다
	 */
	@DisplayName("지하철구간_생성_상행역이_기존_구간_하행_종점이_아닌경우")
	@Test
	void createSectionErrorCase1() {
		//when
		지하철_구간_생성(lineId, UP_STATION_ID_1, DOWN_STATION_ID_1, DISTANCE_1, HttpStatus.CREATED, "sectionId");

		//then
		assertEquals("INVALID_STATUS",
			지하철_구간_생성(lineId, UP_STATION_ID_1, DOWN_STATION_ID_1, DISTANCE_1, HttpStatus.OK, "errorCode"));
	}

	/**
	 * given
	 * when 지하철 구간 생성하고
	 * then 지하철 신규 구간 하행역이 기존구간에 등록된 역이면 INVALID_STATUS 코드 응답 받는다
	 */
	@DisplayName("지하철구간_생성_상행역이_기존_구간_하행_종점이_아닌경우")
	@Test
	void createSectionErrorCase2() {
		//given

		//when
		지하철_구간_생성(lineId, UP_STATION_ID_1, DOWN_STATION_ID_1, DISTANCE_1, HttpStatus.CREATED, "sectionId");

		//then
		assertEquals("INVALID_STATUS",
			지하철_구간_생성(lineId, UP_STATION_ID_2, DOWN_STATION_ID_1, DISTANCE_1, HttpStatus.OK, "errorCode"));
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

		//when
		int section1 = (int)지하철_구간_생성(lineId, UP_STATION_ID_1, DOWN_STATION_ID_1, DISTANCE_1, HttpStatus.CREATED,
			"sectionId");
		int section2 = (int)지하철_구간_생성(lineId, UP_STATION_ID_2, DOWN_STATION_ID_2, DISTANCE_2, HttpStatus.CREATED,
			"sectionId");

		//then
		assertThat(지하철_구간_목록_조회(lineId)).hasSize(2)
			.containsExactly(section1, section2);
	}

	/**
	 * given
	 * when 지하철 구간을 생성하고
	 * then 생성한 지하철 구간 정보를 응답 받을 수 있다.
	 */
	@DisplayName("지하철구간 조회")
	@Test
	void getSelection() {
		//given

		//when
		long sectionId = (int)지하철_구간_생성(lineId, UP_STATION_ID_1, DOWN_STATION_ID_1, DISTANCE_1, HttpStatus.CREATED,
			"sectionId");
		//then
		assertEquals(지하철_구간_조회_BY_ID(sectionId, HttpStatus.OK).get("sectionId"), (int)sectionId);
	}

	/**
	 * given
	 * given 지하철 구간을 생성을 2번 하고
	 * when 지하철 구간을 삭제하면
	 * then 지하철 구간정보는 삭제된다
	 */
	@DisplayName("지하철구간 삭제")
	@Test
	void deleteSection() {
		//given

		지하철_구간_생성(lineId, UP_STATION_ID_1, DOWN_STATION_ID_1, DISTANCE_1, HttpStatus.CREATED, "sectionId");
		long sectionId = (int)지하철_구간_생성(lineId, UP_STATION_ID_2, DOWN_STATION_ID_2, DISTANCE_2, HttpStatus.CREATED,
			"sectionId");
		//when
		지하철_구간_삭제(lineId, DOWN_STATION_ID_2);
		//then
		assertEquals(ENTITY_NOT_FOUND.toString(), 지하철_구간_조회_BY_ID(sectionId, HttpStatus.OK).get("errorCode"));
	}

	/**
	 * given
	 * given
	 * when 지하철 구간을 생성을 2번 하고
	 * then 첫번쨰 등록된 구간을 삭제하면 INVALID_STATUS 코드 응답 받는다
	 */
	@DisplayName("지하철구간 삭제요청시, 마지막 section 의 하행역이 아닌경우 오류")
	@Test
	void deleteSectionErrorCase1() {
		//given

		//when
		지하철_구간_생성(lineId, UP_STATION_ID_1, DOWN_STATION_ID_1, DISTANCE_1, HttpStatus.CREATED, "sectionId");
		지하철_구간_생성(lineId, UP_STATION_ID_2, DOWN_STATION_ID_2, DISTANCE_2, HttpStatus.CREATED, "sectionId");

		//then
		assertEquals("INVALID_STATUS", 지하철_구간_삭제_응답(lineId, DOWN_STATION_ID_1));
	}

	/**
	 * given
	 * given
	 * when 지하철 구간을 생성을 1번 하고
	 * then 첫번쨰 등록된 구간을 삭제하면 INVALID_STATUS 코드 응답 받는다
	 */
	@DisplayName("지하철구간 1개있을떄 삭제 요청시 오류 ")
	@Test
	void deleteSectionErrorCase2() {
		//given

		//when
		지하철_구간_생성(lineId, UP_STATION_ID_1, DOWN_STATION_ID_1, DISTANCE_1, HttpStatus.CREATED, "sectionId");

		//then
		assertEquals("INVALID_STATUS", 지하철_구간_삭제_응답(lineId, DOWN_STATION_ID_1));
	}

	private Object 지하철_구간_생성(long lineId, long upStationId, long downStationId, long distance, HttpStatus httpStatus,
		String returnName) {
		String postUrl = String.format("%s/%s/section", baseUrlPrefix, lineId);
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(지하철_구간_생성_파라미터생성(upStationId, downStationId, distance))
			.when().post(postUrl)
			.then().log().all()
			.statusCode(httpStatus.value())
			.extract().jsonPath().get(returnName);
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

		return extractableResponse.jsonPath().get();
	}

	private void 지하철_구간_삭제(long lineId, long stationId) {
		String deleteUrl = String.format("%s/%s/section?stationId=%s", baseUrlPrefix, lineId, stationId);
		RestAssured.given().log().all()
			.when().delete(deleteUrl)
			.then().log().all()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

	private String 지하철_구간_삭제_응답(long lineId, long stationId) {
		String deleteUrl = String.format("%s/%s/section?stationId=%s", baseUrlPrefix, lineId, stationId);
		return RestAssured.given().log().all()
			.when().delete(deleteUrl)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath().getString("errorCode");
	}

	private Map<String, Object> 지하철_구간_생성_파라미터생성(long upStationId, long downStationId, long distance) {
		Map<String, Object> requestParameter = new HashMap<>();
		requestParameter.put("upStationId", upStationId);
		requestParameter.put("downStationId", downStationId);
		requestParameter.put("distance", distance);
		return requestParameter;
	}

	private boolean 등록된_구간정보와_조회된_구간정보_일치여부(int sectionId, int upStationId, int downStationId,
		int distance) {

		Map<String, Object> response = 지하철_구간_조회_BY_ID(sectionId, HttpStatus.OK);

		int registeredSectionId = (int)response.get("sectionId");
		int registeredUpStationId = (int)response.get("upStationId");
		int registeredDownStationId = (int)response.get("downStationId");
		int registeredDistance = (int)response.get("distance");

		if (sectionId != registeredSectionId) {
			return false;
		}
		if (upStationId != registeredUpStationId) {
			return false;
		}
		if (downStationId != registeredDownStationId) {
			return false;
		}
		if (distance != registeredDistance) {
			return false;
		}

		return true;
	}
}
