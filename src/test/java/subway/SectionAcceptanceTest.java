package subway;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.api.LineTestApi;
import subway.api.SectionTestApi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("지하철 구간 관리")
@Sql(scripts = {"classpath:sql/truncate.sql", "classpath:sql/setupSectionTest.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

	LineTestApi lineApi = new LineTestApi();
	SectionTestApi sectionApi = new SectionTestApi();

	/** 구간 등록 기능 **/
	/**
	 * When 지하철 구간을 등록하면
	 * Then 지하철 노선 조회 시 등록한 구간을 찾을 수 있다.
	 * */
	@DisplayName("지하철 노선에 구간을 등록")
	@Test
	void createSection() {
		// when
		long registeredUpStationId = 2L;
		long newDownStationId = 4L;
		long lineId = lineApi.createLine("신분당선", "bg-red-600", 1L, registeredUpStationId, 10).jsonPath().getLong("id");

		sectionApi.createSection(lineId, newDownStationId, registeredUpStationId, 10);

		// then
		ExtractableResponse<Response> showLineResponse = lineApi.showLineById(lineId);
		List<Long> stationIds = showLineResponse.jsonPath().getList("stations.id", Long.class);
		assertThat(stationIds).contains(registeredUpStationId, newDownStationId);
	}

	/**
	 * When 노선에 하행 종점역으로 등록되어 있지 않는 새로운 구간을 등록하면
	 * Then 400에러 발생
	 * Then 지하철 노선 조회 시 등록한 구간을 찾을 수 없다.
	 * */
	@DisplayName("노선에 하행 종점역으로 등록되어 있지 않는 새로운 구간 등록 시 에러 발생")
	@Test
	void newUpStationNotRegisteredAsLastDownStation() {
		// when
		long registeredUpStationId = 2L;
		long newDownStationId = 4L;
		long lineId = lineApi.createLine("신분당선", "bg-red-600", 1L, 3L, 10).jsonPath().getLong("id");

		ExtractableResponse<Response> createResponse = sectionApi.createSection(lineId, newDownStationId, registeredUpStationId, 10);

		// then
		assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// then
		ExtractableResponse<Response> showLineResponse = lineApi.showLineById(lineId);
		List<Long> stationIds = showLineResponse.jsonPath().getList("stations.id", Long.class);
		assertThat(stationIds).doesNotContain(newDownStationId);
	}

	/**
	 * When 노선에 등록되어 있는 역을 새로운 구간으로 등록하면
	 * Then 400에러 발생
	 * Then 지하철 노선 조회 시 등록하려던 역을 찾을 수 있다.
	 * */
	@DisplayName("노선에 등록되어 있는 역을 새로운 구간으로 등록 시 에러 발생")
	@Test
	void newDownStationAlreadyRegistered() {
		// when
		long registeredUpStationId = 2L;
		long newDownStationId = 4L;
		long lineId = lineApi.createLine("신분당선", "bg-red-600", 1L, registeredUpStationId, 10).jsonPath().getLong("id");

		ExtractableResponse<Response> addSectionResponse = sectionApi.createSection(lineId, newDownStationId, registeredUpStationId, 10);
		assertThat(addSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		ExtractableResponse<Response> alreadyRegisteredResponse = sectionApi.createSection(lineId, registeredUpStationId, newDownStationId, 10);
		assertThat(alreadyRegisteredResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// then
		ExtractableResponse<Response> showLineResponse = lineApi.showLineById(lineId);
		List<Long> stationIds = showLineResponse.jsonPath().getList("stations.id", Long.class);
		assertThat(stationIds).contains(1L, registeredUpStationId, newDownStationId);
	}

	/** 구간 제거 기능 **/
	/**
	 * Given 지하철 구간을 등록하고
	 * When 등록한 지하철 구간을 제거하면
	 * Then 지하철 노선 조회 시 구간이 제거된 것을 확인할 수 있다
	 **/
	@DisplayName("등록한 구간을 삭제")
	@Test
	void deleteSection() {
		// given
		long newDownStationId = 4L;
		long lineId = lineApi.createLine("신분당선", "bg-red-600", 1L,  2L, 10).jsonPath().getLong("id");
		ExtractableResponse<Response> addSectionResponse = sectionApi.createSection(lineId, newDownStationId, 2L, 10);
		assertThat(addSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// when
		ExtractableResponse<Response> deleteResponse = sectionApi.deleteSection(lineId, newDownStationId);
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		// then
		ExtractableResponse<Response> showLineResponse = lineApi.showLineById(lineId);
		List<Long> stationIds = showLineResponse.jsonPath().getList("stations.id", Long.class);
		assertThat(stationIds).doesNotContain(newDownStationId);
	}

	/**
	 * Given 지하철 구간을 등록하고
	 * When 중간 구간을 제거하면
	 * Then 400 에러 발생
	 * Then 지하철 노선 조회 시 등록한 구간이 그대로인 것을 확인할 수 있다
	 * */
	@DisplayName("구간 삭제 시 마지막 구간이 아니면 에러 발생")
	@Test
	void deleteLastSection() {
		// given
		long newDownStationId = 4L;
		long lineId = lineApi.createLine("신분당선", "bg-red-600", 1L,  2L, 10).jsonPath().getLong("id");
		ExtractableResponse<Response> addSectionResponse = sectionApi.createSection(lineId, newDownStationId, 2L, 10);
		ExtractableResponse<Response> addSectionResponse2 = sectionApi.createSection(lineId, 3L, newDownStationId, 10);
		assertAll(
			() -> assertThat(addSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(addSectionResponse2.statusCode()).isEqualTo(HttpStatus.CREATED.value())
		);

		// when
		ExtractableResponse<Response> deleteResponse = sectionApi.deleteSection(lineId, newDownStationId);
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// then
		ExtractableResponse<Response> showLineResponse = lineApi.showLineById(lineId);
		List<Long> stationIds = showLineResponse.jsonPath().getList("stations.id", Long.class);
		assertThat(stationIds).contains(1L, 2L, newDownStationId, 3L);
	}

	/**
	 * Given 지하철 노선(구간 1개)을 등록하고
	 * When 등록한 구간을 삭제하면
	 * Then 400 에러 발생
	 * Then 지하철 노선 조회 시 등록한 구간 1개가 그대로인 것을 확인할 수 있다
	 * */
	@DisplayName("구간 삭제 시 구간이 1개인 경우 에러 발생")
	@Test
	void deleteExistOneSection() {
		// given
		long newDownStationId = 4L;
		long lineId = lineApi.createLine("신분당선", "bg-red-600", 1L,  newDownStationId, 10).jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> deleteResponse = sectionApi.deleteSection(lineId, newDownStationId);
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// then
		ExtractableResponse<Response> showLineResponse = lineApi.showLineById(lineId);
		List<Long> stationIds = showLineResponse.jsonPath().getList("stations.id", Long.class);
		assertThat(stationIds).contains(1L, newDownStationId);
	}

}
