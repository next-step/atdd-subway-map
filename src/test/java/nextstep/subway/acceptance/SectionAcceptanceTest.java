package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.dto.StationResponse;

@DisplayName("구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest{

	StationResponse 강남역;
	StationResponse 양재역;
	StationResponse 정자역;
	LineResponse 신분당선;

	@BeforeEach
	void setUpStation() {
		강남역 = 지하철역_생성("강남역").jsonPath().getObject(".", StationResponse.class);
		양재역 = 지하철역_생성("양재역").jsonPath().getObject(".", StationResponse.class);
		정자역 = 지하철역_생성("정자역").jsonPath().getObject(".", StationResponse.class);

		신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId()).jsonPath().getObject(".", LineResponse.class);;
	}

	/**
	 * Scenario
	 * 구간 등록 기능
	 *
	 * When
	 * 구간 등록 요청을 하면
	 *
	 * Then
	 * 노선의 하행에 구간이 등록된다
	 *
	 * Condition
	 * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
	 * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
	 * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
	 */
	@DisplayName("구간 등록")
	@Test
	void createSection() {
		SectionRequest request = new SectionRequest(양재역.getId(), 정자역.getId(), 3);

		// when
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines/" + 신분당선.getId() + "/sections")
			.then().log().all().extract();;

		// then
		// 새로운 역이 등록되었는지 확인
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		지하철_노선에_등록한_구간이_포함(response, Arrays.asList(강남역.getId(), 양재역.getId(), 정자역.getId()));
	}
	
	/**
	 * Scenario
	 * 구간 제거 기능
	 *
	 * When
	 * 구간 제거 요청을 하면
	 *
	 * Then
	 * 구간이 제거된다
	 *
	 * Condition
	 * 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
	 * 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
	 * 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.
	 */
	@DisplayName("구간 삭제")
	@Test
	void deleteSection() {
	}

	/**
	 * Scenario
	 * 등록된 구간을 통해 역 목록 조회 기능
	 *
	 * When
	 * 구간 조회를 요청하면
	 *
	 * Then
	 * 구간에 등록된 역이 조회된다
	 *
	 * Condition
	 * 지하철 노선 조회 시 등록된 역 목록을 함께 응답
	 * 노선에 등록된 구간을 순서대로 정렬하여 상행 종점부터 하행 종점까지 목록을 응답하기
	 */
	@Test
	void findStationsBySection() {
	}

	private void 지하철_노선에_등록한_구간이_포함(ExtractableResponse<Response> response, List<Long> expectedStationIds) {
		List<Long> resultStationIds = response.jsonPath().getList("sections", SectionResponse.class).stream()
			.map(SectionResponse::getId)
			.collect(Collectors.toList());
		assertThat(resultStationIds).isEqualTo(expectedStationIds);
	}

	private ExtractableResponse<Response> 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);
		params.put("upStationId", String.valueOf(upStationId));
		params.put("downStationId", String.valueOf(downStationId));
		params.put("distance", String.valueOf(10));

		return RestAssured
			.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all().extract();
	}

	private ExtractableResponse<Response> 지하철역_생성(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);

		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/stations")
			.then().log().all()
			.extract();
	}
}
