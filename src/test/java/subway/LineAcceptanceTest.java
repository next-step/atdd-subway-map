package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.response.LineAcceptanceTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static subway.response.LineAcceptanceTestUtils.*;
import static subway.response.StationAcceptanceTestUtils.createStation;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest{
	/**
	 * Given 지하철역 생성을 요청 하고
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();

		createStation("지하철역");
		createStation("새로운 지하철역");
	}
	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void addLine() {
		//when
		Map<String, Object> line1 = createLine("신분당선", "bg-red-600", 1, 2, 10);

		ExtractableResponse<Response> createResponse = createLineResponse(line1);

		// then
		assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		ExtractableResponse<Response> response = getLinesResponse();
		List<String> lineNames = response.jsonPath().getList("name", String.class);

		assertThat(lineNames).containsAnyOf("신분당선");
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철 노선목록을 조회한다.")
	@Test
	void showLines() {
		//given
		createStation("또 다른 지하철역");
		Map<String, Object> line1 = createLine("신분당선", "bg-red-600", 1, 2, 10);
		createLineResponse(line1);
		Map<String, Object> line2 = createLine("분당선", "bg-red-600", 1, 3, 10);
		createLineResponse(line2);

		//when
		ExtractableResponse<Response> response = LineAcceptanceTestUtils.getLinesResponse();
		List<String> lineNames = response.jsonPath().getList("name", String.class);
		//then
		assertAll(
				() -> assertThat(lineNames).containsExactly("신분당선", "분당선"),
				() -> assertEquals(lineNames.size(), 2)
		);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@DisplayName("지하철 노선을 조회한다.")
	@Test
	void showLine() {
		//given
		Map<String, Object> line1 = createLine("신분당선", "bg-red-600", 1, 2, 10);
		ExtractableResponse<Response> lineResponse = createLineResponse(line1);
		long id = lineResponse.response().jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> response = getLineResponse(id);
		String lineName = response.jsonPath().getString("name");

		//then
		assertAll(
				() -> assertThat(lineName).isEqualTo("신분당선")
		);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void updateLine() {
		//given
		Map<String, Object> line1 = createLine("신분당선", "bg-red-600", 1, 2, 10);
		ExtractableResponse<Response> lineResponse = createLineResponse(line1);
		long id = lineResponse.response().jsonPath().getLong("id");

		//when
		Map<String, String> updateInfoParam = new HashMap<>();
		updateInfoParam.put("name", "다른분당선");
		updateInfoParam.put("color", "bg-red-600");
		ExtractableResponse<Response> updatedResponse = updateLineResponse(updateInfoParam, id);

		assertThat(updatedResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		ExtractableResponse<Response> response = getLineResponse(id);
		String lineName = response.jsonPath().getString("name");
		String lineColor = response.jsonPath().getString("color");

		//then
		assertAll(
				() -> assertThat(id).isEqualTo(1),
				() -> assertThat(lineName).isEqualTo("다른분당선"),
				() -> assertThat(lineColor).isEqualTo("bg-red-600")
		);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@DisplayName("지하철 노선을 삭제한다.")
	@Test
	void deleteLine() {
		//given
		Map<String, Object> line1 = createLine("신분당선", "bg-red-600", 1, 2, 10);

		ExtractableResponse<Response> lineResponse = createLineResponse(line1);
		long id = lineResponse.response().jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> response = deleteLineResponse(id);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}




}
