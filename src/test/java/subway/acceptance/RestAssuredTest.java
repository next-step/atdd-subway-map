package subway.acceptance;

import static io.restassured.RestAssured.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class RestAssuredTest {

    public static final int DEFAULT_REST_ASSURED_PORT = -1;
    @Autowired
    RestAssuredUtil restAssuredUtil;

    @BeforeEach
    void setup() {
        restAssuredUtil.initializePort(DEFAULT_REST_ASSURED_PORT);
    }

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    void accessGoogle() {
        // TODO: 구글 페이지 요청 구현
        when().
            get("https://google.com").
        then().
            statusCode(HttpStatus.OK.value());
    }
}