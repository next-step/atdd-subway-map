package subway.presentation;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.fixture.StationFixtures.*;

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
import subway.presentation.request.StationRequest;
import subway.presentation.response.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

	private static final String ROOT_PATH = "/stations";

	/**
	 * When 지하철역을 생성하면
	 * Then 지하철역이 생성된다
	 * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
	 */
	@DisplayName("지하철역을 생성한다.")
	@Test
	void 지하철역을_생성한다() {
		// when
		ExtractableResponse<Response> response = 지하철역_생성(STATION_NAME_1);

		// then
		List<String> stationNames = requestApi(
			Method.GET,
			ROOT_PATH
		).jsonPath().getList("name", String.class);

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(stationNames).containsAnyOf(STATION_NAME_1)
		);
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	@DisplayName("지하철역 2개의 조회 응답에 성공한다")
	@Test
	void 지하철역_2개의_조회_응답에_성공한다() {
		// given
		지하철역_생성(STATION_NAME_1);
		지하철역_생성(STATION_NAME_2);

		// when
		ExtractableResponse<Response> response = 지하철역을_조회한다();
		List<StationResponse> stationResponses = response.as(new TypeRef<>() {
		});

		// then
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(stationResponses).hasSize(2)
		);
	}

	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */
	@DisplayName("지하철역 1개역 삭제에 성공한다")
	@Test
	void 지하철역_1개역_삭제에_성공한다() {
		//given
		Long targetId = 지하철역_생성(STATION_NAME_1)
			.as(new TypeRef<StationResponse>() {
			}).getId();

		//when
		ExtractableResponse<Response> deleteResponse = 지하철역_삭제(targetId);

		// then
		List<StationResponse> stationResponses = 지하철역을_조회한다().as(new TypeRef<>() {
		});

		assertAll(
			() -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
			() -> assertThat(stationResponses).hasSize(0)
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
			ROOT_PATH
		);
	}

	private ExtractableResponse<Response> 지하철역을_조회한다() {
		return requestApi(
			Method.GET,
			ROOT_PATH
		);
	}

	private ExtractableResponse<Response> 지하철역_삭제(Long targetId) {
		return requestApi(
			Method.DELETE,
			ROOT_PATH + "/{id}",
			targetId
		);
	}
}
