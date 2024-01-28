package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.common.CommonApi;
import subway.common.Fixture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    Fixture.Station 지하철역 = Fixture.지하철역();
    Fixture.Station 새로운지하철역 = Fixture.새로운지하철역();
    Fixture.Station 또다른지하철역 = Fixture.또다른지하철역();
    Fixture.Line 신분당선 = Fixture.신분당선(지하철역, 새로운지하철역);
    Fixture.Line 분당선 = Fixture.분당선(지하철역, 또다른지하철역);

    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        /*
        * TODO
        * When 지하철 노선을 생성하면
        * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        */
    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void showLines() {
        /*
         * TODO
         * Given 2개의 지하철 노선을 생성하고
         * When 지하철 노선 목록을 조회하면
         * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
         */
    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void retrieveLine() {
        /*
         * TODO
         * Given 지하철 노선을 생성하고
         * When 생성한 지하철 노선을 조회하면
         * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
         */
    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        /*
         * TODO
         * Given 지하철 노선을 생성하고
         * When 생성한 지하철 노선을 수정하면
         * Then 해당 지하철 노선 정보는 수정된다
         */
    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        /*
         * TODO
         * Given 지하철 노선을 생성하고
         * When 생성한 지하철 노선을 삭제하면
         * Then 해당 지하철 노선 정보는 삭제된다
         */
    }


}
