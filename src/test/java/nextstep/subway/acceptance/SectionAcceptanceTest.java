package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest extends BaseAcceptanceTest {

	@BeforeEach
	public void setUp() {
		지하철_역_생성("논현역");
		지하철_역_생성("신논현역");
		지하철_역_생성("강남역");
		지하철_노선_생성("신분당선", "bg-red-600", 1L, 2L);
	}

	/**
	 * given 역과 노선을 등록하고
	 * when 해당 노선에 구간을 등록한 후 노선을 조회하면
	 * then 노선 안에 등록한 구간이 포함된 것을 확인할 수 있다.
	 */
	@DisplayName("지하철 구간 등록")
	@Test
	void addSection(){
		// when
		지하철_구간_등록(1L, 2L, 4L, 10);
		List<String> stations = 지하철_노선_조회(1L).jsonPath().getList("stations.name");
		// then
		assertThat(stations).containsAll(List.of("논현역", "신논현역", "강남역"));
	}

	/**
	 * given 역과 노선을 등록하고 구간을 등록한 후
	 * when 해당 구간을 삭제한 다음 노선을 조회하면
	 * then 노선 안에 삭제한 구간이 없는 것을 확인할 수 있다.
	 */
	@DisplayName("지하철 구간 삭제")
	@Test
	void deleteSection(){
		//given
		지하철_구간_등록(1L, 2L, 3L, 10);
		List<String> beforeDelete = 지하철_노선_조회(1L).jsonPath().getList("stations.name");
		// when
		지하철_구간_삭제(1L,3L);
		List<String> stations = 지하철_노선_조회(1L).jsonPath().getList("stations.name");
		// then
		assertThat(beforeDelete).containsAll(List.of("논현역", "신논현역", "강남역"));
		assertThat(stations).containsAll(List.of("논현역", "신논현역"));
	}

	private ExtractableResponse<Response> 지하철_구간_삭제(Long lineId, Long stationId) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);

		return RestAssured
				.given().log().all()
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/lines/{lineId}/sections", lineId)
				.then().log().all()
				.extract();
	}

	private ExtractableResponse<Response> 지하철_구간_등록(Long lineId, Long upStationId, Long downStationId, Integer distance) {
		Map<String, Object> params = new HashMap<>();
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);
		params.put("distance", distance);

		return RestAssured
				.given().log().all()
				.body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/lines/{lineId}/sections", lineId)
				.then().log().all()
				.extract();
	}
}
