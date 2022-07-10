package nextstep.subway.test.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author a1101466 on 2022/07/10
 * @project subway
 * @description
 */
public class LineAcceptanceTest extends AcceptanceTest{
    StationRequest stationRequest;

    @BeforeEach
    public void lineTestSetUp() {
        stationRequest.지하철역_생성("개봉역");
        stationRequest.지하철역_생성("구일역");
    }
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @Test
    @DisplayName("지하철노선을 생성한다")
    void createLine(){


        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "red");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract()
                ;
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철노선 목록 조회")
    void getLineList(){

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 시하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철노선 조회")
    void getLine(){

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @Test
    @DisplayName("지하철조선 수정")
    void modifyLine(){

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @Test
    @DisplayName("지하철노선 삭제")
    void deleteLine(){

    }
}
