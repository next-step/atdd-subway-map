package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.LineUpdateRequest;
import org.junit.jupiter.api.*;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LineAcceptanceTest extends AcceptanceTest {
	@LocalServerPort
	int port;

	Long stationId1;
	Long stationId2;
	Long stationId3;
	Long stationId4;
	Long stationId5;

	@BeforeEach
	public void beforeEach() {
		RestAssured.port = port;
	}

	@BeforeAll
	public void beforeAll() {
		RestAssured.port = port;

		stationId1 = createStation("양재역").jsonPath().getLong("id");
		stationId2 = createStation("양재시민의숲역").jsonPath().getLong("id");
		stationId3 = createStation("청계산입구역").jsonPath().getLong("id");
		stationId4 = createStation("사당역").jsonPath().getLong("id");
		stationId5 = createStation("방배역").jsonPath().getLong("id");
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@Test
	@Sql(scripts = "/sql/truncate_line_table.sql")
	void 지하철노선을_생성한다() {
		//when
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", stationId1, stationId2, 10);
		ExtractableResponse<Response> response = post("/lines", lineRequest);
		String location = response.header("Location");


		//then
		Integer id = response.jsonPath().getInt("id");
		String name = response.jsonPath().get("name");
		List<String> stations = response.jsonPath().getList("stations");
		assertThat(id).isNotNull();
		assertThat(name).isEqualTo("신분당선");
		assertThat(stations).isNotEmpty();
		assertThat(location).matches("^/lines/[0-9]+");
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@Sql(scripts = "/sql/truncate_line_table.sql")
	@Test
	void 지하철노선_목록을_조회한다() {
		//given
		createLine("신분당선", "bg-red-600", stationId1, stationId2, 10);
		createLine("2호선", "bg-greed-600", stationId4, stationId5, 10);

		//when
		ExtractableResponse<Response> response = get("/lines");

		//then
		assertThat(response.jsonPath().getList("")).hasSize(2);
		assertThat(response.jsonPath().getList("name")).contains("신분당선");
		assertThat(response.jsonPath().getList("[0].stations.name")).contains("양재역");
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@Sql(scripts = "/sql/truncate_line_table.sql")
	@Test
	void 지하철노선을_조회한다() {
		//given
		long id = createLine("신분당선", "bg-red-600", stationId1, stationId2, 10).jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> response = get("/lines/" + id);
		String name = response.jsonPath().get("name");

		//then
		assertThat(name).isEqualTo("신분당선");
		assertThat(response.jsonPath().getList("stations")).hasSize(2);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@Test
	@Sql(scripts = "/sql/truncate_line_table.sql")
	void 지하철노선을_업데이트한다() {
		//given
		long id = createLine("신분당선", "bg-red-600", stationId1, stationId2, 10).jsonPath().getLong("id");
		LineUpdateRequest lineRequest = new LineUpdateRequest("분당선", "bg-blue-600");

		//when
		ExtractableResponse<Response> response = put("/lines/" + id, lineRequest);

		//then
		assertThat(response.response().getStatusCode()).isEqualTo(HttpStatus.OK.value());

		ExtractableResponse<Response> lineResponse = get("/lines/" + id);
		String name = lineResponse.jsonPath().get("name");
		String color = lineResponse.jsonPath().get("color");
		assertThat(name).isEqualTo("분당선");
		assertThat(color).isEqualTo("bg-blue-600");

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

		return post("/stations", params);
	}

	ExtractableResponse<Response> createLine(String name, String color, Long upStationId, Long downStationId, Integer distance) {
		LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);
		return post("/lines", lineRequest);
	}
}
