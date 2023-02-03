package subway.presentation;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
import subway.fixture.SubwayLineFixtures;
import subway.presentation.request.SectionRequest;
import subway.presentation.response.SubwayLineResponse;

public class SubwaySectionAcceptanceTest extends SubwayAcceptanceTest {

	private static final String ROOT_PATH = "/lines/{id}/sections";

	@BeforeEach
	void setUp() {
		지하철역_생성(STATION_NAME_1);
		지하철역_생성(STATION_NAME_2);
		지하철역_생성(STATION_NAME_3);

	}

	// Given 노선 1개를 생성하고
	// When 구간등록을 요청하면
	// Then 지하철 노선 조회 시 하행 종점역이 변경됨을 확인할 수 있다
	@DisplayName("구간등록 요청시 지하철 노선을 조회하면 하행역 변경을 확인할 수 있다")
	@Test
	void 구간등록_요청시_지하철_노선을_조회하면_하행역_변경을_확인할_수_있다() {
		long subwayLineId = 지하철노선_생성(SubwayLineFixtures.getSubwayLineRequest1())
			.jsonPath().getLong("id");

		SectionRequest.Create createRequest = SectionRequest.Create.builder()
			.upStationId(STATION_ID_2)
			.downStationId(STATION_ID_3)
			.distance(10)
			.build();

		ExtractableResponse<Response> response = 구간등록_요청(subwayLineId, createRequest);

		SubwayLineResponse.LineInfo subwayLineInfo = 지하철노선_조회(subwayLineId).as(new TypeRef<>() {
		});

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(subwayLineInfo.getStations()).extracting("id")
				.contains(EXISTING_SUBWAY_LINE_UP_STATION_ID, STATION_ID_3)
		);
	}

	// Given 노선 1개를 생성하고
	// When 구간등록을 요청하면
	// Then 새로운 구간 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닐때 예외가 발생한다
	@DisplayName("새로운 구간 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닐때 예외가 발생한다")
	@Test
	void 새로운_구간_상행역이_해당_노선에_등록되어있는_하행_종점역이_아닐때_예외가_발생한다() {
		long subwayLineId = 지하철노선_생성(SubwayLineFixtures.getSubwayLineRequest1())
			.jsonPath().getLong("id");

		SectionRequest.Create createRequest = SectionRequest.Create.builder()
			.upStationId(STATION_ID_1)
			.downStationId(STATION_ID_3)
			.distance(10)
			.build();

		ExtractableResponse<Response> response = 구간등록_요청(subwayLineId, createRequest);

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
		long subwayLineId = 지하철노선_생성(SubwayLineFixtures.getSubwayLineRequest1())
			.jsonPath().getLong("id");

		SectionRequest.Create createRequest = SectionRequest.Create.builder()
			.upStationId(STATION_ID_2)
			.downStationId(STATION_ID_1)
			.distance(10)
			.build();

		ExtractableResponse<Response> response = 구간등록_요청(subwayLineId, createRequest);

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
	// Given 구간등록을 요청하고
	// When 구간제거를 요청하면
	// Then 지하철 노선 조회 시 하행역이 변경됨을 확인할 수 있다
	/*@DisplayName("구간등록을 한 후 구간제거를 요청하면 지하철 노선 조회하면 하행역이 변경됨을 확인할 수 있다")
	@Test
	void 구간등록을_한_후_구간제거를_요청하면_지하철_노선_조회하면_하행역이_변경됨을_확인할_수_있다() {
		long subwayLineId = 지하철노선_생성(SubwayLineFixtures.getSubwayLineRequest1())
			.jsonPath().getLong("id");

		SectionRequest.Create createRequest = SectionRequest.Create.builder()
			.upStationId(STATION_ID_2)
			.downStationId(STATION_ID_3)
			.distance(10)
			.build();

		구간등록_요청(subwayLineId, createRequest);

		ExtractableResponse<Response> deleteResponse = 구간제거_요청(subwayLineId, STATION_ID_3);

		SubwayLineResponse.LineInfo subwayLineInfo = 지하철노선_조회(subwayLineId).as(new TypeRef<>() {
		});

		assertAll(
			() -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(subwayLineInfo.getStations()).extracting("id")
				.contains(EXISTING_SUBWAY_LINE_UP_STATION_ID, STATION_ID_2)
		);
	}*/

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
