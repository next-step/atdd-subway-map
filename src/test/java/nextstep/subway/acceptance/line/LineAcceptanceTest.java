package nextstep.subway.acceptance.line;

import static nextstep.subway.acceptance.line.LineProvider.*;
import static nextstep.subway.acceptance.station.StationProvider.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.isolation.TestIsolationUtil;

@DisplayName("노선 관련 기능")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

	private static final String 신분당선 = "신분당선";
	private static final String 신분당선_COLOR = "bg-red-600";

	private static final String 분당선 = "분당선";
	private static final String 분당선_COLOR = "bg-green-600";

	private static final String 다른_분당선 = "다른_분당선";
	private static final String 다른_분당선_COLOR = "다른_분당선_COLOR";

	private static final String 지하철역_A = "지하철역_A";
	private static final String 지하철역_B = "지하철역_B";
	private static final String 지하철역_C = "지하철역_C";

	@LocalServerPort
	int port;

	@Autowired
	TestIsolationUtil testIsolationUtil;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		testIsolationUtil.clean();
	}

	/*
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@Test
	void 지하철_노선_생성() {
		// When 지하철 노선을 생성하면
		String 지하철역_A_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_A));
		String 지하철역_B_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_B));

		지하철_노선_생성_성공(신분당선, 신분당선_COLOR, 지하철역_A_ID, 지하철역_B_ID, 10);

		// Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
		assertThat(노선_이름_추출(지하철_노선_목록_조회_성공())).containsAnyOf(신분당선);
	}

	/*
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@Test
	void 지하철노선_목록_조회() {
		// Given 2개의 지하철 노선을 생성하고
		String 지하철역_A_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_A));
		String 지하철역_B_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_B));
		String 지하철역_C_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_C));

		지하철_노선_생성_성공(신분당선, 신분당선_COLOR, 지하철역_A_ID, 지하철역_B_ID, 10);
		지하철_노선_생성_성공(분당선, 분당선_COLOR, 지하철역_A_ID, 지하철역_C_ID, 20);

		// When 지하철 노선 목록을 조회하면
		ExtractableResponse<Response> 지하철_노선_목록 = 지하철_노선_목록_조회_성공();

		// Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
		assertThat(노선_이름_목록_추출(지하철_노선_목록)).containsExactly(신분당선, 분당선);
	}

	/*
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@Test
	void 지하철_노선_조회() {
		// Given 지하철 노선을 생성하고
		String 지하철역_A_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_A));
		String 지하철역_B_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_B));
		String 지하철_노선_Id = 지하철_노선_Id_추출(지하철_노선_생성_성공(신분당선, 신분당선_COLOR, 지하철역_A_ID, 지하철역_B_ID, 10));

		// When 생성한 지하철 노선을 조회하면
		ExtractableResponse<Response> response = 지하철_노선_조회_성공(지하철_노선_Id);

		// Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
		assertThat(노선_이름_추출(response)).isEqualTo(신분당선);
	}

	/*
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@Test
	void 지하철_노선_수정() {
		// Given 지하철 노선을 생성하고
		String 지하철역_A_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_A));
		String 지하철역_B_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_B));
		String 지하철_노선_Id = 지하철_노선_Id_추출(지하철_노선_생성_성공(신분당선, 신분당선_COLOR, 지하철역_A_ID, 지하철역_B_ID, 10));

		// When 생성한 지하철 노선을 수정하면
		지하철_노선_수정_성공(지하철_노선_Id, 다른_분당선, 다른_분당선_COLOR);

		// Then 해당 지하철 노선 정보는 수정된다
		ExtractableResponse<Response> response = 지하철_노선_조회_성공(지하철_노선_Id);
		assertThat(노선_이름_추출(response)).isEqualTo(다른_분당선);
		assertThat(노선_COLOR_추출(response)).isEqualTo(다른_분당선_COLOR);
	}

	/*
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@Test
	void 지하철_노선_삭제() {
		// Given 지하철 노선을 생성하고
		String 지하철역_A_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_A));
		String 지하철역_B_ID = 지하철역_Id_추출(지하철역_생성_성공(지하철역_B));
		String 지하철_노선_Id = 지하철_노선_Id_추출(지하철_노선_생성_성공(신분당선, 신분당선_COLOR, 지하철역_A_ID, 지하철역_B_ID, 10));

		// When 생성한 지하철 노선을 삭제하면
		지하철_노선_제거_성공(지하철_노선_Id);

		// Then 해당 지하철 노선 정보는 삭제된다
		ExtractableResponse<Response> response = 지하철_노선_목록_조회_성공();
		assertThat(노선_이름_목록_추출(response)).doesNotContain(신분당선);
	}
}
