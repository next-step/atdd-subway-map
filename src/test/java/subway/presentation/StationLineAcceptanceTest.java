package subway.presentation;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.fixture.StationFixture.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.AcceptanceTest;
import subway.presentation.request.SubwayLineRequest;
import subway.presentation.request.StationRequest;
import subway.presentation.response.SubwayLineResponse;

public class StationLineAcceptanceTest extends AcceptanceTest {

	private static final String ROOT_PATH = "/lines";

	// When 지하철 노선을 생성하면
	// Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	@DisplayName("지하철노선을 생성하면 목록 조회 시 생성한 노선을 찾을 수 있다")
	@Test
	void 지하철노선을_생성하면_목록_조회_시_생성한_노선을_찾을_수_있다() {
		// given
		지하철역_생성("지하철역");
		지하철역_생성("새로운지하철역");

		SubwayLineRequest.Create stationLineCreateRequest = SubwayLineRequest.Create.builder()
			.name("신분당선")
			.color("bg-red-600")
			.upStationId(1L)
			.downStationId(2L)
			.distance(10)
			.build();

		// when
		ExtractableResponse<Response> createResponse = 지하철노선_생성(stationLineCreateRequest);

		List<SubwayLineResponse.LineInfo> lineInfos = 지하철노선_목록조회().as(new TypeRef<>() {
		});

		// then
		assertAll(
			() -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(lineInfos).hasSize(1)
		);
	}

	// Given 2개의 지하철 노선을 생성하고
	// When 지하철 노선 목록을 조회하면
	// Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
	@DisplayName("지하철노선 2개를 생성하고 목록조회 시 2개의 노선을 조회할 수 있다")
	@Test
	void 지하철노선_2개를_생성하고_목록조회_시_2개의_노선을_조회할_수_있다() {
		지하철역_생성("지하철역");
		지하철역_생성("새로운지하철역");
		지하철역_생성("또다른지하철역");

		SubwayLineRequest.Create stationLineCreateRequest1 = SubwayLineRequest.Create.builder()
			.name("신분당선")
			.color("bg-red-600")
			.upStationId(1L)
			.downStationId(2L)
			.distance(10)
			.build();

		SubwayLineRequest.Create stationLineCreateRequest2 = SubwayLineRequest.Create.builder()
			.name("2호선")
			.color("bg-red-300")
			.upStationId(1L)
			.downStationId(3L)
			.distance(10)
			.build();

		지하철노선_생성(stationLineCreateRequest1);
		지하철노선_생성(stationLineCreateRequest2);

		List<SubwayLineResponse.LineInfo> lineInfos = 지하철노선_목록조회().as(new TypeRef<>() {
		});

		assertThat(lineInfos).hasSize(2);
	}

	private ExtractableResponse<Response> 지하철노선_생성(SubwayLineRequest.Create stationLineCreateRequest) {
		return requestApi(
			with()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(stationLineCreateRequest),
			Method.POST,
			ROOT_PATH
		);
	}

	private ExtractableResponse<Response> 지하철역_생성(String name) {
		StationRequest stationRequest = StationRequest.builder()
			.name(name)
			.build();

		return requestApi(
			with().contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(stationRequest),
			Method.POST,
			STATION_ROOT_PATH
		);
	}

	private ExtractableResponse<Response> 지하철노선_목록조회() {
		return requestApi(
			Method.GET,
			ROOT_PATH
		);
	}
}
