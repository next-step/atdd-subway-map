package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationLineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createStationLine() {
        //when
        Long 강남역 = 지하철역_등록("강남역").as(StationResponse.class).getId();
        Long 역삼역 = 지하철역_등록("역삼역").as(StationResponse.class).getId();
        StationLineResponse 신분당선 = 지하철역_노선_등록("신분당선", "bg-red-600", 강남역, 역삼역, 10).as(StationLineResponse.class);

        //then
        List<StationLineResponse> stationLineResponses = 지하철역노선_목록_조회();

        assertThat(stationLineResponses).hasSize(1);
        assertThat(stationLineResponses).containsExactly(신분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getStationLines(){
        //given
        Long 강남역 = 지하철역_등록("강남역").as(StationResponse.class).getId();
        Long 역삼역 = 지하철역_등록("역삼역").as(StationResponse.class).getId();
        Long 선릉역 = 지하철역_등록("선릉역").as(StationResponse.class).getId();

        StationLineResponse 신분당선 = 지하철역_노선_등록("신분당선", "bg-red-600", 강남역, 역삼역, 10).as(StationLineResponse.class);
        StationLineResponse 분당선 = 지하철역_노선_등록("분당선", "bg-green-600", 강남역, 선릉역, 10).as(StationLineResponse.class);

        //when
        List<StationLineResponse> stationLineResponses = 지하철역노선_목록_조회();

        //then
        assertThat(stationLineResponses).hasSize(2);
        assertThat(stationLineResponses).containsExactly(신분당선,분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getStationLine(){
        //given
        Long 강남역 = 지하철역_등록("강남역").as(StationResponse.class).getId();
        Long 역삼역 = 지하철역_등록("역삼역").as(StationResponse.class).getId();
        ExtractableResponse<Response> response = 지하철역_노선_등록("신분당선", "bg-red-600", 강남역, 역삼역, 10);
        String url = response.header("Location");

        //when
        StationLineResponse stationLineResponse = 지하철노선_조회(url);

        //then
        assertThat(stationLineResponse.getName()).isEqualTo("신분당선");
        assertThat(stationLineResponse.getColor()).isEqualTo("bg-red-600");
        assertThat(stationLineResponse.getStations()).containsExactly(new StationResponse(강남역,"강남역"),new StationResponse(역삼역,"역삼역"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateStationLine(){
        //given
        Long 강남역 = 지하철역_등록("강남역").as(StationResponse.class).getId();
        Long 역삼역 = 지하철역_등록("역삼역").as(StationResponse.class).getId();
        String url = 지하철역_노선_등록("신분당선", "bg-red-600", 강남역, 역삼역, 10).header("Location");

        //when
        ExtractableResponse<Response> response  = 지하철노선_수정(url,"다른분당선","bg-red-600");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteStationLine(){
        //given
        Long 강남역 = 지하철역_등록("강남역").as(StationResponse.class).getId();
        Long 역삼역 = 지하철역_등록("역삼역").as(StationResponse.class).getId();
        String url = 지하철역_노선_등록("신분당선", "bg-red-600", 강남역, 역삼역, 10).header("Location");

        //when
        ExtractableResponse<Response> response  = 지하철노선_삭제(url);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    private ExtractableResponse<Response> 지하철역_노선_등록(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        //when
        StationLineRequest request = new StationLineRequest(name,color,upStationId,downStationId,distance);
        ExtractableResponse<Response> response = RestAssured
                .given()
                    .log().all()
                .body(request)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then()
                    .statusCode(201)
                    .log().all()
                    .extract();

        return response;
    }

    private List<StationLineResponse> 지하철역노선_목록_조회() {
        return RestAssured
                .given()
                    .log().all()
                .when()
                    .get("/lines")
                .then()
                    .statusCode(200)
                    .log().all()
                    .extract()
                    .jsonPath().getList("$", StationLineResponse.class);
    }

    private StationLineResponse 지하철노선_조회(String url) {
        return RestAssured
                .given()
                    .log().all()
                .when()
                    .get(url)
                .then()
                    .statusCode(200)
                    .log().all()
                    .extract()
                    .jsonPath().getObject("$", StationLineResponse.class);
    }

    private ExtractableResponse<Response> 지하철노선_수정(String url, String name, String color) {
        StationLineRequest request = new StationLineRequest(name,color);

        return RestAssured
                .given()
                    .log().all()
                    .body(request)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .put(url)
                .then()
                    .log().all()
                    .extract();
    }

    private ExtractableResponse<Response> 지하철노선_삭제(String url) {
        return RestAssured
                .given()
                    .log().all()
                .when()
                    .delete(url)
                .then()
                    .log().all()
                    .extract();
    }
}