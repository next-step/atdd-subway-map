package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.tool.RequestTool;
import nextstep.subway.acceptance.tool.SubwayFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
	@LocalServerPort
	int port;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

	/**
	 * When 지하철역을 생성하면
	 * Then 지하철역이 생성된다
	 * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
	 */
	@DisplayName("지하철역을 생성한다.")
	@Test
	void createStation() {
		// when
		ExtractableResponse<Response> response = SubwayFactory.역_생성("강남역");
		String location = response.header("Location");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(location).matches("^/stations/[0-9]+");

	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	@DisplayName("지하철역을 조회한다.")
	@Test
	void getStations() {
		//given
		SubwayFactory.역_생성("방배역");
		SubwayFactory.역_생성("삼성역");

		//when
		ExtractableResponse<Response> response = RequestTool.get("/stations");
		List<String> stationNames = response.jsonPath().getList("name", String.class);

		//then
		assertThat(stationNames).containsAnyOf("방배역", "삼성역");
	}

	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */
	@DisplayName("지하철역을 제거한다.")
	@Test
	void deleteStation() {
		//given
		Long id = SubwayFactory.역_생성("녹번역").jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> response = RequestTool.delete("/stations/" + id);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		//then
		List<String> stations = RequestTool.get("/stations").response().jsonPath().getList("name", String.class);
		assertThat(stations).doesNotContain("녹번역");
	}
}
