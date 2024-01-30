package subway;

import common.RestApiRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(value = "/db/sectionTest.sql")
@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {
	private final RestApiRequest<SectionRequest> sectionApiRequest = new RestApiRequest<>("/lines/{id}/sections");
	private final RestApiRequest<LineRequest> lineApiRequest = new RestApiRequest<>("/lines/{id}");

	/**
	 * When 생성한 지하철 노선의 하행 종점역부터 새로운 구간의 하행역을 등록하면
	 * Then 지하철 노선 조회 시, 새로운 하행 종점역을 확인할 수 있다.
	 */
	@DisplayName("지하철 노선에 구간을 등록한다.")
	@Test
	void createSectionTest() {
		// when
		ExtractableResponse<Response> response = sectionApiRequest.post(new SectionRequest(3L, 2L, 10), 1L);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(lineApiRequest.get(1L).as(LineResponse.class).getStaions().stream()
				.map(StationResponse::getId)).contains(3L);
	}

	/**
	 * Given 지하철 노선에 구간을 등록한다.
	 * When 등록한 구간과 같은 구간을 등록하면
	 * Then 등록되지 않고 코드값 500 (Internal sever Error) 을 반환한다.
	 */
	@DisplayName("지하철 노선에 등록되어 있는 역을 등록하면 실패한다.")
	@DirtiesContext
	@Test
	void createSectionWithRegisteredStationThenFailTest() {
		// given
		sectionApiRequest.post(new SectionRequest(4L, 2L, 10), 1L);

		// when & then
		ExtractableResponse<Response> response = sectionApiRequest.post(new SectionRequest(4L, 2L, 10), 1L);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	/**
	 * Given 지하철 노선에 구간을 등록한다.
	 * When 상행역이 해당 노선의 하행 종점역이 아닌 구간을 등록하면
	 * Then 등록되지 않고 코드값 500 (Internal sever Error) 을 반환한다.
	 */
	@DisplayName("하행 종점역이 상행역이 아닌 구간을 등록하면 실패한다.")
	@DirtiesContext
	@Test
	void createSectionWithUpStationIsNotEndStationThenFailTest() {
		// given
		sectionApiRequest.post(new SectionRequest(4L, 2L, 10), 1L);

		// when
		ExtractableResponse<Response> response = sectionApiRequest.post(new SectionRequest(5L, 3L, 10), 1L);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	/**
	 * Given 지하철 노선에 구간을 생성한다.
	 * When 해당 구간을 삭제하면
	 * Then 구간 목록 조회 시, 생성한 구간을 찾을 수 없다.
	 */
	@DisplayName("지하철 노선의 구간을 제거 한다.")
	@DirtiesContext
	@Test
	void deleteSectionTest() {
		// given
		sectionApiRequest.post(new SectionRequest(4L, 2L, 10), 1L);

		// when
		ExtractableResponse<Response> response = sectionApiRequest.delete(Map.of("stationId", 2L), 1L);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		assertThat(sectionApiRequest.get(1L).jsonPath().getString("downStationId")).doesNotContain("2");
	}
}
