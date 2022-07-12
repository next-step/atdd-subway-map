package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.StationUtils.지하철역을_등록한다;
import static nextstep.subway.acceptance.SubwayLineUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubwayLineTest {

	public static final String LINE_NAME = "신분당선";
	public static final String LINE_COLOR = "bg-red-600";
	public static final String UP_STATION_NAME = "광교중앙역";
	public static final String DOWN_STATION_NAME = "신사역";
	public static Long upStationId;
	public static Long downStationId;
	public static ExtractableResponse<Response> setUpSubwayLine;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		databaseCleanup.execute();
		subwayLineInit();
	}

	private void subwayLineInit() {
		upStationId = 지하철역을_등록한다(UP_STATION_NAME).jsonPath().getLong("id");
		downStationId = 지하철역을_등록한다(DOWN_STATION_NAME).jsonPath().getLong("id");
		setUpSubwayLine = 지하철노선을_등록한다(LINE_NAME, LINE_COLOR, upStationId, downStationId, 10);
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
	 */
	@DisplayName("지하철노선을 생성한다.")
	@Test
	void createSubwayLine() {
		//when
		JsonPath responseToJson = setUpSubwayLine.jsonPath();

		//then
		assertAll(
				() -> assertThat(setUpSubwayLine.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
				() -> assertThat(responseToJson.getLong("id")).isNotNull(),
				() -> assertThat(responseToJson.getString("name")).isEqualTo(LINE_NAME),
				() -> assertThat(responseToJson.getString("color")).isEqualTo(LINE_COLOR),
				() -> assertThat(responseToJson.getList("stations.name")).containsExactly(UP_STATION_NAME, DOWN_STATION_NAME)
		);
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
		Long 인천역 = 지하철역을_등록한다("인천역").jsonPath().getLong("id");
		Long 청량리역 = 지하철역을_등록한다("청량리역").jsonPath().getLong("id");
		지하철노선을_등록한다("수인분당선", "bg-yellow-600", 인천역, 청량리역, 20);

		//when
		ExtractableResponse<Response> response = 지하철노선_목록을_조회한다();
		JsonPath responseToJson = response.jsonPath();

		//then
		assertAll(
				() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
				() -> assertThat(responseToJson.getList("name")).containsExactly(LINE_NAME, "수인분당선"),
				() -> {
					List<String> list = responseToJson.getList("stations.name", String.class);
					assertThat(list).hasSize(2);
					assertThat(list.get(0)).contains("광교중앙역", "신사역");
					assertThat(list.get(1)).contains("인천역", "청량리역");
				}
		);
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
		long subwayLineId = setUpSubwayLine.jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> response = 지하철노선_하나를_조회한다(subwayLineId);
		JsonPath responseToJson = response.jsonPath();

		//then
		assertAll(
				() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
				() -> assertThat(responseToJson.getString("name")).isEqualTo(LINE_NAME),
				() -> assertThat(responseToJson.getList("stations.name")).containsExactly(UP_STATION_NAME, DOWN_STATION_NAME)
		);
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
		long subwayLineId = setUpSubwayLine.jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> modifiedSubwayLine = 지하철노선을_수정한다(subwayLineId, "상현역", "bg-white-600");

		//then
		assertAll(
				() -> assertThat(modifiedSubwayLine.statusCode()).isEqualTo(HttpStatus.OK.value()),
				() -> {
					JsonPath findSubwayLineJson = 지하철노선_하나를_조회한다(subwayLineId).jsonPath();
					assertThat(findSubwayLineJson.getString("name")).isEqualTo("상현역");
					assertThat(findSubwayLineJson.getString("color")).isEqualTo("bg-white-600");
				}
		);
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
		long subwayLineId = setUpSubwayLine.jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> response = 지하철_노선을_삭제한다(subwayLineId);

		//then
		assertAll(
				() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
				() -> assertThat(지하철노선_하나를_조회한다(subwayLineId).jsonPath().getString("name")).isNull()
		);
	}
}
