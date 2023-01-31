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
import static subway.StationUtils.*;

@DisplayName("지하철 역 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

    @BeforeEach
    void setup() {
        StationUtils.createStation(GANG_NAM_STATION);
        StationUtils.createStation(SIN_SA_STATION);
        StationUtils.createStation(PAN_GYEO_STATION);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void createStationLine() {
        ExtractableResponse<Response> response = StationUtils.createStationLine(SIN_BUN_DANG_STATION_LINE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/1");
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        JsonPath jsonPath = response.jsonPath();

        assertThat(jsonPath.getLong("id")).isEqualTo(1L);
        assertThat(jsonPath.getString("name")).isEqualTo(SIN_BUN_DANG_NAME);
        assertThat(jsonPath.getString("color")).isEqualTo(LINE_RED);
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    void selectStationLineList() {
        StationUtils.createStationLine(SIN_BUN_DANG_STATION_LINE);
        StationUtils.createStationLine(BUN_DANG_STATION_LINE);

        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(StationUtils.getRequestSpecification()).log().all()
                        .when().get("/lines")
                        .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        JsonPath jsonPath = response.jsonPath();

        assertThat(jsonPath.getList("id", Integer.class)).containsExactly(1, 2);
        assertThat(jsonPath.getList("name", String.class)).containsExactly(SIN_BUN_DANG_NAME, BUN_DANG_NAME);
        assertThat(jsonPath.getList("color", String.class)).containsExactly(LINE_RED, LINE_GREEN);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void selectStationLine() {
        StationUtils.createStationLine(SIN_BUN_DANG_STATION_LINE);

        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(getRequestSpecification()).log().all()
                        .when().get("/lines/1")
                        .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        JsonPath jsonPath = response.jsonPath();

        assertThat(jsonPath.getLong("id")).isEqualTo(1L);
        assertThat(jsonPath.getString("name")).isEqualTo(SIN_BUN_DANG_NAME);
        assertThat(jsonPath.getString("color")).isEqualTo(LINE_RED);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void updateStationLine() {
        StationUtils.createStationLine(SIN_BUN_DANG_STATION_LINE);

        Map<String, Object> body = new HashMap<>();
        body.put("name", BUN_DANG_NAME);
        body.put("color", LINE_GREEN);
        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(getRequestSpecification()).body(body).log().all()
                        .when().put("/lines/1")
                        .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        JsonPath jsonPath = StationUtils.selectStationLine(1L).jsonPath();

        assertThat(jsonPath.getString("name")).isEqualTo(BUN_DANG_NAME);
        assertThat(jsonPath.getString("color")).isEqualTo(LINE_GREEN);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void deleteStationLine() {
        StationUtils.createStationLine(SIN_BUN_DANG_STATION_LINE);

        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(getRequestSpecification()).log().all()
                        .when().delete("/lines/1")
                        .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(StationUtils.selectStationLine(1).statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
