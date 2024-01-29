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

import subway.line.LineResponse;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @DisplayName("지하철 노선을 생성하면 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.")
    @Test
    void test_canSearchLine_whenCreateLine() {
        //given
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
        List<LineResponse> responses = when().get("/lines").then().extract().jsonPath().getList(".", LineResponse.class);
        assertThat(responses).extracting(LineResponse::getName).containsExactly(lineName);
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

}
