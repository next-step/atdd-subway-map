package subway.common;

import static io.restassured.RestAssured.*;

import javax.transaction.Transactional;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class RestAssuredTest {

	protected ExtractableResponse<Response> requestApi(
		Method method,
		String url, Object... pathVariables) {
		//@formatter:off
		return given()
			.log().all()
			.when()
			.request(method, url, pathVariables)
			.then()
			.log().all()
			.extract();
	}


	protected ExtractableResponse<Response> requestApi(
		RequestSpecification requestSpec,
		Method method,
		String url, Object... pathVariables) {
		//@formatter:off
		return given(requestSpec)
			.log().all()
			.when()
			.request(method, url, pathVariables)
			.then()
			.log().all()
			.extract();
	}
}
