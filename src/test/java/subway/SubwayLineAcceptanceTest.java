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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineAcceptanceTest {

    private final static int PORT = 8080;

    @BeforeEach
    void setUp() {
        RestAssured.port = PORT;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    // TODO 지하철 노선 생성
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //when
        Map<String, String> params = new HashMap<>();
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/line")
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    // TODO 지하철 노선 목록 조회
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void findLines() {
        //given
        long firstLineId = beforeTestCreateLine();
        long secondLineId = beforeTestCreateLine();

        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when().post("/line/list")
                .then().log().all()
                .extract();
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        extract.body().jsonPath().get("$.list.*");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    // TODO 지하철 노선 조회
    @DisplayName("지하철 노선 단일 조회")
    @Test
    void findLine() {
        //given
        long id = beforeTestCreateLine();
        Map<String, String> params = new HashMap<>();

        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/line/{id}", id)
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());

    }
    /**
     *Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO 지하철 노선 수정
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given
        Long id = beforeTestCreateLine();
        Map<String, String> params = new HashMap<>();

        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/line/{id}", id)
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO 지하철 노선 삭제
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        Long id = beforeTestCreateLine();
        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when().delete("/line/{id}", id)
                .then().log().all()
                .extract();
        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private long beforeTestCreateLine() {
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when().post("/line")
                .then().log().all()
                .extract();
        return extract.body().jsonPath().getLong("id");
    }
}
