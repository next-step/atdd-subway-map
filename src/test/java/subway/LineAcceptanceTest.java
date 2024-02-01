package subway;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;

import subway.domain.LineResponse;
import subway.domain.StationResponse;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @DisplayName("지하철 노선을 생성하면 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.")
    @Test
    void test_canSearchLine_whenCreateLine() {
        //given
        // 역 저장
        Map<String, String> param1 = Map.of("name", "역삼역");
        Map<String, String> param2 = Map.of("name", "선릉역");

        createStation(param1);
        createStation(param2);

        String lineName = "신분당선";
        String lineColor = "bg-red-600";
        Long upStationId = 1L;
        Long downStationId = 2L;
        Integer distance = 10;

        Map<String, String> requestParams = Map.of(
            "name", lineName,
            "color", lineColor,
            "upStationId", upStationId.toString(),
            "downStationId", downStationId.toString(),
            "distance", distance.toString()
        );

        //when
        given()
            .body(requestParams)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines").then().log().all();

        //then
        LineResponse lineResponse = when().get("/lines").then().extract().jsonPath().getList(".", LineResponse.class).get(0);
        assertThat(lineResponse).extracting(LineResponse::getName).isEqualTo(lineName);
        assertThat(lineResponse).extracting(LineResponse::getColor).isEqualTo(lineColor);
        List<StationResponse> stationsResponse = lineResponse.getStations();

        assertThat(stationsResponse).extracting(StationResponse::getId).containsExactly(1L, 2L);
        assertThat(stationsResponse).extracting(StationResponse::getName).containsExactly("역삼역", "선릉역");
    }

    @DisplayName("2개의 지하철 노선을 생성하고 지하철 노선 목록을 조회하면 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.")
    @Test
    void test_create() {

    }

    @DisplayName("지하철 노선을 생성하고 생성한 지하철 노선을 조회하면 생성한 지하철 노선의 정보를 응답받을 수 있다.")
    @Test
    void test3() {

    }

    @DisplayName("지하철 노선을 생성하고 생성한 지하철 노선을 수정하면 해당 지하철 노선 정보는 수정된다.")
    @Test
    void test4() {

    }

    @DisplayName("지하철 노선을 생성하고 생성한 지하철 노선을 삭제하면 해당 지하철 노선 정보는 삭제된다.")
    @Test
    void test5() {

    }

    void createStation(Map<String, String> param1) {
        given().body(param1)
               .contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
               .when().post("/stations")
               .then().log().all();
    }

}
