package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
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
        지하철역_노선_등록("신분당선", "bg-red-600",1L,2L,10);

        //then
        List<StationLineResponse> stationLineResponses = 지하철역_목록_조회();

        assertThat(stationLineResponses).hasSize(1);
        assertThat(stationLineResponses.get(0).getName()).isEqualTo("신분당선");
        assertThat(stationLineResponses.get(0).getColor()).isEqualTo("bg-red-600");
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
        지하철역_노선_등록("신분당선", "bg-red-600",1L,2L,10);
        지하철역_노선_등록("분당선", "bg-green-600",1L,3L,10);

        //when
        List<StationLineResponse> stationLineResponses = 지하철역_목록_조회();

        //then
        assertThat(stationLineResponses).hasSize(2);
        assertThat(stationLineResponses.get(0).getName()).isEqualTo("신분당선");
        assertThat(stationLineResponses.get(0).getColor()).isEqualTo("bg-red-600");
        assertThat(stationLineResponses.get(1).getName()).isEqualTo("분당선");
        assertThat(stationLineResponses.get(1).getColor()).isEqualTo("bg-green-600");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getStationLine(){
        //given
        지하철역_노선_등록("신분당선", "bg-red-600",1L,2L,10);

        //when
        List<StationLineResponse> stationLineResponses = 지하철역_목록_조회();

        //then
        assertThat(stationLineResponses).hasSize(2);
        assertThat(stationLineResponses.get(0).getName()).isEqualTo("신분당선");
        assertThat(stationLineResponses.get(0).getColor()).isEqualTo("bg-red-600");
        assertThat(stationLineResponses.get(1).getName()).isEqualTo("분당선");
        assertThat(stationLineResponses.get(1).getColor()).isEqualTo("bg-green-600");
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
                    .log().all()
                    .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
}