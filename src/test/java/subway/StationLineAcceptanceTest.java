package subway;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 역 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {
    @BeforeEach
    void setup() {
        StationUtils.createStation("강남역");
        StationUtils.createStation("신사역");
        StationUtils.createStation("판교역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void createStationLine() {
        Map<String, Object> body = new HashMap<>();
        String name = "신분당선";
        String color = "bg-red-600";
        body.put("name", name);
        body.put("color", color);
        body.put("upStationId", 1);
        body.put("downStationId", 2);
        body.put("distance", 10);

        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(StationUtils.getRequestSpecification()).body(body).log().all()
                        .when().post("/lines")
                        .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/1");
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        JsonPath jsonPath = response.jsonPath();

        assertThat(jsonPath.getInt("$.id")).isEqualTo(1);
        assertThat(jsonPath.getString("$.name")).isEqualTo(name);
        assertThat(jsonPath.getString("$.color")).isEqualTo(color);
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    void selectStationLineList() {
        Map<String, Object> body1 = new HashMap<>();
        String name1 = "신분당선";
        String color1 = "bg-red-600";
        body1.put("name", name1);
        body1.put("color", color1);
        body1.put("upStationId", 1);
        body1.put("downStationId", 2);
        body1.put("distance", 10);

        Map<String, Object> body2 = new HashMap<>();
        String name2 = "분당선";
        String color2 = "bg-green-600";
        body2.put("name", name2);
        body2.put("color", color2);
        body2.put("upStationId", 1);
        body2.put("downStationId", 3);
        body2.put("distance", 20);

        StationUtils.createStationLine(body1);
        StationUtils.createStationLine(body2);

        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(StationUtils.getRequestSpecification()).log().all()
                        .when().get("/lines")
                        .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        JsonPath jsonPath = response.jsonPath();

        assertThat(jsonPath.getList("id", Integer.class)).containsExactly(1, 2);
        assertThat(jsonPath.getList("name", String.class)).containsExactly(name1, name2);
        assertThat(jsonPath.getList("color", String.class)).containsExactly(color1, color2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void selectStationLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void updateStationLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void deleteStationLine() {

    }



}
