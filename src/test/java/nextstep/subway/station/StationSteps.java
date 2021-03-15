package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StationSteps {

	protected static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
		StationRequest request = new StationRequest(name);

		// when
		return RestAssured.given().log().all()
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/stations")
			.then().log().all()
			.extract();
	}

	protected static void 지하철역_생성_요청됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
	}

	protected static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	protected static Long 지하철역_등록되어_있음(String name) {

		return 지하철역_생성_요청(name).as(StationResponse.class).getId();
	}

	protected static ExtractableResponse<Response> 지하철역_목록_조회_요청() {

		return RestAssured.given().log().all()
			.when()
			.get("/stations")
			.then().log().all()
			.extract();
	}

	protected static void 지하철역_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	protected static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
		지하철역_응답됨(response);
	}

	protected static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	protected static ExtractableResponse<Response> 지하철역_삭제_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");

		return RestAssured.given().log().all()
			.when()
			.delete(uri)
			.then().log().all()
			.extract();
	}

	protected static void 지하철역_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
		List<Long> expectedLineIds = createdResponses.stream()
			.map(it -> Long.parseLong(it.header("Location").split("/")[2]))
			.collect(Collectors.toList());

		List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		assertThat(resultLineIds).containsAll(expectedLineIds);
	}
}
