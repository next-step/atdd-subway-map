package subway.section;

import static fixture.LineFixtureCreator.*;
import static org.junit.jupiter.api.Assertions.*;
import static testhelper.ExtractableResponseParser.*;
import static testhelper.LineRequestExecutor.*;
import static testhelper.SectionRequestExecutor.*;
import static testhelper.StationRequestExecutor.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.api.interfaces.dto.LineCreateRequest;
import subway.api.interfaces.dto.SectionCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@DisplayName("지하철 구간 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionManagementTest {

	/**
	 * 구간 등록 - 성공 케이스
	 * - Given 지하철 노선이 존재하고, 해당 노선의 하행 종점역이 존재할 때
	 * - When 새로운 구간을 등록하려고 할 때
	 * - Then 새로운 구간의 상행역은 현재 노선의 하행 종점역이어야 한다.
	 * - And 이미 노선에 등록된 역은 새로운 구간의 하행역이 될 수 없다.
	 * - And 조건을 만족하지 않는 경우 에러를 반환한다.
	 */
	@Test
	@Sql(scripts = {"/cleanup.sql"})
	@DisplayName("구간 등록 - 성공 케이스")
	void createSection_Success() {
		// given
		ExtractableResponse<Response> stationCreateResponse1 = executeCreateStationRequest("상행 종점역");
		long stationId1 = parseId(stationCreateResponse1);
		ExtractableResponse<Response> stationCreateResponse2 = executeCreateStationRequest("하행 종점역");
		long stationId2 = parseId(stationCreateResponse2);
		LineCreateRequest lineCreateRequest = createLineCreateRequest("노선 이름", stationId1, stationId2);
		ExtractableResponse<Response> lineCreateResponse = executeCreateLineRequest(lineCreateRequest);
		long lineId = parseId(lineCreateResponse);

		ExtractableResponse<Response> newStationCreateResponse = executeCreateStationRequest("새로운 역");
		long stationId3 = parseId(newStationCreateResponse);

		// When & then
		// 성공 케이스 -> 상행역은 해당 노선에 등록되어 있는 하행 종점역이어야 한다 & 새로운 역이어야 한다 조건을 만족하도록 request 구성
		SectionCreateRequest sectionCreateRequest = SectionCreateRequest.builder().upStationId(stationId2).downStationId(stationId3).distance(10L).build();
		ExtractableResponse<Response> response = executeCreateSectionRequest(lineId, sectionCreateRequest);

		assertEquals( HttpStatus.CREATED.value(), response.statusCode());
	}

	/**
	 * 구간 등록 - 예외 케이스
	 * - Given 지하철 노선이 존재하고, 해당 노선에 역이 등록되어 있을 때
	 * - When 부적합한 구간(상행역 또는 하행역 조건 불만족)을 등록하려고 할 때
	 * - Then 에러를 반환한다.
	 */
	@Test
	@Sql(scripts = {"/cleanup.sql"})
	@DisplayName("구간 등록 - 예외 케이스 1 -> 새로운 구간은 상행역은 해당 노선에 등록되어 있는 하행 종점역이어야 한다는 조건을 불만족")
	void createSection_Failure_1() {
		// Given
		ExtractableResponse<Response> stationCreateResponse1 = executeCreateStationRequest("상행 종점역");
		long stationId1 = parseId(stationCreateResponse1);
		ExtractableResponse<Response> stationCreateResponse2 = executeCreateStationRequest("하행 종점역");
		long stationId2 = parseId(stationCreateResponse2);
		LineCreateRequest lineCreateRequest = createLineCreateRequest("노선 이름", stationId1, stationId2);
		ExtractableResponse<Response> lineCreateResponse = executeCreateLineRequest(lineCreateRequest);
		long lineId = parseId(lineCreateResponse);

		ExtractableResponse<Response> newStationCreateResponse = executeCreateStationRequest("새로운 역");
		long stationId3 = parseId(newStationCreateResponse);

		// When
		// 새로운 구간의 상행역은 해당 노선에 등록되어 있는 하행 종점역이어야 한다는 조건을 불만족
		SectionCreateRequest invalidSectionRequest = SectionCreateRequest.builder().upStationId(stationId1).downStationId(stationId3).distance(10L).build();
		ExtractableResponse<Response> response = executeCreateSectionRequest(lineId, invalidSectionRequest);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
	}

	/**
	 * 구간 등록 - 예외 케이스
	 * - Given 지하철 노선이 존재하고, 해당 노선에 역이 등록되어 있을 때
	 * - When 부적합한 구간(상행역 또는 하행역 조건 불만족)을 등록하려고 할 때
	 * - Then 에러를 반환한다.
	 */
	@Test
	@Sql(scripts = {"/cleanup.sql"})
	@DisplayName("구간 등록 - 예외 케이스 2 -> 이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다는 조건을 불만족")
	void createSection_Failure_2() {
		// Given
		ExtractableResponse<Response> stationCreateResponse1 = executeCreateStationRequest("상행 종점역");
		long stationId1 = parseId(stationCreateResponse1);
		ExtractableResponse<Response> stationCreateResponse2 = executeCreateStationRequest("하행 종점역");
		long stationId2 = parseId(stationCreateResponse2);
		LineCreateRequest lineCreateRequest = createLineCreateRequest("노선 이름", stationId1, stationId2);
		ExtractableResponse<Response> lineCreateResponse = executeCreateLineRequest(lineCreateRequest);
		long lineId = parseId(lineCreateResponse);

		// When
		// 이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다는 조건을 불만족
		SectionCreateRequest invalidSectionRequest = SectionCreateRequest.builder().upStationId(stationId2).downStationId(stationId1).distance(10L).build();
		ExtractableResponse<Response> response = executeCreateSectionRequest(lineId, invalidSectionRequest);

		// Then
		assertEquals( HttpStatus.BAD_REQUEST.value(), response.statusCode());
	}

	/**
	 * 구간 제거 - 성공 케이스
	 * - Given 지하철 노선이 존재하고, 해당 노선에 여러 역이 등록되어 있을 때
	 * - When 하행 종점역을 제거하려고 할 때
	 * - Then 해당 역이 제거된다.
	 * - And 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
	 * - And 조건을 만족하지 않는 경우 에러를 반환한다.
	 */
	@Test
	@Sql(scripts = {"/cleanup.sql"})
	@DisplayName("구간 제거 - 성공 케이스")
	void deleteSection_Success() {
		// Given
		ExtractableResponse<Response> stationCreateResponse1 = executeCreateStationRequest("상행 종점역");
		long stationId1 = parseId(stationCreateResponse1);
		ExtractableResponse<Response> stationCreateResponse2 = executeCreateStationRequest("하행 종점역");
		long stationId2 = parseId(stationCreateResponse2);
		LineCreateRequest lineCreateRequest = createLineCreateRequest("노선", stationId1, stationId2);
		ExtractableResponse<Response> lineCreateResponse = executeCreateLineRequest(lineCreateRequest);
		long lineId = parseId(lineCreateResponse);

		ExtractableResponse<Response> newStationCreateResponse = executeCreateStationRequest("새로운 역");
		long stationId3 = parseId(newStationCreateResponse);
		SectionCreateRequest sectionCreateRequest = SectionCreateRequest.builder().upStationId(stationId2).downStationId(stationId3).distance(10L).build();
		executeCreateSectionRequest(lineId, sectionCreateRequest);

		// When
		ExtractableResponse<Response> deleteResponse = executeDeleteSectionRequest(lineId, stationId3);

		// Then
		assertEquals(HttpStatus.NO_CONTENT.value(), deleteResponse.statusCode());
	}

	/**
	 * 구간 제거 - 예외 케이스
	 * - Given 지하철 노선이 존재하고, 해당 노선에 여러 역이 등록되어 있을 때
	 * - When 마지막 구간이 아닌 구간을 제거하려고 할 때
	 * - Then 에러를 반환한다.
	 */
	@Test
	@Sql(scripts = {"/cleanup.sql"})
	@DisplayName("구간 제거 - 예외 케이스 -> 마지막 구간이 아닌 구간을 제외할 때 예외 발생")
	void deleteSection_Failure_1() {
		// Given
		ExtractableResponse<Response> stationCreateResponse1 = executeCreateStationRequest("Station A");
		long stationId1 = parseId(stationCreateResponse1);
		ExtractableResponse<Response> stationCreateResponse2 = executeCreateStationRequest("Station B");
		long stationId2 = parseId(stationCreateResponse2);
		ExtractableResponse<Response> stationCreateResponse3 = executeCreateStationRequest("Station C");
		long stationId3 = parseId(stationCreateResponse3);

		LineCreateRequest lineCreateRequest = createLineCreateRequest("노선", stationId1, stationId2);
		ExtractableResponse<Response> lineCreateResponse = executeCreateLineRequest(lineCreateRequest);
		long lineId = parseId(lineCreateResponse);

		SectionCreateRequest newSectionRequest = SectionCreateRequest.builder()
			.upStationId(stationId2)
			.downStationId(stationId3)
			.distance(10L)
			.build();
		executeCreateSectionRequest(lineId, newSectionRequest);

		// When -> 마지막 구간이 아닌 라인을 삭제 시도한다
		ExtractableResponse<Response> deleteResponse = executeDeleteSectionRequest(lineId, stationId2);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST.value(), deleteResponse.statusCode());
	}

	/**
	 * 구간 제거 - 예외 케이스
	 * - Given 지하철 노선이 존재하고, 해당 노선에 상행과 하행이 단 1개씩 존재할 때
	 * - When 하행역을 삭제하려고 할 때
	 * - Then 에러를 반환한다.
	 */
	@Test
	@Sql(scripts = {"/cleanup.sql"})
	@DisplayName("구간 제거 - 예외 케이스 -> 상행 종점역과 하행 종점역이 각 1개만 있는 경우 삭제 할 수 없다는 조건을 불만족")
	void deleteSection_Failure_2() {
		// Given
		ExtractableResponse<Response> stationCreateResponse1 = executeCreateStationRequest("상행 종점역");
		long stationId1 = parseId(stationCreateResponse1);
		ExtractableResponse<Response> stationCreateResponse2 = executeCreateStationRequest("하행 종점역");
		long stationId2 = parseId(stationCreateResponse2);
		LineCreateRequest lineCreateRequest = createLineCreateRequest("노선", stationId1, stationId2);
		ExtractableResponse<Response> lineCreateResponse = executeCreateLineRequest(lineCreateRequest);
		long lineId = parseId(lineCreateResponse);

		// When
		ExtractableResponse<Response> deleteResponse = executeDeleteSectionRequest(lineId, stationId2);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST.value(), deleteResponse.statusCode());
	}

}
