package subway.common;

import static io.restassured.RestAssured.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Import(JpaDataBaseCleaner.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AcceptanceTest {

	@Autowired
	private JpaDataBaseCleaner jpaDataBaseCleaner;

	@AfterEach
	void cleanTable() {
		jpaDataBaseCleaner.execute();
	}

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
