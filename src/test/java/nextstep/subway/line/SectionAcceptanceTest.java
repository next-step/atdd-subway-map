package nextstep.subway.line;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
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
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 구간에 관련된 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 판교역;
	private LineResponse 신분당선;

	@BeforeEach
	void before() {
		강남역 = 지하철역_생성_요청(지하철역_생성("강남역")).as(StationResponse.class);
		양재역 = 지하철역_생성_요청(지하철역_생성("양재역")).as(StationResponse.class);
		판교역 = 지하철역_생성_요청(지하철역_생성("판교역")).as(StationResponse.class);

		신분당선 = 지하철_노선_생성_요청(노선_생성("신분당선", "bg-red-600",
			String.valueOf(강남역.getId()), String.valueOf(양재역.getId()), "5"))
			.as(LineResponse.class);
	}

	@DisplayName("지하철 노선에 구간을 등록한다.")
	@Test
	void createSection() {
		// when
		final ExtractableResponse<Response> response = 지하철_노선에_구간_등록_요청(신분당선, 양재역, 판교역, 3);

		// then
		지하철_노선에_구간_등록됨(response);
	}

	@DisplayName("지하철 노선에 구간을 제거한다.")
	@Test
	void deleteSection() {
		// given
		지하철_노선에_구간_등록_요청(신분당선, 양재역, 판교역, 3);

		// when
		final ExtractableResponse<Response> response = 지하철_구간_제거_요청(신분당선, 판교역);

		// then
		지하철_구간_제거됨(response);
	}

	@DisplayName("지하철 노선에 등록된 구간을 통해 역 목록을 조회한다.")
	@Test
	void findStationsBySection() {
		// given
		지하철_노선에_구간_등록_요청(신분당선, 양재역, 판교역, 3);

		// when
		final ExtractableResponse<Response> response = 지하철역_목록_조회();

		// then
		지하철역_목록_조회됨(response, Arrays.asList(강남역, 양재역, 판교역));
	}

	private void 지하철역_목록_조회됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		final LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);

		assertThat(lineResponse.getStations()).usingRecursiveFieldByFieldElementComparator()
			.containsExactlyElementsOf(stationResponses);
	}

	private ExtractableResponse<Response> 지하철역_목록_조회() {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.pathParam("lineId", 신분당선.getId())
			.when().get("/lines/{lineId}")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 지하철_노선에_구간_등록_요청(
		LineResponse line, StationResponse upStation,
		StationResponse downStation, int distance) {
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", String.valueOf(upStation.getId()));
		params.put("downStationId", String.valueOf(downStation.getId()));
		params.put("distance", String.valueOf(distance));

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.pathParam("lineId", line.getId())
			.when().post("/lines/{lineId}/sections")
			.then().log().all()
			.extract();
	}

	private void 지하철_노선에_구간_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private void 지하철_구간_제거됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private ExtractableResponse<Response> 지하철_구간_제거_요청(LineResponse line, StationResponse station) {
		return RestAssured
			.given().log().all()
			.pathParam("lineId", line.getId())
			.queryParam("stationId", station.getId())
			.when().delete("/lines/{lineId}/sections")
			.then().log().all()
			.extract();
	}
}
