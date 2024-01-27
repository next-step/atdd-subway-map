package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestAssuredTest {

	@DisplayName("구글 페이지 접근 테스트")
	@Test
	void accessGoogle() {
		RestAssured.given().when().get("https://google.com").then().statusCode(200);
	}
}
