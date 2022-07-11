package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.station.StationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubwayLineTest {

	public static final String LINE_NAME = "신분당선";
	public static final String LINE_COLOR = "bg-red-600";
	public static final String UP_STATION_NAME = "광교중앙역";
	public static final String DOWN_STATION_NAME = "신사역";

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Autowired
	private StationService stationService;

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
	 */
	@DisplayName("지하철노선을 생성한다.")
	@Test
	void createSubwayLine() {
		//given
		Long upStationId = stationService.saveStation(new StationRequest(UP_STATION_NAME)).getId();
		Long downStationId = stationService.saveStation(new StationRequest(DOWN_STATION_NAME)).getId();

		//when
		ExtractableResponse<Response> response = createSubwayLine(LINE_NAME, LINE_COLOR, upStationId, downStationId, 10);
		JsonPath responseToJson = response.jsonPath();

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(responseToJson.getLong("id")).isNotNull();
		assertThat(responseToJson.getString("name")).isEqualTo(LINE_NAME);
		assertThat(responseToJson.getString("color")).isEqualTo(LINE_COLOR);
		assertThat(responseToJson.getList("stations.name")).containsExactly(UP_STATION_NAME, DOWN_STATION_NAME);
	}

	private ExtractableResponse<Response> createSubwayLine(String name, String color, Long upStationId, Long downStationId, Integer distance) {

		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("color", color);
		params.put("upStationId", upStationId);
		params.put("downStationId", downStationId);
		params.put("distance", distance);

		return RestAssured
				.given()
					.log().all()
					.body(params)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.post("/lines")
				.then()
					.log().all()
				.extract();
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철노선 목록을 조회한다.")
	@Test
	void findSubwayLineList() {
	    //given

	    //when

	    //then
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@DisplayName("지하철노선을 조회한다.")
	@Test
	void findSubwayLine() {
	    //given

	    //when

	    //then
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@DisplayName("지하철노선을 수정한다.")
	@Test
	void modifySubwayLine() {
	    //given

	    //when

	    //then
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@DisplayName("지하철노선을 삭제한다.")
	@Test
	void deleteSubwayLine() {
	    //given

	    //when

	    //then
	}
}
