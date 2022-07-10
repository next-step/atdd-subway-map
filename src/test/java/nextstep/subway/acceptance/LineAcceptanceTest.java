package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.line.dto.LineRequest;
import org.junit.jupiter.api.*;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LineAcceptanceTest extends AcceptanceTest {
	@LocalServerPort
	int port;

	Long stationId1;
	Long stationId2;
	Long stationId3;


	@BeforeEach
	public void beforeEach() {
		RestAssured.port = port;
	}

	@BeforeAll
	public void beforeAll() {
		stationId1 = createStation("양재역").jsonPath().getLong("id");
		stationId2 = createStation("양재시민의숲역").jsonPath().getLong("id");
		stationId3 = createStation("청계산입구역").jsonPath().getLong("id");
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철노선을 생성한다.")
	@Transactional
	@Test
	void createLine() {
		//when
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", stationId1, stationId2, 10);
		ExtractableResponse<Response> response = post("/lines", lineRequest);

		//then
		Integer id = response.jsonPath().getInt("id");
		String name = response.jsonPath().get("name");
		List<String> stations = response.jsonPath().getList("stations");
		assertThat(id).isNotNull();
		assertThat(name).isEqualTo("신분당선");
		assertThat(stations).isNotEmpty();
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철노선을 조회한다.")
	@Transactional
	@Test
	void getLineList() {
		//given
		Long upStationId = createStation("양재역").jsonPath().getLong("id");
		Long downStationId = createStation("양재시민의숲역").jsonPath().getLong("id");
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	void getLine() {

	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	void updateLine() {

	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	void deleteLine() {

	}

	ExtractableResponse<Response> createStation(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);

		return RestAssured.given().port(port).log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/stations")
			.then().log().all().extract();
	}
}
