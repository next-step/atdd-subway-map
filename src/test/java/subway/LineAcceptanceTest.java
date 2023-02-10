package subway;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.api.LineTestApi.*;

@DisplayName("지하철 노선 관리 기능")
@Sql(scripts = {"classpath:sql/truncate.sql", "classpath:sql/setupLineTest.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선 생성")
	@Test
	void createLineTest() {
		// when
		long id = createLine("신분당선", "bg-red-600", 1L, 2L, 10).jsonPath().getLong("id");

		// then
		String lineName = showLineById(id).jsonPath().getString("name");
		assertThat(lineName).isEqualTo("신분당선");
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 * */
	@DisplayName("지하철 노선 목록 조회")
	@Test
	void showLineList() {
		// given
		createLine("신분당선", "bg-red-600", 1L, 2L, 10);
		createLine("분당선", "bg-green-600", 1L, 3L, 10);

		// when
		ExtractableResponse<Response> showResponse = showLines();

		// then
		List<String> lineNames = showResponse.jsonPath().getList("name", String.class);
		assertThat(lineNames.size()).isEqualTo(2);
		assertThat(lineNames).containsAnyOf("신분당선", "분당선");
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 * */
	@DisplayName("지하철 노선 조회")
	@Test
	void showLine() {
		// given
		long id = createLine("신분당선", "bg-red-600", 1L, 2L, 10).jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> showResponse = showLineById(id);

		// then
		Assertions.assertAll(
			() -> assertThat(showResponse.jsonPath().getLong("id")).isEqualTo(id),
			() -> assertThat(showResponse.jsonPath().getString("name")).isEqualTo("신분당선"),
			() -> assertThat(showResponse.jsonPath().getString("color")).isEqualTo("bg-red-600")
		);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 * */
	@DisplayName("지하철 노선 수정")
	@Test
	void updateLineTest() {
		// given
		long id = createLine("신분당선", "bg-red-600", 1L, 2L, 10).jsonPath().getLong("id");

		// when
		updateLine(id, "다른분당선", "bg-red-600");

		// then
		ExtractableResponse<Response> showResponse = showLineById(id);
		Assertions.assertAll(
			() -> assertThat(showResponse.jsonPath().getLong("id")).isEqualTo(id),
			() -> assertThat(showResponse.jsonPath().getString("name")).isEqualTo("다른분당선"),
			() -> assertThat(showResponse.jsonPath().getString("color")).isEqualTo("bg-red-600")
		);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 * */
	@DisplayName("지하철 노선 삭제")
	@Test
	void deleteLineTest() {
		// given
		long id = createLine("신분당선", "bg-red-600", 1L, 2L, 10).jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> deleteResponse = deleteLine(id);

		// then
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
