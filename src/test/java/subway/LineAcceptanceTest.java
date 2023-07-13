package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

//    StationAcceptanceTest stationAcceptanceTest = new StationAcceptanceTest();

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        StationAcceptanceTest.지하철_역_추가_요청("사당역");
        StationAcceptanceTest.지하철_역_추가_요청("이수역");

        ExtractableResponse<Response> response = 노선_추가_요청("4호선", "skyblue", 1, 2, 10);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Map<String, Object> line = 노선_조회(Long.valueOf("1"));
        assertThat(line.get("name")).isEqualTo("4호선");
    }

    @DisplayName("지하철 노선을 전체 조회한다.")
    @Test
    void showLines() {
        //given
        StationAcceptanceTest.지하철_역_추가_요청("사당역");
        StationAcceptanceTest.지하철_역_추가_요청("이수역");

        노선_추가_요청("4호선", "skyblue", 1, 2, 10);

        StationAcceptanceTest.지하철_역_추가_요청("강남역");
        StationAcceptanceTest.지하철_역_추가_요청("역삼역");

        노선_추가_요청("2호선", "green", 3, 4, 7);
        //when
        List<HashMap<String, Object>> lines = 노선_전체_조회();
        //then
        assertThat(lines.size()).isEqualTo(2);
    }

    @DisplayName("지하철 노선을 조회한다")
    @Test
    void showLine() {
        //given
        StationAcceptanceTest.지하철_역_추가_요청("사당역");
        StationAcceptanceTest.지하철_역_추가_요청("이수역");

        노선_추가_요청("4호선", "skyblue", 1, 2, 10);

        //when
        Map<String, Object> line = 노선_조회(Long.valueOf("1"));

        //then
        assertThat(line.get("name")).isEqualTo("4호선");
    }

    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void deleteLine() {
        //given
        ExtractableResponse<Response> response_sadang = StationAcceptanceTest.지하철_역_추가_요청("사당역");
        ExtractableResponse<Response> response_isu = StationAcceptanceTest.지하철_역_추가_요청("이수역");

        노선_추가_요청("4호선", "skyblue", 1, 2, 10);

        //when
        ExtractableResponse<Response> response = StationAcceptanceTest.지하철_역_삭제_요청(response_sadang.response().getHeader("location"));
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 노선_추가_요청(String name, String color, Integer upStationId, Integer downStationId, Integer distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", distance.toString());

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private Map<String, Object> 노선_조회(Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract().jsonPath().get();
    }

    private List<HashMap<String, Object>> 노선_전체_조회() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList(".");
    }

    private ExtractableResponse<Response> 노선_삭제_요청(String location) {
        return RestAssured.given().log().all()
                .when().delete(location)
                .then().log().all()
                .extract();
    }
}