package subway;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.DatabaseTruncate;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.api.LineTestApi.*;
import static subway.api.SectionTestApi.*;
import static subway.api.StationTestApi.*;

@DisplayName("지하철 구간 관리")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

	@Autowired
	private DatabaseTruncate databaseTruncate;

	@BeforeEach
	void truncate() {
		databaseTruncate.execute();
		fixedId = createStation("신분당선").jsonPath().getLong("id");
		registeredUpStationId = createStation("새로운지하철역").jsonPath().getLong("id");
		anotherId = createStation("또다른지하철역").jsonPath().getLong("id");
		newDownStationId = createStation("새로운구간등록역").jsonPath().getLong("id");
	}

	long fixedId;
	long registeredUpStationId;
	long anotherId;
	long newDownStationId;

	/** 구간 등록 기능 **/
	/**
	 * When 지하철 구간을 등록하면
	 * Then 지하철 노선 조회 시 등록한 구간을 찾을 수 있다.
	 * */
	@DisplayName("지하철 노선에 구간을 등록")
	@Test
	void createSectionTest() {
		// when
		long lineId = successCreateSection();

		// then
		List<Long> stationIds = getStationIds(showLineById(lineId));
		assertThat(stationIds).contains(registeredUpStationId, newDownStationId);
	}

	/**
	 * When 노선에 하행 종점역으로 등록되어 있지 않는 새로운 구간을 등록하면
	 * Then 400에러 발생
	 * Then 지하철 노선 조회 시 등록한 구간을 찾을 수 없다.
	 * */
	@DisplayName("구간 등록 시, 구간의 상행역은 노선의 하행 종점역이여 한다.")
	@Test
	void UpStationNotRegistered() {
		// when
		long lineId = createLine("신분당선", "bg-red-600", fixedId, anotherId, 10).jsonPath().getLong("id");
		ExtractableResponse<Response> createResponse = createSection(lineId, newDownStationId, registeredUpStationId, 10);

		// then
		assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// then
		List<Long> stationIds = getStationIds(showLineById(lineId));
		assertThat(stationIds).doesNotContain(newDownStationId);
	}

	/**
	 * When 노선에 등록되어 있는 역을 새로운 구간으로 등록하면
	 * Then 400에러 발생
	 * Then 지하철 노선 조회 시 등록하려던 역을 찾을 수 있다.
	 * */
	@DisplayName("구간 등록 시, 노선에 등록하려는 역이 있으면 안 된다.")
	@Test
	void newDownStationAlreadyRegistered() {
		// when
		long lineId = successCreateSection();

		// then
		ExtractableResponse<Response> alreadyRegisteredResponse = createSection(lineId, registeredUpStationId, newDownStationId, 10);
		assertThat(alreadyRegisteredResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// then
		List<Long> stationIds = getStationIds(showLineById(lineId));
		assertThat(stationIds).contains(fixedId, registeredUpStationId, newDownStationId);
	}

	/** 구간 제거 기능 **/
	/**
	 * Given 지하철 구간을 등록하고
	 * When 등록한 지하철 구간을 제거하면
	 * Then 지하철 노선 조회 시 구간이 제거된 것을 확인할 수 있다
	 **/
	@DisplayName("등록한 구간을 삭제")
	@Test
	void deleteSectionTest() {
		// given
		long lineId = successCreateSection();

		// when
		ExtractableResponse<Response> deleteResponse = deleteSection(lineId, newDownStationId);
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

		// then
		List<Long> stationIds = getStationIds(showLineById(lineId));
		assertThat(stationIds).doesNotContain(newDownStationId);
	}

	/**
	 * Given 지하철 구간을 등록하고
	 * When 중간 구간을 제거하면
	 * Then 400 에러 발생
	 * Then 지하철 노선 조회 시 등록한 구간이 그대로인 것을 확인할 수 있다
	 * */
	@DisplayName("구간 삭제 시, 마지막 구간이 아니면 에러 발생")
	@Test
	void deleteLastSection() {
		// given
		long lineId = successCreateSection();
		ExtractableResponse<Response> addSectionResponse = createSection(lineId, anotherId, newDownStationId, 10);
		assertThat(addSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// when
		ExtractableResponse<Response> deleteResponse = deleteSection(lineId, newDownStationId);
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// then
		List<Long> stationIds = getStationIds(showLineById(lineId));
		assertThat(stationIds).contains(fixedId, registeredUpStationId, newDownStationId, anotherId);
	}

	/**
	 * Given 지하철 노선(구간 1개)을 등록하고
	 * When 등록한 구간을 삭제하면
	 * Then 400 에러 발생
	 * Then 지하철 노선 조회 시 등록한 구간 1개가 그대로인 것을 확인할 수 있다
	 * */
	@DisplayName("구간 삭제 시, 구간이 1개인 경우 에러 발생")
	@Test
	void deleteExistOneSection() {
		// given
		long lineId = successCreateSection();

		// when
		ExtractableResponse<Response> deleteResponse = deleteSection(lineId, registeredUpStationId);
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// then
		List<Long> stationIds = getStationIds(showLineById(lineId));
		assertThat(stationIds).contains(fixedId, registeredUpStationId);
	}

	private Long successCreateSection() {
		long lineId = createLine("신분당선", "bg-red-600", fixedId, registeredUpStationId, 10).jsonPath().getLong("id");
		ExtractableResponse<Response> addSectionResponse = createSection(lineId, newDownStationId, registeredUpStationId, 10);
		assertThat(addSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		return lineId;
	}
}
