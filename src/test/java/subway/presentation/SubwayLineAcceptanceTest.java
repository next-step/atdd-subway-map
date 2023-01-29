package subway.presentation;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.fixture.StationFixtures.*;
import static subway.fixture.SubwayLineFixtures.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.AcceptanceTest;
import subway.presentation.request.StationRequest;
import subway.presentation.request.SubwayLineRequest;
import subway.presentation.response.SubwayLineResponse;

public class SubwayLineAcceptanceTest extends AcceptanceTest {

	private static final String ROOT_PATH = "/lines";

	@BeforeEach
	void setUp() {
		지하철역_생성(STATION_NAME_1);
		지하철역_생성(STATION_NAME_2);
		지하철역_생성(STATION_NAME_3);
	}

	// When 지하철 노선을 생성하면
	// Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	@DisplayName("지하철노선을 생성하면 목록 조회 시 생성한 노선을 찾을 수 있다")
	@Test
	void 지하철노선을_생성하면_목록_조회_시_생성한_노선을_찾을_수_있다() {
		// given
		ExtractableResponse<Response> createResponse = 지하철노선_생성(getSubwayLineRequest1());


		// when
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
		// given
		지하철노선_생성(getSubwayLineRequest1());
		지하철노선_생성(getSubwayLineRequest2());

		// when
		List<SubwayLineResponse.LineInfo> lineInfos = 지하철노선_목록조회().as(new TypeRef<>() {
		});

		// then
		assertThat(lineInfos).hasSize(2);
	}

	// Given 지하철 노선을 생성하고
	// When 생성한 지하철 노선을 조회하면
	// Then 생성한 지하철 노선의 정보를 응답받을 수 있다
	@DisplayName("지하철노선을 생성하고 노선을 조회하면 노선의 정보를 응답받을 수 있다")
	@Test
	void 지하철노선을_생성하고_노선을_조회하면_노선의_정보를_응답받을_수_있다() {
		// given
		Long id = 지하철노선_생성(getSubwayLineRequest1())
			.as(new TypeRef<SubwayLineResponse.CreateInfo>() {
			})
			.getId();

		// when
		ExtractableResponse<Response> response = 지하철노선_조회(id);

		SubwayLineResponse.LineInfo lineInfo = response.as(new TypeRef<>() {
		});

		// then
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(lineInfo.getId()).isEqualTo(id),
			() -> assertThat(lineInfo.getStations()).hasSize(2)
		);
	}

	// Given 지하철 노선을 생성하고
	// When 생성한 지하철 노선을 수정하면
	// Then 해당 지하철 노선 정보는 수정된다
	@DisplayName("지하철노선을 생성하고 노선을 생성한 노선을 수정하면 수정된다")
	@Test
	void 지하철노선을_생성하고_노선을_생성한_노선을_수정하면_수정된다() {
		// given
		Long id = 지하철노선_생성(getSubwayLineRequest1())
			.as(new TypeRef<SubwayLineResponse.CreateInfo>() {
			})
			.getId();

		SubwayLineRequest.Update updateRequest = SubwayLineRequest.Update.builder()
			.name(SUBWAY_LINE_NAME_2)
			.color(SUBWAY_LINE_COLOR_2)
			.build();

		// when
		ExtractableResponse<Response> response = 지하철노선_수정(id, updateRequest);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	// Given 지하철 노선을 생성하고
	// When 생성한 지하철 노선을 삭제하면
	// Then 해당 지하철 노선 정보는 삭제된다
	@DisplayName("지하철노선을 생성하고 노선을 생성한 노선을 삭제하면 삭제된다")
	@Test
	void 지하철노선을_생성하고_노선을_생성한_노선을_삭제하면_삭제된다() {
		// given
		Long id = 지하철노선_생성(getSubwayLineRequest1())
			.as(new TypeRef<SubwayLineResponse.CreateInfo>() {
			})
			.getId();

		// when
		ExtractableResponse<Response> response = 지하철노선_삭제(id);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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

	private ExtractableResponse<Response> 지하철노선_조회(Long id) {
		return requestApi(
			Method.GET,
			ROOT_PATH + "/{id}",
			id
		);
	}

	private ExtractableResponse<Response> 지하철노선_수정(Long id, SubwayLineRequest.Update updateRequest) {
		return requestApi(
			with().contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(updateRequest),
			Method.PUT,
			ROOT_PATH + "/{id}",
			id
		);
	}

	private ExtractableResponse<Response> 지하철노선_삭제(Long id) {
		return requestApi(
			Method.DELETE,
			ROOT_PATH + "/{id}",
			id
		);
	}
}
