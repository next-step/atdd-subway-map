package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철노선 구간 관련 기능")
@AcceptanceTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {
	private static final String LINE_NAME = "신분당선";
	private static final String NEW_STATION_NAME = "양재시민의숲";
	private ExtractableResponse<Response> responseOfSection;
	private static Long upStationId;
	private static Long downStationId;
	private static Long newStationId;

	private Long lineId;

	private final StationAcceptanceTest stationAcceptanceTest;
	private final LineAcceptanceTest lineAcceptanceTest;

	public SectionAcceptanceTest() {
		this.stationAcceptanceTest = new StationAcceptanceTest();
		this.lineAcceptanceTest = new LineAcceptanceTest();
	}


	@BeforeEach
	public void setUp() {
		// 지하철 노선 생성
		upStationId = stationAcceptanceTest.지하철역_생성_파싱_id(stationAcceptanceTest.지하철역_생성("강남역"));
		downStationId = stationAcceptanceTest.지하철역_생성_파싱_id(stationAcceptanceTest.지하철역_생성("양재역"));

		ExtractableResponse<Response> responseOfLine = lineAcceptanceTest.지하철노선_생성(LINE_NAME, "bg-red-600", upStationId,
			downStationId, 10L);
		lineId = responseOfLine.jsonPath().getLong("id");

		// given 신규 지하철역 생성
		newStationId = stationAcceptanceTest.지하철역_생성_파싱_id(stationAcceptanceTest.지하철역_생성(NEW_STATION_NAME));

		// when 지하철 구간 등록
		responseOfSection = 지하철구간_등록(lineId, downStationId, newStationId, 3L);
	}

	/**
	 * Given 지하철역을 추가로 생성하고
	 * When 구간을 등록하면
	 * Then 해당 역이 노선에 추가된다.
	 */
	@DisplayName("지하철 노선에 구간을 등록한다.")
	@Test
	void addSection() {
		// given 신규 지하철역 생성 -> setUp()
		// when 지하철 구간 등록 -> setUp()

		// then
		assertThat(responseOfSection.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(lineAcceptanceTest.지하철노선_조회_파싱_stationNames(lineAcceptanceTest.지하철노선_조회(lineId))).contains(NEW_STATION_NAME);
	}

	/**
	 * Given 지하철역을 추가로 생성하고
	 * When 예외발생 케이스로 구간등록을 하면
	 * Then 에러가 발생한다.
	 */
	@DisplayName("구간 등록 예외 케이스")
	@ParameterizedTest
	@MethodSource("addSectionErrorArgs")
	void addSectionError(Long upStationId, Long downStationId) {
		// given 신규 지하철역 생성 -> setUp()

		// when
		responseOfSection = 지하철구간_등록(lineId, upStationId, downStationId, 3L);

		// then
		assertThat(responseOfSection.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	static Stream<Arguments> addSectionErrorArgs() { // 구간 등록 예외 발생 케이스
		return Stream.of(
			Arguments.of(newStationId, downStationId), // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
			Arguments.of(newStationId, upStationId) // 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
		);
	}

	/**
	 * Given 지하철 구간을 신규 등록하고
	 * When 해당 구간을 삭제하면
	 * Then 해당 역이 지하철노선에서 삭제된다.
	 */
	@DisplayName("지하철 노선에서 구간을 제거한다.")
	@Test
	void subSection() {
		// when
		ExtractableResponse<Response> response = 지하철구간_제거(lineId, newStationId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		assertThat(lineAcceptanceTest.지하철노선_조회_파싱_stationNames(lineAcceptanceTest.지하철노선_조회(lineId))).doesNotContain(NEW_STATION_NAME);
	}

	/**
	 * Given 지하철 구간을 신규 등록하고
	 * When 상행역을 구간에서 제거하면
	 * Then 에러가 발생한다.
	 */
	@DisplayName("구간 제거 예외) 마지막 구간만 제거할 수 있다.")
	@Test
	void subSectionError1() {
		// given 구간 생성 -> setUp()

		// when
		ExtractableResponse<Response> response = 지하철구간_제거(lineId, downStationId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	/**
	 * Given 지하철 구간을 한개만 남겨놓고
	 * When 구간을 제거하면
	 * Then 에러가 발생한다.
	 */
	@DisplayName("구간 제거 예외) 구간이 1개인 경우 역을 삭제할 수 없다.")
	@Test
	void subSectionError2() {
		// given 구간 생성 -> setUp()
		지하철구간_제거(lineId, newStationId);

		// when
		ExtractableResponse<Response> response = 지하철구간_제거(lineId, downStationId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private ExtractableResponse<Response> 지하철구간_등록(Long lineId, Long upStationId, Long downStationId, Long distance) {
		Map<String, Object> params = new HashMap<>();
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);
		params.put("distance", distance);

		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines/" + lineId + "/sections")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 지하철구간_제거(Long lineId, Long stationId) {
		return RestAssured.given().log().all()
			.param("stationId", stationId)
			.when().delete("/lines/" + lineId + "/sections")
			.then().log().all()
			.extract();
	}

}
