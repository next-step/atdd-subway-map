package nextstep.subway.line.acceptance;

import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineStationAddAcceptanceTest extends AcceptanceTest {

	private ExtractableResponse<Response> createdLineResponse;
	private ExtractableResponse<Response> createdStationResponse;

	@BeforeEach
	public void setUp() {
		createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
		createdStationResponse = 지하철역_등록되어_있음("강남역");
	}


	@DisplayName("지하철 노선에 역을 등록한다.")
	@Test
	void addLineStation() {
		// when
		// 지하철_노선에_지하철역_등록_요청
		Long lineId = createdLineResponse.as(LineResponse.class).getId();
		Long stationId = createdStationResponse.as(StationResponse.class).getId();
		Map<String, String> params = new HashMap<>();
		params.put("preStationId", "");
		params.put("stationId", stationId + "");
		params.put("distance", "4");
		params.put("duration", "2");

		ExtractableResponse<Response> response = RestAssured.given().log().all().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(params).
			when().
			post("/lines/{lineId}/stations", lineId).
			then().
			log().all().
			extract();

		// then
		// 지하철 노선에 지하철역 등록됨
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	@DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
	@Test
	void getLineWithStations() {
		// 지하철_노선에_지하철역_등록되어_있음
		Long lineId = createdLineResponse.as(LineResponse.class).getId();
		Long stationId = createdStationResponse.as(StationResponse.class).getId();
		노선에_지하철역_첫번째_등록(stationId, lineId);

		// when
		// 지하철_노선_상세정보_조회_요청
		ExtractableResponse<Response> response = RestAssured.given().log().all().
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			get("/lines/{lineId}", lineId).
			then().
			log().all().
			extract();

		// then
		// 지하철 노선 상세정보 응답됨
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		LineResponse lineResponse = response.as(LineResponse.class);
		assertThat(lineResponse).isNotNull();
		assertThat(lineResponse.getStations()).hasSize(1);
	}

	@DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
	@Test
	void addLineStationInOrder() {
		// given
		ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
		ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

		// when
		// 지하철_노선에_지하철역_등록_요청
		Long lineId = createdLineResponse.as(LineResponse.class).getId();
		Long stationId1 = createdStationResponse.as(StationResponse.class).getId();
		ExtractableResponse<Response> lineStationResponse = 노선에_지하철역_첫번째_등록(stationId1, lineId);

		// 지하철_노선에_지하철역_등록_요청
		Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();
		Map<String, String> params2 = new HashMap<>();
		params2.put("preStationId", stationId1 + "");
		params2.put("stationId", stationId2 + "");
		params2.put("distance", "4");
		params2.put("duration", "2");

		RestAssured.given().log().all().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(params2).
			when().
			post("/lines/{lineId}/stations", lineId).
			then().
			log().all().
			extract();

		// 지하철_노선에_지하철역_등록_요청
		Long stationId3 = createdStationResponse3.as(StationResponse.class).getId();
		Map<String, String> params3 = new HashMap<>();
		params3.put("preStationId", stationId2 + "");
		params3.put("stationId", stationId3 + "");
		params3.put("distance", "4");
		params3.put("duration", "2");

		RestAssured.given().log().all().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(params3).
			when().
			post("/lines/{lineId}/stations", lineId).
			then().
			log().all().
			extract();

		// then
		assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// when
		// 지하철_노선_조회_요청
		ExtractableResponse<Response> response = RestAssured.given().log().all().
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			get("/lines/{lineId}", lineId).
			then().
			log().all().
			extract();

		LineResponse lineResponse = response.as(LineResponse.class);
		assertThat(lineResponse).isNotNull();
		assertThat(lineResponse.getStations()).hasSize(3);
		List<Long> stationIds = lineResponse.getStations().stream()
			.map(it -> it.getStation().getId())
			.collect(Collectors.toList());
		assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 2L, 3L));
	}

	@DisplayName("지하철 노선에 역을 마지막에 등록한다")
	@Test
	void 지하철_노선_마지막에_역을_등록한다() {
		// given
		// 지하철 노선에 지하철역이 등록되어 있다.
		ExtractableResponse<Response> createdStationResponseLast = 지하철역_등록되어_있음("역삼역");

		Long lineId = createdLineResponse.as(LineResponse.class).getId();
		Long givenStationId = createdStationResponse.as(StationResponse.class).getId();
		Long lastStationId = createdStationResponseLast.as(StationResponse.class).getId();

		노선에_지하철역_첫번째_등록(givenStationId, lineId);

		// when
		// 지하철 노선의 마지막에 지하철역을 등록 요청한다.
		Map<String, String> params = new HashMap<>();
		params.put("preStationId", givenStationId + "");
		params.put("stationId", lastStationId + "");
		params.put("distance", "4");
		params.put("duration", "2");

		ExtractableResponse<Response> lineStationRegisterRequest = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when()
			.post("/lines/{lineId}/stations", lineId)
			.then()
			.log()
			.all()
			.extract();

		// then
		// 지하철 노선에 지하철역 등록됨
		assertThat(lineStationRegisterRequest.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// when
		// 지하철 노선 상세정보 조회를 요청한다.
		ExtractableResponse<Response> lineInformationResponse = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/lines/{lineId}", lineId)
			.then()
			.log()
			.all()
			.extract();

		// then
		// 지하철 노선 상세정보가 응답된다.
		LineResponse lineResponse = lineInformationResponse.as(LineResponse.class);
		assertThat(lineResponse).isNotNull();
		assertThat(lineResponse.getStations()).hasSize(2);

		// and
		// 등록된 지하철역이 마지막에 위치된다.
		assertThat(lineResponse.getStations()
			.get(0).getStation().getId()).isEqualTo(givenStationId);
		assertThat(lineResponse.getStations()
			.get(1).getStation().getId()).isEqualTo(lastStationId);
	}

	@DisplayName("지하철 노선에 역을 중간에 등록한다.")
	@CsvSource({"역삼역, 1", "선릉역, 2"})
	@ParameterizedTest
	void 지하철_노선_중간에_역을_등록한다(String stationName, Long preStationId) {
		// given
		// 지하철 노선에 지하철역이 등록되어 있다.
		// 노선에 첫 번째로 지하철역을 등록한다.
		ExtractableResponse<Response> secondStationRegisterRequest = 지하철역_등록되어_있음("역삼역");
		ExtractableResponse<Response> lastStationRegisterRequest = 지하철역_등록되어_있음("선릉역");

		Long lineId = createdLineResponse.as(LineResponse.class).getId();
		Long firstStationId = createdStationResponse.as(StationResponse.class).getId();
		Long secondStationId = secondStationRegisterRequest.as(StationResponse.class).getId();
		Long lastStationId = lastStationRegisterRequest.as(StationResponse.class).getId();

		노선에_지하철역_첫번째_등록(firstStationId, lineId);
		노선에_지하철역_추가로_등록(firstStationId, lastStationId, lineId);

		// when
		// 지하철 노선의 중간에 지하철역 등록을 요청한다.
		ExtractableResponse<Response> secondLineStationRegisterRequest =
			노선에_지하철역_추가로_등록(firstStationId, secondStationId, lineId);

		// then
		// 지하철 노선에 지하철역이 등록된다.
		assertThat(secondLineStationRegisterRequest.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// when
		// 지하철 노선의 상세정보 조회를 요청한다.
		ExtractableResponse<Response> lineResponseRequest = 노선정보_확인_요청(lineId);

		// then
		// 지하철 노선 상세정보를 응답한다.
		LineResponse lineResponse = lineResponseRequest.as(LineResponse.class);
		assertThat(lineResponse).isNotNull();
		assertThat(lineResponse.getStations()).hasSize(3);

		// and
		// 등록된 지하철역이 중간에 위치한다.
		assertThat(lineResponse.getStations().stream()
			.filter(station -> station.getStation().getName().equals(stationName))
			.mapToLong(LineStationResponse::getPreStationId))
			.containsExactly(preStationId);
	}

	@DisplayName("이미 특정 호선에 등록되어 있는 역을 중복으로 등록하는 경우 오류를 반환한다.")
	@Test
	void 이미_등록되어_있는_역을_등록할_수_없다() {
		//
	}

	@DisplayName("특정 호선에 등록하고자 하는 역이 실제로 존재하는 역이지 안흐면 오류를 반환한다.")
	void 존재하지_않는_역을_등록한다() {
		//
	}

	private ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);
		params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_TIME));
		params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
		params.put("intervalTime", "5");

		return RestAssured.given().log().all().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(params).
			when().
			post("/lines").
			then().
			log().all().
			extract();
	}

	public static ExtractableResponse<Response> 노선에_지하철역_첫번째_등록(Long givenStationId, Long lineId) {
		Map<String, String> givenParams = new HashMap<>();
		givenParams.put("preStationId", "");
		givenParams.put("stationId", givenStationId + "");
		givenParams.put("distance", "4");
		givenParams.put("duration", "2");

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(givenParams)
			.when()
			.post("/lines/{lineId}/stations", lineId)
			.then()
			.log()
			.all()
			.extract();
	}

	public static ExtractableResponse<Response> 노선에_지하철역_추가로_등록(Long preStationId, Long givenStationId, Long lineId) {
		Map<String, String> params = new HashMap<>();
		params.put("preStationId", preStationId + "");
		params.put("stationId", givenStationId + "");
		params.put("distance", "4");
		params.put("duration", "2");

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when()
			.post("/lines/{lineId}/stations", lineId)
			.then()
			.log()
			.all()
			.extract();
	}

	public static ExtractableResponse<Response> 노선정보_확인_요청(Long lineId) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/lines/{lineId}", lineId)
			.then()
			.log()
			.all()
			.extract();
	}
}
