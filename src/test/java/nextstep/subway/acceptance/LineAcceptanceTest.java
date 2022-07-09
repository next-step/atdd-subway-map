package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp(){
        RestAssured.port = port;
    }

    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine(){

    }
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLines(){

    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLine(){

    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine(){

    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine(){

    }

}
