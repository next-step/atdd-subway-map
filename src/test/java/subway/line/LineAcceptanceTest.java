package subway.line;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import com.google.thirdparty.publicsuffix.PublicSuffixType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.DataBaseCleanUp;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.station.StationAcceptanceTest;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

	public static final String RESOURCE_PATH = "/lines";

	@Autowired
	private DataBaseCleanUp dataBaseCleanUp;

	@AfterEach
	void tearDown() {
		dataBaseCleanUp.cleanUp();
	}

	@Test
	void 지하철_노선을_생성한다() {
		//given
		LineCreateRequest lineCreateRequest = createLineRequestFixture("신분당선");

		// when 지하철 노선을 생성하면
		long id = requestCreateLine(lineCreateRequest).getId();

		// then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
		List<LineResponse> lineList = requestFindAllLines();

		LineResponse created = lineList.get(0);

		assertThat(lineList).hasSize(1);
		assertRequestAndFindEquals(lineCreateRequest, id, created);
	}

	@Test
	void 지하철_노선_목록_조회() {
		//given 2개의 지하철노선을 생성하고
		long id1 = requestCreateLine("1호선").getId();
		long id2 = requestCreateLine("2호선").getId();

		//when 지하철노선 목록을 조회하면
		List<LineResponse> findLines = requestFindAllLines();
		List<Long> ids = filterIds(findLines);

		//then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
		assertThat(findLines).hasSize(2);
		assertThat(ids).contains(id1, id2);
	}

	private List<Long> filterIds(List<LineResponse> findLines) {
		return findLines.stream()
			.map(LineResponse::getId)
			.collect(Collectors.toList());
	}

	private List<LineResponse> requestFindAllLines() {
		return given()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(RESOURCE_PATH)
			.then()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.jsonPath().getList(".", LineResponse.class);
	}

	@Test
	void 지하철_노선_조회() {
		//Given 지하철 노선을 생성하고
		LineCreateRequest lineCreateRequest = createLineRequestFixture("1호선");
		long id = requestCreateLine(lineCreateRequest).getId();

		//When 생성한 지하철 노선을 조회하면
		ExtractableResponse<Response> response = requestFindLine(id);

		//Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertRequestAndFindEquals(lineCreateRequest, id, response.as(LineResponse.class));
	}

	@Test
	void 지하철_노선_수정() {
		//Given 지하철 노선을 생성하고
		long id = requestCreateLine("1호선").getId();
		LineUpdateRequest request = LineUpdateRequest.of(id, "newName", "newColor");

		//When 생성한 지하철 노선을 수정하면
		requestUpdateLine(request);

		//Then 해당 지하철 노선 정보는 수정된다
		ExtractableResponse<Response> response = requestFindLine(id);
		LineResponse find = response.as(LineResponse.class);

		//Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(find.getName()).isEqualTo(request.getName());
		assertThat(find.getColor()).isEqualTo(request.getColor());
	}

	private void requestUpdateLine(LineUpdateRequest request) {
		given()
			.body(request)
			.pathParam("id", request.getId())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.patch(RESOURCE_PATH + "/{id}")
			.then()
			.statusCode(HttpStatus.OK.value())
			.assertThat();
	}

	@Test
	void 지하철_노선_삭제() {
		//Given 지하철 노선을 생성하고
		long id = requestCreateLine("1호선").getId();

		//When 생성한 지하철 노선을 삭제하면
		requestDeleteLine(id);

		//Then 해당 지하철 노선 정보는 삭제된다
		ExtractableResponse<Response> response = requestFindLine(id);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	public static ExtractableResponse<Response> requestFindLine(long id) {
		return given()
			.pathParam("id", id)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get(RESOURCE_PATH + "/{id}")
			.then()
			.extract();
	}

	private void requestDeleteLine(long id) {
		given()
			.pathParam("id", id)
			.when()
			.delete(RESOURCE_PATH + "/{id}")
			.then()
			.statusCode(HttpStatus.NO_CONTENT.value())
			.assertThat();
	}

	private void assertRequestAndFindEquals(LineCreateRequest request, long id, LineResponse find) {
		assertThat(find.getId()).isEqualTo(id);
		assertThat(find.getName()).isEqualTo(request.getName());
		assertThat(find.getColor()).isEqualTo(request.getColor());
		assertThat(find.getStations()).hasSize(2);
		assertThat(find.getStations().get(0).getId()).isEqualTo(request.getUpStationId());
		assertThat(find.getStations().get(1).getId()).isEqualTo(request.getDownStationId());
	}

	public static LineResponse requestCreateLine(String name) {
		return requestCreateLine(createLineRequestFixture(name));
	}

	public static LineCreateRequest createLineRequestFixture(String name) {
		String color = "bg-red-600";
		long upStationId = StationAcceptanceTest.createStation(name + "-상행역");
		long downStationId = StationAcceptanceTest.createStation(name + "-하행역");
		long distance = 10;
		return new LineCreateRequest(name, color, upStationId, downStationId, distance);
	}

	public static LineResponse requestCreateLine(LineCreateRequest lineCreateRequest) {
		return given()
			.body(lineCreateRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post(RESOURCE_PATH)
			.then()
			.statusCode(HttpStatus.CREATED.value())
			.extract()
			.as(LineResponse.class);
	}
}