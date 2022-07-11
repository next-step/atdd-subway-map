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
	public static Long upStationId;
	public static Long downStationId;

	@Autowired
	private StationService stationService;

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		upStationId = stationService.saveStation(new StationRequest(UP_STATION_NAME)).getId();
		downStationId = stationService.saveStation(new StationRequest(DOWN_STATION_NAME)).getId();
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
	 */
	@DisplayName("지하철노선을 생성한다.")
	@Test
	void createSubwayLine() {
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
		createSubwayLine(LINE_NAME, LINE_COLOR, upStationId, downStationId, 10);

		Long 인천역 = stationService.saveStation(new StationRequest("인천역")).getId();
		Long 청량리역 = stationService.saveStation(new StationRequest("청량리역")).getId();
		createSubwayLine("수인분당선", "bg-yellow-600", 인천역, 청량리역, 20);

	    //when
		ExtractableResponse<Response> response = findSubwayLines();
		JsonPath responseToJson = response.jsonPath();

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(responseToJson.getList("name")).containsExactly(LINE_NAME, "수인분당선");
	}

	private ExtractableResponse<Response> findSubwayLines() {
		return RestAssured
				.given()
					.log().all()
					.accept(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.get("/lines")
				.then()
					.log().all()
				.extract();
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
		ExtractableResponse<Response> createSubwayLine = createSubwayLine(LINE_NAME, LINE_COLOR, upStationId, downStationId, 10);
		long subwayLineId = createSubwayLine.jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> response = findSubwayLine(subwayLineId);
		JsonPath responseToJson = response.jsonPath();

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(responseToJson.getString("name")).isEqualTo(LINE_NAME);
		assertThat(responseToJson.getList("stations.name")).containsExactly(UP_STATION_NAME, DOWN_STATION_NAME);
	}

	private ExtractableResponse<Response> findSubwayLine(long responseId) {
		return RestAssured
				.given()
					.log().all()
					.accept(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.get("/lines/{id}", responseId)
				.then()
					.log().all()
				.extract();
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
