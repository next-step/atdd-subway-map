package subway.line;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import subway.station.fixture.StationFixtures;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DirtiesContext
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        StationFixtures.createSubwayStation("지하철역");
        StationFixtures.createSubwayStation("새로운지하철역");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", 1);
        params.put("downStationId", 1);
        params.put("distance", 10L);

        // when
        given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all().and().statusCode(HttpStatus.CREATED.value())
            .and().extract();

        // then
        given().contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", 1)
            .when().get("/lines/{id}")
            .then().statusCode(HttpStatus.OK.value())
            .and()
            .body("name", equalTo("신분당선"))
            .body("stations.name", hasItems("지하철역", "새로운지하철역"));
    }
}