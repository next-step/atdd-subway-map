package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    private static Map<String, Object> line1;
    private static Map<String, Object> line2;


    @BeforeAll
    @Sql("classpath:/stations.sql")
    static void init() {
        line1 = new HashMap<>();
        line1.put("name", "신분당선");
        line1.put("color", "bg-red-600");
        line1.put("upStationId", 1);
        line1.put("downStationId", 2);
        line1.put("distance", 10);

        line2 = new HashMap<>();
        line2.put("name", "2호선");
        line2.put("color", "bg-green-600");
        line2.put("upStationId", 1);
        line2.put("downStationId", 3);
        line2.put("distance", 10);

    }


    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        //when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(line1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = response.response().getHeader("Location");

    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLines() {
        //given
        createLine(line1);
        createLine(line2);

        //when
        List<String> names = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        //then
        assertThat(names).containsAnyOf(line1.get("name").toString(), line2.get("name").toString());
    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLine() {
        //given
        Long id = createLine(line1);

        //when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/lines/{id}", id)
                        .then().log().all()
                        .extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());


    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        //given
        Long id = createLine(line1);

        Map<String, String> updateParams = new HashMap<>();

        updateParams.put("name", "구분당선");
        updateParams.put("color", "bg-orange-600");

        //when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(updateParams)
                        .when().put("/lines/{id}", id)
                        .then().log().all()
                        .extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());


    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        //given
        Long id = createLine(line1);

        //when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().delete("/lines/{id}", id)
                        .then().log().all()
                        .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    Long createLine(Map<String, Object> params) {

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = response.header("Location");
        String[] args = location.split("/");
        return Long.parseLong(args[args.length-1]);
    }


}
