package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선도_등록("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<StationResponse> stations = 지하철_노선도_리스트_반환();
        assertThat(stations).hasSize(1);

        StationResponse 강남역 = stations.get(0);
        assertThat(강남역.getId()).isEqualTo(1L);
        assertThat(강남역.getName()).isEqualTo("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철 역을 조회한다.")
    @Test
    void showStations() {
        //given
        지하철_노선도_등록("판교역");
        지하철_노선도_등록("정자역");

        //when
        ExtractableResponse<Response> response = 지하철_노선도_조회();
        List<StationResponse> stations = 지하철_노선도_리스트_반환();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stations).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철 역을 삭제한다")
    @Test
    void deleteStation() {
        //given
        Long savedId = 지하철_노선도_등록_Id_획득("판교역");

        //when
        ExtractableResponse<Response> response = 지하철_노선도_삭제(savedId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        List<StationResponse> stations = 지하철_노선도_리스트_반환();
        assertThat(stations).isEmpty();
    }

    private ExtractableResponse<Response> 지하철_노선도_삭제(Long id) {
        String pathVariable = "/" + id;
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations" + pathVariable)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선도_등록(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private Long 지하철_노선도_등록_Id_획득(String name) {
        return 지하철_노선도_등록(name).jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 지하철_노선도_조회() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private List<StationResponse> 지하철_노선도_리스트_반환() {
        return 지하철_노선도_조회().jsonPath().getList("", StationResponse.class);
    }
}