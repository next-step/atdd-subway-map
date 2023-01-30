package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort private int port;

    @BeforeAll
    private void setupForClass() {
        RestAssured.port = port;
    }

    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLineTest() {
        // when

        // then
    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLineListTest() {
        // given

        // when

        // then
    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLineTest() {
        // given

        // when

        // then
    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLineTest() {
        // given

        // when

        // then
    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLineTest() {
        // given

        // when

        // then
    }
}
