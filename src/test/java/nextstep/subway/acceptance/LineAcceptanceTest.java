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
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.dto.StationResponse;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

	StationResponse 강남역;
	StationResponse 정자역;
	StationResponse 방화역;
	StationResponse 하남검단산역;

	@BeforeEach
	void setUpStation() {
		강남역 = 지하철역_생성("강남역").jsonPath().getObject(".", StationResponse.class);
		정자역 = 지하철역_생성("정자역").jsonPath().getObject(".", StationResponse.class);
		방화역 = 지하철역_생성("방화역").jsonPath().getObject(".", StationResponse.class);
		하남검단산역 = 지하철역_생성("하남검단산역").jsonPath().getObject(".", StationResponse.class);
	}

	/**
	 * When 지하철 노선 생성을 요청 하면
	 * Then 지하철 노선 생성이 성공한다.
	 */
	@DisplayName("지하철 노선 생성")
	@Test
	void createLine() {
		// when
		ExtractableResponse<Response> response = 지하철_노선_생성("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId());
		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
	}

	@DisplayName("중복이름으로 지하철 노선 생성")
	@Test
	void createLineWithDuplicateName() {
		// given
		지하철_노선_생성("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId());
		// when
		ExtractableResponse<Response> response = 지하철_노선_생성("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId());
		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * Given 새로운 지하철 노선 생성을 요청 하고
	 * When 지하철 노선 목록 조회를 요청 하면
	 * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
	 */
	@DisplayName("지하철 노선 목록 조회")
	@Test
	void getLines() {
		// given
		지하철_노선_생성("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId());
		지하철_노선_생성("5호선", "bg-purple-600", 방화역.getId(), 하남검단산역.getId());

		// when
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().get("/lines")
			.then().log().all().extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		List<String> lineNames = response.jsonPath().getList(".", LineResponse.class)
			.stream().map(LineResponse::getName)
			.collect(Collectors.toList());
		assertThat(lineNames).containsAll(Arrays.asList("신분당선", "5호선"));
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 생성한 지하철 노선 조회를 요청 하면
	 * Then 생성한 지하철 노선을 응답받는다
	 */
	@DisplayName("지하철 노선 조회")
	@Test
	void getLine() {
		// given
		ExtractableResponse<Response> createResponse = 지하철_노선_생성("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId());
		LineResponse createLine = createResponse.jsonPath().getObject(".", LineResponse.class);

		// when
		ExtractableResponse<Response> response = 지하철_노선_조회(createLine.getId());

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);
		assertThat(lineResponse.getName()).isEqualTo("신분당선");

		List<Long> sections = lineResponse.getSections().stream()
			.map(SectionResponse::getId)
			.collect(Collectors.toList());
		assertThat(sections).containsAll(Arrays.asList(강남역.getId(), 정자역.getId()));
	}

	@DisplayName("지하철 노선의 구간 조회")
	@Test
	void getLineSection() {
		ExtractableResponse<Response> createResponse = 지하철_노선_생성("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId());
		LineResponse createLine = createResponse.jsonPath().getObject(".", LineResponse.class);

		// when
		ExtractableResponse<Response> response = 지하철_노선_조회(createLine.getId());
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 지하철 노선의 정보 수정을 요청 하면
	 * Then 지하철 노선의 정보 수정은 성공한다.
	 */
	@DisplayName("지하철 노선 수정")
	@Test
	void updateLine() {
		// given
		ExtractableResponse<Response> createResponse = 지하철_노선_생성("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId());
		LineResponse createLine = createResponse.jsonPath().getObject(".", LineResponse.class);

		Map<String, String> params = new HashMap<>();
		params.put("name", "구분당선");
		params.put("color", "bg-blue-600");

		// when
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put("/lines/{id}", createLine.getId())
			.then().log().all().extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		ExtractableResponse<Response> selectResponse = 지하철_노선_조회(createLine.getId());

		LineResponse lineResponse = selectResponse.jsonPath().getObject(".", LineResponse.class);
		assertThat(lineResponse.getName()).isEqualTo("구분당선");
		assertThat(lineResponse.getColor()).isEqualTo("bg-blue-600");
	}

	/**
	 * Given 지하철 노선 생성을 요청 하고
	 * When 생성한 지하철 노선 삭제를 요청 하면
	 * Then 생성한 지하철 노선 삭제가 성공한다.
	 */
	@DisplayName("지하철 노선 삭제")
	@Test
	void deleteLine() {
		// given
		ExtractableResponse<Response> createResponse = 지하철_노선_생성("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId());
		LineResponse lineResponse = createResponse.jsonPath().getObject(".", LineResponse.class);

		// when
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().delete("/lines/{id}", lineResponse.getId())
			.then().log().all().extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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

	private ExtractableResponse<Response> 지하철_노선_조회(Long id) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/lines/{id}", id)
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
