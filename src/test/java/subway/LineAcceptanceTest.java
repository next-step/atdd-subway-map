package subway;

import static org.assertj.core.api.Assertions.*;
import static subway.location.enums.Location.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.fixture.LineFixture;
import subway.line.LineResponse;
import subway.line.LineUpdateRequest;
import subway.rest.Rest;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {
	@LocalServerPort
	private int port;

	@BeforeEach
	public void beforeEach() {
		RestAssured.port = port;
	}

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void saveLine() {
		// when
		ExtractableResponse<Response> savedLine = LineFixture.init()
			.build()
			.actionReturnExtractableResponse();
		LineResponse actualResponse = savedLine.as(LineResponse.class);

		String uri = LINES.addPath(actualResponse.getId());
		LineResponse expectedResponse = Rest.builder().get(uri).as(LineResponse.class);

		// then
		assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철 노선 2개를 생성하고, 생성된 목록을 조회한다.")
	@Test
	void createLineAndRetrieveLines() {
		// given
		LineFixture lineFixture1 = LineFixture.init().build();
		LineFixture lineFixture2 = LineFixture.init().upStationName("남강역").downStationName("재양역").build();
		LineResponse expectedLineResponse1 = lineFixture1.actionReturnLineResponse();
		LineResponse expectedLineResponse2 = lineFixture2.actionReturnLineResponse();

		// when
		List<LineResponse> actualResponse = Rest.builder()
			.get(LINES.path())
			.jsonPath()
			.getList("", LineResponse.class);

		// then
		assertThat(actualResponse.get(0)).usingRecursiveComparison().isEqualTo(expectedLineResponse1);
		assertThat(actualResponse.get(1)).usingRecursiveComparison().isEqualTo(expectedLineResponse2);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@DisplayName("생성된 노선을 조회한다.")
	@Test
	void createLineAndRetrieveLine() {
		// given
		LineResponse expectedResponse = LineFixture.init()
			.build()
			.actionReturnLineResponse();

		// when
		String uri = LINES.addPath(expectedResponse.getId());
		ExtractableResponse<Response> extractableResponse = Rest.builder().get(uri);
		LineResponse actualResponse = extractableResponse.as(LineResponse.class);

		// then
		assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@DisplayName("지하철 노선을 생성하고, 생성된 노선을 수정한다.")
	@Test
	void updateLine() {
		// given
		String expectedName = "변경된 이름";
		String expectedColor = "변경된 색깔";
		LineResponse lineResponse = LineFixture.init()
			.build()
			.actionReturnLineResponse();

		// when
		LineUpdateRequest request = new LineUpdateRequest(expectedName, expectedColor);

		String uri = LINES.addPath(lineResponse.getId());
		Rest.builder().uri(uri).body(request).put();
		LineResponse actualResponse = Rest.builder().get(uri).as(LineResponse.class);

		// then
		assertThat(actualResponse.getName()).isEqualTo(expectedName);
		assertThat(actualResponse.getColor()).isEqualTo(expectedColor);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@DisplayName("지하철 노선을 생성하고, 삭제한다.")
	@Test
	void deleteLine() {
		// given
		LineResponse lineResponse = LineFixture.init()
			.build()
			.actionReturnLineResponse();

		// when
		String uri = LINES.addPath(lineResponse.getId());
		Rest.builder().delete(uri);

		// then
		List<LineResponse> actualResponse = Rest.builder()
			.get(LINES.path())
			.jsonPath()
			.getList("", LineResponse.class);

		assertThat(actualResponse).isEmpty();
	}
}
