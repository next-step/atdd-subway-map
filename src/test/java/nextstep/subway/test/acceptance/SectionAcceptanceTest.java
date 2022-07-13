package nextstep.subway.test.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.test.acceptance.LineTestMethod.지하철노선_생성;
import static nextstep.subway.test.acceptance.StationTestMethod.지하철역_생성;

/**
 * @author a1101466 on 2022/07/10
 * @project subway
 * @description
 */
public class SectionAcceptanceTest extends AcceptanceTest{

    public String 파랑선_시작ID;
    public String 파랑선_종착ID;
    public String 초록선_시작ID;
    public String 초록선_종착ID;

    public Long 초록선_라인ID;

    @BeforeEach
    public void lineTestSetUp() {
        파랑선_시작ID = 지하철역_생성("구일역").jsonPath().getString("id");
        파랑선_종착ID = 지하철역_생성("구로역").jsonPath().getString("id");;
        초록선_시작ID = 지하철역_생성("신도립역").jsonPath().getString("id");;
        초록선_종착ID = 지하철역_생성("문래역").jsonPath().getString("id");;

        지하철노선_생성("파랑선", 파랑선_시작ID, 파랑선_종착ID, "blue", "10");
        초록선_라인ID = 지하철노선_생성("초록선", 초록선_시작ID, 초록선_종착ID, "green", "10")
                .jsonPath().getLong("id");
    }
    /**
     * 지하철 노선에 구간을 등록하는 기능을 구현
     * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @Test
    @DisplayName("지하철구간을 생성한다")
    void createSection(){

        String 새로운구간_종착ID = 지하철역_생성("합정역").jsonPath().getString("id");

        Map<String, String> params = new HashMap<>();
        params.put("downStationId", 새로운구간_종착ID );
        params.put("upStationId", 초록선_종착ID);
        params.put("distance", "10");

        RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();
    }

    /**
     * 지하철 노선에 구간을 제거하는 기능 구현
     * 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
     * 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
     * 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @Test
    @DisplayName("구간을 제거한다")
    void deleteSection(){

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/sections", 초록선_라인ID)
                .then().log().all()
                .extract();

    }

}
