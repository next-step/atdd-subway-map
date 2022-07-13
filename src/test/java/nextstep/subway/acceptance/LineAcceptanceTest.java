package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineAcceptanceStatic.*;
import static nextstep.subway.acceptance.StationAcceptanceStatic.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.acceptance_infra.AcceptanceTest;

@DisplayName("지하철 노선 인수테스트")
class LineAcceptanceTest extends AcceptanceTest {

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회시 생성한 노선을 찾을수 있다.
	 */
	@DisplayName("지하철 노선 생성 테스트")
	@Test
	void createStationLintTest() {

		//given
		Long 상행종점역_번호 = 지하철역_생성되어_있음(Map.of("name", "정자역"));
		Long 하행종점역_번호 = 지하철역_생성되어_있음(Map.of("name", "판교역"));

		Map<String, Object> 지하철_노선_생성_요청_값 =
			Map.of("name", "신분당선", "color", "bg-red-600", "upStationId", 상행종점역_번호, "downStationId", 하행종점역_번호, "distance", 10);

		//when
		ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청(지하철_노선_생성_요청_값);

		//then
		assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		List<Long> stationsIdList = createLineResponse.jsonPath().getList("stations.id", Long.class);
		assertThat(stationsIdList).contains(상행종점역_번호, 하행종점역_번호);

	}

	/**
	 *
	 * given 2개의 지하철 노선을 생성하고
	 * when 지하철 노선 목록을 조회하면
	 * then 지하철 조선 목록 조회시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철 노선 목록 조회 테스트")
	@Test
	void getAllLinesTest() {

		//given
		Long 신분당선_상행종점역_번호 = 지하철역_생성되어_있음(Map.of("name", "정자역"));
		Long 신분당선_하행종점역_번호 = 지하철역_생성되어_있음(Map.of("name", "판교역"));
		지하철_노선_생성되어_있음("신분당선", "red", 신분당선_상행종점역_번호, 신분당선_하행종점역_번호, 10);

		Long 분당선_상행종점역_번호 = 지하철_생성_요청(Map.of("name", "서현역")).jsonPath().getLong("id");
		Long 분당선_하행종점역_번호 = 지하철_생성_요청(Map.of("name", "이매역")).jsonPath().getLong("id");
		지하철_노선_생성되어_있음("분당선", "yellow", 분당선_상행종점역_번호, 분당선_하행종점역_번호, 10);

		//when
		ExtractableResponse<Response> getAllLineResponse = 지하철_노선_목록_조회();

		//then
		assertThat(getAllLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(getAllLineResponse.jsonPath().getList("[0].stations.id", Long.class)).contains(신분당선_상행종점역_번호, 신분당선_하행종점역_번호);
		assertThat(getAllLineResponse.jsonPath().getList("[1].stations.id", Long.class)).contains(분당선_상행종점역_번호, 분당선_하행종점역_번호);

	}

	/**
	 * given 지하철 노선을 생성하고
	 * when 생성한 지하철 노선 목록을 조회하면
	 * then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@DisplayName("지하철 노선 조회 테스트")
	@Test
	void getLineTest() {

		//given
		Long 신분당선_노선_번호 = 신분당선_노선_생성되어_있음();

		//when
		ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(신분당선_노선_번호);

		//then
		assertThat(getLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	/**
	 * given 지하철 노선을 생성하고
	 * when 생성한 지하철 노선을 삭제하면
	 * then 해당 지하철 노선 정보는 삭제된다.
	 */
	@DisplayName("지하철 노선 삭제 테스트")
	@Test
	void deleteLintTest() {

		//given
		Long 신분당선_노선_번호 = 신분당선_노선_생성되어_있음();

		//when
		ExtractableResponse<Response> deleteLineResponse = 지하철_노선_삭제_요청(신분당선_노선_번호);

		//then
		assertThat(deleteLineResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	/**
	 * given 지하철 노선을 생성하고
	 * when 생성한 지하철 노선을 수정하면
	 * then 해당 지하철 노선 정보는 수정된다.
	 */
	@DisplayName("지하철 노선 수정 테스트")
	@Test
	void updateLineTest() {

		//given
		Long 신분당선_노선_번호 = 신분당선_노선_생성되어_있음();

		Map<String, Object> 노선_수정_요청값 = Map.of("name", "신분당선_수정", "color", "bg_blue_200");

		//when
		ExtractableResponse<Response> updateLineResponse = 지하철_노선_수정_요청(신분당선_노선_번호, 노선_수정_요청값);

		//then
		assertThat(updateLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	/**
	 * given 지하철 역, 노선을 생성하고
	 * when 노선에 새로운 구간을 등록하면
	 * then 노선 조회시 구간이 조회된다.
	 */
	@DisplayName("구간 등록 기능 테스트")
	@Test
	void addSectionTest() {
		//given
		Long 정자역_번호 = 지하철역_생성되어_있음(Map.of("name", "정자역"));
		Long 판교역_번호 = 지하철역_생성되어_있음(Map.of("name", "판교역"));
		Long 신분당선_노선_번호 = 지하철_노선_생성되어_있음("신분당선", "red", 정자역_번호, 판교역_번호, 10);

		Long 양재시민의_숲역_번호 = 지하철역_생성되어_있음(Map.of("name", "양재시민의숲역"));
		Map<String, Object> 구간_등록_요청값 = Map.of("upStationId", 판교역_번호, "downStationId", 양재시민의_숲역_번호, "distance", 10);

		//when
		ExtractableResponse<Response> response = 구간_등록_요청(신분당선_노선_번호, 구간_등록_요청값);
		ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(신분당선_노선_번호);

		//then
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(getLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(getLineResponse.jsonPath().getList("stations.id", Long.class))
				.contains(정자역_번호, 판교역_번호, 양재시민의_숲역_번호)
		);

	}

	@DisplayName("새로운 구간의 상행역이 노선의 하행종점이 아닐경우 에러 발생 테스트")
	@Test
	void upStationDownStationTest() throws Exception {
		//given
		Long 신분당선_노선_번호 = 신분당선_노선_생성되어_있음();

		Long 신사역_번호 = 지하철역_생성되어_있음(Map.of("name", "신사역"));
		Long 논현역_번호 = 지하철역_생성되어_있음(Map.of("name", "논현역"));
		Map<String, Object> 구간_등록_요청값 = Map.of("upStationId", 논현역_번호, "downStationId", 신사역_번호, "distance", 10);

		//when
		ExtractableResponse<Response> response = 구간_등록_요청(신분당선_노선_번호, 구간_등록_요청값);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private Long 신분당선_노선_생성되어_있음() {
		Long 정자역_번호 = 지하철역_생성되어_있음(Map.of("name", "정자역"));
		Long 판교역_번호 = 지하철역_생성되어_있음(Map.of("name", "판교역"));
		return 지하철_노선_생성되어_있음("신분당선", "red", 정자역_번호, 판교역_번호, 10);
	}
}
