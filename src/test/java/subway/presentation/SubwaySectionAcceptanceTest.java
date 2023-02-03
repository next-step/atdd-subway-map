package subway.presentation;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.fixture.SectionFixtures.*;
import static subway.fixture.StationFixtures.*;
import static subway.fixture.SubwayLineFixtures.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.exception.SectionErrorCode;
import subway.fixture.StationFixtures;
import subway.presentation.request.SectionRequest;
import subway.presentation.response.SubwayLineResponse;

public class SubwaySectionAcceptanceTest extends SubwayAcceptanceTest {

	private static final String ROOT_PATH = "/lines/{id}/sections";

	@BeforeEach
	void setUp() {
		지하철역_생성(STATION_NAME_1);
		지하철역_생성(STATION_NAME_2);
		지하철역_생성(STATION_NAME_3);
		지하철역_생성(STATION_NAME_4);

	}

	// Given 노선 1개를 생성하고
	// When 구간등록을 요청하면
	// Then 지하철 노선 조회 시 하행 종점역이 변경됨을 확인할 수 있다
	@DisplayName("구간등록 요청시 지하철 노선을 조회하면 하행역 변경을 확인할 수 있다")
	@Test
	void 구간등록_요청시_지하철_노선을_조회하면_하행역_변경을_확인할_수_있다() {
		long subwayLineId = 지하철노선_생성(혜화_to_동대문())
			.jsonPath().getLong("id");

		ExtractableResponse<Response> response = 구간등록_요청(subwayLineId, 동대문_동대문역사문화공원());

		SubwayLineResponse.LineInfo subwayLineInfo = 지하철노선_조회(subwayLineId).as(new TypeRef<>() {
		});

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(subwayLineInfo.getStations()).extracting("id")
				.containsOnly(헤화, 동대문역사문화공원)
		);
	}

	// Given 노선 1개를 생성하고
	// When 구간등록을 요청하면
	// Then 새로운 구간 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닐때 예외가 발생한다
	@DisplayName("새로운 구간 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닐때 예외가 발생한다")
	@Test
	void 새로운_구간_상행역이_해당_노선에_등록되어있는_하행_종점역이_아닐때_예외가_발생한다() {
		long subwayLineId = 지하철노선_생성(혜화_to_동대문())
			.jsonPath().getLong("id");

		ExtractableResponse<Response> response = 구간등록_요청(subwayLineId, 동대문역사문화공원_충무로());

		ApiErrorResponse apiErrorResponse = response.as(new TypeRef<>() {
		});

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(apiErrorResponse.getMessage()).isEqualTo(
				SectionErrorCode.INVALID_SECTION_UP_STATION.getMessage()
			),
			() -> assertThat(apiErrorResponse.getCode()).isEqualTo(
				SectionErrorCode.INVALID_SECTION_UP_STATION.getCode()
			)
		);
	}

	// Given 노선 1개를 생성하고
	// When 구간등록을 요청하면
	// Then 새로운 구간 등록시 해당 노선에 등록되어있는 역일경우 예외가 발생한다
	@DisplayName("새로운 구간 등록시 해당 노선에 등록되어있는 역일경우 예외가 발생한다")
	@Test
	void 새로운_구간_등록시_해당_노선에_등록되어있는_역일경우_예외가_발생한다() {
		long subwayLineId = 지하철노선_생성(혜화_to_동대문())
			.jsonPath().getLong("id");

		ExtractableResponse<Response> response = 구간등록_요청(subwayLineId, 혜화_동대문());

		ApiErrorResponse apiErrorResponse = response.as(new TypeRef<>() {
		});

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(apiErrorResponse.getMessage()).isEqualTo(
				SectionErrorCode.ALREADY_STATION_REGISTERED.getMessage()
			),
			() -> assertThat(apiErrorResponse.getCode()).isEqualTo(
				SectionErrorCode.ALREADY_STATION_REGISTERED.getCode()
			)
		);
	}

	// Given 노선 1개를 생성하고
	// Given 구간등록 2개를 요청하고
	// When 구간제거를 요청하면
	// Then 지하철 노선 조회 시 하행역이 변경됨을 확인할 수 있다
	@DisplayName("구간등록을 한 후 구간제거를 요청하면 지하철 노선 조회하면 하행역이 변경됨을 확인할 수 있다")
	@Test
	void 구간등록을_한_후_구간제거를_요청하면_지하철_노선_조회하면_하행역이_변경됨을_확인할_수_있다() {
		long subwayLineId = 지하철노선_생성(혜화_to_동대문())
			.jsonPath().getLong("id");

		구간등록_요청(subwayLineId, 동대문_동대문역사문화공원());

		ExtractableResponse<Response> deleteResponse = 구간제거_요청(subwayLineId, 동대문역사문화공원);

		SubwayLineResponse.LineInfo subwayLineInfo = 지하철노선_조회(subwayLineId).as(new TypeRef<>() {
		});

		assertAll(
			() -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
			() -> assertThat(subwayLineInfo.getStations()).extracting("id")
				.containsOnly(헤화, 동대문)
		);
	}

	// Given 노선 1개를 생성하고
	// Given 구간등록 2개를 요청하고
	// When 구간제거를 요청하면
	// Then 제거할 구간 상행역이 마지막 구간이 아닐 경우 예외가 발생한다
	@DisplayName("제거할 구간 상행역이 마지막 구간이 아닐 경우 예외가 발생한다")
	@Test
	void 제거할_구간_상행역이_마지막_구간이_아닐_경우_예외가_발생한다() {
		long subwayLineId = 지하철노선_생성(혜화_to_동대문())
			.jsonPath().getLong("id");

		구간등록_요청(subwayLineId, 동대문_동대문역사문화공원());
		구간등록_요청(subwayLineId, 동대문역사문화공원_충무로());

		ExtractableResponse<Response> deleteResponse = 구간제거_요청(subwayLineId, 동대문역사문화공원);

		ApiErrorResponse apiErrorResponse = deleteResponse.as(new TypeRef<>() {
		});

		assertAll(
			() -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(apiErrorResponse.getMessage()).isEqualTo(
				SectionErrorCode.INVALID_SECTION_REMOVE_BECAUSE_DOWN_STATION.getMessage()
			),
			() -> assertThat(apiErrorResponse.getCode()).isEqualTo(
				SectionErrorCode.INVALID_SECTION_REMOVE_BECAUSE_DOWN_STATION.getCode()
			)
		);
	}

	// Given 노선 1개를 생성하고
	// When 구간제거를 요청하면
	// Then 제거할 구간이 상행 종점역과 하행 종점역만 있는 경우 예외가 발생한다
	@DisplayName("제거할 구간이 상행 종점역과 하행 종점역만 있는 경우 예외가 발생한다")
	@Test
	void 제거할_구간이_상행_종점역과_하행_종점역만_있는_경우_예외가_발생한다() {
		long subwayLineId = 지하철노선_생성(혜화_to_동대문())
			.jsonPath().getLong("id");

		ExtractableResponse<Response> deleteResponse = 구간제거_요청(subwayLineId, StationFixtures.동대문);

		ApiErrorResponse apiErrorResponse = deleteResponse.as(new TypeRef<>() {
		});

		assertAll(
			() -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
			() -> assertThat(apiErrorResponse.getMessage()).isEqualTo(
				SectionErrorCode.INVALID_SECTION_REMOVE_BECAUSE_ONLY_ONE_SECTION.getMessage()
			),
			() -> assertThat(apiErrorResponse.getCode()).isEqualTo(
				SectionErrorCode.INVALID_SECTION_REMOVE_BECAUSE_ONLY_ONE_SECTION.getCode()
			)
		);
	}

	private ExtractableResponse<Response> 구간등록_요청(Long subwayLineId, SectionRequest.Create createRequest) {
		return requestApi(
			with()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(createRequest),
			Method.POST,
			ROOT_PATH,
			subwayLineId
		);
	}

	private ExtractableResponse<Response> 구간제거_요청(
		Long subwayLineId,
		Long stationId) {
		return requestApi(
			with()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.param("stationId", stationId),
			Method.DELETE,
			ROOT_PATH,
			subwayLineId
		);
	}
}
