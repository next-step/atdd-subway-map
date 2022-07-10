package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.line.dto.LineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
	@LocalServerPort
	int port;

	@BeforeEach
	public void beforeEach() {
		RestAssured.port = port;
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철노선을 생성한다.")
	@Transactional
	@Test
	void createLine() {
		//given
		Long upStationId = createStation("양재역").jsonPath().getLong("id");
		Long downStationId = createStation("양재시민의숲역").jsonPath().getLong("id");

		//when
		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", upStationId, downStationId, 10);
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

		return post("/stations", params);
	}
}
