package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("노선관련 기능")
@Sql(scripts = {"StationInsert.sql", "LineInsert.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @DirtiesContext
    @Test
    void 지하철_노선_생성_테스트() {
        //given
        Map<String, String> params = new HashMap<>();
        String lineName = "GTX-A";
        String color = "burgundy";
        String upStationId = "1";
        String downStationId = "2";
        String distance = "20";

        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        //when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        List<String> lineList = getAPIResponse("/lines").jsonPath().getList("name", String.class);

        boolean isLineInserted = lineList.stream().anyMatch(name -> name.equals(lineName));
        assertThat(isLineInserted).isTrue();
    }

    /**
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("지하철 노선목록을 조회한다.")
    @DirtiesContext
    @Test
    void 지하철_노선_목록_조회() {
        //given
        List<String> insertedLines = List.of("신분당선", "분당선", "2호선");

        //when
        ExtractableResponse<Response> response = getAPIResponse("/lines");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        List<String> testingLines = getLines();

        boolean isWellRead = insertedLines.containsAll(testingLines);
        assertThat(isWellRead).isTrue();
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @DirtiesContext
    @Test
    void 지하철_단일노선_조회() {
        //given
        String insertedName = "분당선";

        //when
        ExtractableResponse<Response> response = getAPIResponse("/lines/2");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        String testingName = response.jsonPath().getString("name");
        assertThat(testingName).isEqualTo(insertedName);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @DirtiesContext
    @Test
    void 지하철_노선_수정_테스트() {

        //when
        String changingName = "구분당선";
        String changingColor = "blue";

        Map<String, String> params = new HashMap<>();
        params.put("name", changingName);
        params.put("color", changingColor);

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(params)
                        .when().patch("/lines/1")
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        ExtractableResponse getApiResponse = getAPIResponse("/lines/1");
        String testingName = getApiResponse.jsonPath().getString("name");
        String testingColor = getApiResponse.jsonPath().getString("color");

        assertThat(testingName).isEqualTo(changingName);
        assertThat(testingColor).isEqualTo(changingColor);

    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @DirtiesContext
    @Test
    void 지하철_노선_삭제_테스트() {
        //given
        String insertedID = "2";
        String insertedName = "분당선";

        //when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().delete("/lines/" + insertedID)
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        List<String> lines = getLines();
        assertThat(lines).isNotIn(insertedName);

    }


    ExtractableResponse<Response> getAPIResponse(String url) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    List<String> getLines() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }
}
