package testhelper;

import static org.springframework.http.MediaType.*;

import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.api.interfaces.dto.LineCreateRequest;
import subway.api.interfaces.dto.LineUpdateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/26
 */
public class StationLineRequestExecutor {

	private static final String URL_PATH = "/lines";

	public static ExtractableResponse<Response> executeCreateLineRequest(LineCreateRequest createRequest) {
		return RestAssured.given().log().all()
			.body(createRequest)
			.contentType(APPLICATION_JSON_VALUE)
			.when().post(URL_PATH)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> executeGetAllStationLineRequest() {
		return RestAssured.given().log().all()
			.when().get(URL_PATH)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> executeGetSpecificStationLineRequest(Long id) {
		return RestAssured.given().log().all()
			.when().get(URL_PATH + "/" + id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> executeGetSpecificStationLineRequestWithOk(Long id) {
		return RestAssured.given().log().all()
			.when().get(URL_PATH + "/" + id)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract();
	}


	public static ExtractableResponse<Response> executeUpdateLineRequest(Long id, LineUpdateRequest updateRequest) {
		return RestAssured.given().log().all()
			.body(updateRequest)
			.contentType(APPLICATION_JSON_VALUE)
			.when().put(URL_PATH + "/" + id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> executeDeleteLineRequest(Long id) {
		return RestAssured.given().log().all()
			.when().delete(URL_PATH + "/" + id)
			.then().log().all()
			.extract();
	}




}
