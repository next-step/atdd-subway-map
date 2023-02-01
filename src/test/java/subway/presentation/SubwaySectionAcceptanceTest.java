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
	// Then 지하철 노선 목록 조회 시 하행 종점역이 변경됨을 확인할 수 있다
	@DisplayName("구간등록 요청시 지하철 노선을 조회하면 종점역 변경을 확인할 수 있다")
	@Test
	void 구간등록_요청시_지하철_노선목록을_조회하면_종점역_변경을_확인할_수_있다() {
		long subwayLineId = 지하철노선_생성(SubwayLineFixtures.getSubwayLineRequest1())
			.jsonPath().getLong("id");

		SectionRequest.Create createRequest = SectionRequest.Create.builder()
			.upStationId(EXISTING_SUBWAY_LINE_UP_STATION_ID)
			.downStationId(STATION_ID_3)
			.distance(10)
			.build();

		ExtractableResponse<Response> response = 구간등록_요청(subwayLineId, createRequest);

		SubwayLineResponse.LineInfo subwayLineInfo = 지하철노선_조회(subwayLineId).as(new TypeRef<>() {});

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(subwayLineInfo.getStations()).extracting("id")
				.contains(EXISTING_SUBWAY_LINE_UP_STATION_ID, STATION_ID_3)
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
}
