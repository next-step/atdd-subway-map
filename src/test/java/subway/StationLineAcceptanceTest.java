package subway;

import config.fixtures.subway.StationLineMockData;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

    /**
      * When 지하철 노선을 생성하면
      * When  지하철 노선이 생성된다
      * Then  지하철 노선 목록 조회 시 생성된 노선을 찾을 수 있다.
      */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // when
        Map<String, String> map = new HashMap<>();
        map.put("name", StationLineMockData.stationLineName);
        map.put("color", StationLineMockData.stationLineColor);
        map.put("upStationId", String.valueOf(StationLineMockData.upStationId));
        map.put("downStationId", String.valueOf(StationLineMockData.downStationId));
        map.put("distance", String.valueOf(StationLineMockData.distance));

        List<String> stationLineName = RestAssured
                .given().log().all()
                    .body(map)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then().log().all()
                    .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getList("name", String.class);

        // then
        assertThat(stationLineName).containsAnyOf(StationLineMockData.stationLineName);
    }

    /**
      * Given 2개의 지하철 노선을 생성하고
      * When  지하철 노선 목록을 조회하면
      * Then  지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
      */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllStationLine() {
        // given

        // when

        // then
    }

    /**
      * Given 지하철 노선을 생성하고
      * When  생성한 지하철 노선을 조회하면
      * Then  생성한 지하철 노선의 정보를 응답받을 수 있다.
      */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findStationLine() {
        // given

        // when

        // then
    }

    /**
      * Given 지하철 노선을 생성하고
      * When  생성한 지하철 노선을 수정하면
      * Then  해당 지하철 노선 정보는 수정된다.
      */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateStationLine() {
        // given

        // when

        // then
    }

    /**
      * Given 지하철 노선을 생성하고
      * When  생성한 지하철 노선을 삭제하면
      * Then  해당 지하철 노선 정보는 삭제된다.
      */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteStationLine() {
        // given

        // when

        // then
    }
}
