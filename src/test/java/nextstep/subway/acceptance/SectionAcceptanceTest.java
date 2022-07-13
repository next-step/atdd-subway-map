package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.DatabaseCleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.utils.StationUtils.지하철역을_등록한다;
import static nextstep.subway.acceptance.utils.SubwayLineUtils.*;
import static nextstep.subway.acceptance.utils.SubwayLineUtils.지하철_노선을_등록한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("지하철 구간 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

	@LocalServerPort
	int port;

	@Autowired
	DatabaseCleanup databaseCleanup;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		databaseCleanup.execute();
	}

	/**
	 * GIVEN 역 2개를 등록하고 역들을 노선에 등록한다.
	 * GIVEN 새로운 역 1개를 등록한다.
	 * WHEN 등록된 노선에 지하철 구간을 등록하면
	 * THEN 지하철 노선 조회시 하행선이 변경된 것을 알 수 있다.
	 */
	@DisplayName("구간을 등록한다")
	@Test
	void registerSection() {
		//given
		long upStationId = 지하철역을_등록한다("광교역").jsonPath().getLong("id");
		long downStationId = 지하철역을_등록한다("광교중앙역").jsonPath().getLong("id");
		long subwayLineId = 지하철_노선을_등록한다("신분당선", "bg-red-600", upStationId, downStationId, 5).jsonPath().getLong("id");

		//given
		long otherStationId = 지하철역을_등록한다("상현역").jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> response = 지하철_구간을_등록한다(subwayLineId, downStationId, otherStationId, 5);

		//then
		Assertions.assertAll(
				() ->assertThat(response.statusCode()).isEqualTo(OK.value()),
				() -> {
					List<String> stationList = 지하철_노선_하나를_조회한다(subwayLineId).jsonPath().getList("stations.name");
					assertThat(stationList).containsExactly("광교역", "상현역");
				}
		);
	}

	private ExtractableResponse<Response> 지하철_구간을_등록한다(Long subwayLineId, Long upStationId, Long downStationId, Integer distance) {
		Map<String, Object> params = new HashMap<>();
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);
		params.put("distance", distance);

		return RestAssured
				.given()
					.log().all()
					.body(params)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.post("/lines/{subwayLineId}/sections", subwayLineId)
				.then()
					.log().all()
				.extract();
	}
}
