package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@DisplayName("지하철 노선 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    // When 지하철 노선을 생성하면
    // Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given 지하철 노선을 생성
        지하철노선_생성();

        // then 지하철 노석 목록 조회하여 생성한 노선 확인
        지하철노선_조회();
    }

    // Given 2개의 지하철 노선을 생성하고
    // When 지하철 노선 목록을 조회하면
    // Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given 2개의 지하철 노선 생성
        지하철노선_생성();
        지하철노선_생성();

        // when 지하철 노선 목록 조회
        지하철노선_목록조회();

        // then 지하철 노선 목록 조회 시 2개의 노선 조회
    }

    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 조회하면
    // Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given 지하철 노선 생성
        지하철노선_생성();

        // when 지하철 노선 조회
        지하철노선_조회();

        // then 생성한 지하철 노선의 정보 응답 확인
    }

    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 수정하면
    // Then 해당 지하철 노선 정보는 수정된다
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given 지하철 노선 생성
        지하철노선_생성();

        // when 생성한 지하철 노선 수정
        지하철노선_수정();

        // then 지하철 노선 정보 수정 확인
        지하철노선_조회();
    }

    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 삭제하면
    // Then 해당 지하철 노선 정보는 삭제된다
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given 지하철 노선 생성
        지하철노선_생성();

        // when 생성한 지하철 노석 삭제
        지하철노선_삭제();

        // then 지하철 노선 정보 삭제 확인
        지하철노선_조회();
    }

    private ExtractableResponse<Response> 지하철노선_생성() {
        return null;
    }

    private ExtractableResponse<Response> 지하철노선_목록조회() {
        return null;
    }

    private ExtractableResponse<Response> 지하철노선_조회() {
        return null;
    }

    private ExtractableResponse<Response> 지하철노선_수정() {
        return null;
    }

    private ExtractableResponse<Response> 지하철노선_삭제() {
        return null;
    }

}
