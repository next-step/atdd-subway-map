package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.request.SubwayLineRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@DirtiesContext
@Sql("/truncate.sql")
public class SubwayLineAcceptanceTest {

    @BeforeEach
    void setUp() {
        createStation("지하철역");
        createStation("새로운지하철역");
        createStation("또다른지하철역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {
        // when
        ExtractableResponse<Response> response = createSubwayLine(new SubwayLineRequest("신분당선", "bg-red-600", 1L, 2L, 10));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> subwayLines = getSubwayLines();
        assertThat(subwayLines).containsAnyOf("신분당선");
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void showSubwayLines() {
        //given
        createSubwayLine(new SubwayLineRequest("신분당선", "bg-red-600", 1L, 2L, 10));
        createSubwayLine(new SubwayLineRequest("분당선", "bg-green-600", 1L, 3L, 10));

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        //then
        List<String> subwayLineNames = response.jsonPath().getList("name", String.class);
        assertThat(subwayLineNames.size()).isEqualTo(2);
        //then
        assertThat(subwayLineNames).containsAnyOf("신분당선", "분당선");
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void showSubwayLine() {
        //given
        createSubwayLine(new SubwayLineRequest("신분당선", "bg-red-600", 1L, 2L, 10));

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/1")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        //then
        String name = response.jsonPath().get("name");
        assertThat(name).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateSubwayLine() {
        //given
        createSubwayLine(new SubwayLineRequest("신분당선", "bg-red-600", 1L, 2L, 10));

        //when
        Map<String, String> params = new HashMap<>();
        params.put("name", "다른 분당선");
        params.put("color", "bg-red-600");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/1")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        //then
        List<String> subwayLineNames = getSubwayLines();
        assertThat(subwayLineNames.size()).isEqualTo(1);
        assertThat(subwayLineNames).containsAnyOf("다른 분당선");
    }


    /**
     * Given 지하철 노선을 생성고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteSubwayLine() {
        //given
        createSubwayLine(new SubwayLineRequest("신분당선", "bg-red-600", 1L, 2L, 10));

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/1")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        //then
        List<String> stationNames = getSubwayLines();
        assertThat(stationNames).doesNotContain("신분당선");
    }

    private void createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all();
    }

    private List<String> getSubwayLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    private static ExtractableResponse<Response> createSubwayLine(SubwayLineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }


}