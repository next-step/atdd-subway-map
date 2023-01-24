package subway;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {

	@DisplayName("구글 페이지 접근 테스트")
	@Test
	void accessGoogle() {
		String googleUrl = "https://www.google.com";

		//@formatter:off
		ExtractableResponse<Response> response = given()
			.log().all()
				.when()
				.get(googleUrl)
			.then()
				.log().all()
			.extract();

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}
}
