package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    StationRepository stationRepository;

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 을지로입구역 = stationRepository.save(new Station("을지로입구역"));

        // when
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", 강남역.getId());
        params.put("downStationId", 을지로입구역.getId());
        params.put("distance", 10);

        지하철노선을_생성한다(params);

        // then
        ExtractableResponse<Response> response = 지하철노선_목록을_조회한다();
        List<String> lines = response.jsonPath().getList("name", String.class);

        assertThat(lines).containsAnyOf("신분당선");
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    // TODO. 지하철노선 목록 조회 인수테스트 작성
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void selectLines() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    // TODO. 지하철노선 조회 인수테스트 작성
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void selectLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO. 지하철노선 수정 인수테스트 작성
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO. 지하철노선 삭제 인수테스트 작성
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {

    }

    private static ExtractableResponse<Response> 지하철노선_목록을_조회한다() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철노선을_생성한다(Map<String, Object> params) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }
}
