package subway.presentation;

import static io.restassured.RestAssured.*;
import static subway.fixture.StationFixtures.*;
import static subway.fixture.SubwayLineFixtures.*;

import org.springframework.http.MediaType;

import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.AcceptanceTest;
import subway.presentation.request.StationRequest;
import subway.presentation.request.SubwayLineRequest;

public abstract class SubwayAcceptanceTest extends AcceptanceTest {

	protected ExtractableResponse<Response> 지하철역_생성(String name) {
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

	protected ExtractableResponse<Response> 지하철노선_생성(SubwayLineRequest.Create stationLineCreateRequest) {
		return requestApi(
			with()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(stationLineCreateRequest),
			Method.POST,
			SUBWAY_LINE_ROOT_PATH
		);
	}

	protected ExtractableResponse<Response> 지하철노선_조회(Long subwayLineId) {
		return requestApi(
			Method.GET,
			SUBWAY_LINE_ROOT_PATH + "/{id}",
			subwayLineId
		);
	}
}
