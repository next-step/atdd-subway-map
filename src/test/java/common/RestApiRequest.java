package common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class RestApiRequest<T> {
	private final String path;

	public RestApiRequest(String path) {
		this.path = path;
	}

	public ExtractableResponse<Response> get(Object... pathParams) {
			return RestAssured.given().log().all()
					.when()
						.get(path, pathParams)
					.then().log().all()
						.extract();
	}

	public ExtractableResponse<Response> post(T body) {
		return RestAssured.given().log().all()
				.body(body)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.post(path)
				.then().log().all()
					.extract();
	}

	public ExtractableResponse<Response> put(T body, Object... pathParams) {
		return RestAssured.given().log().all()
				.body(body)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.put(path, pathParams)
				.then().log().all()
					.extract();
	}

	public ExtractableResponse<Response> delete(Object... pathParams) {
		return RestAssured.given().log().all()
				.when()
					.delete(path, pathParams)
				.then().log().all()
					.extract();
	}
}
