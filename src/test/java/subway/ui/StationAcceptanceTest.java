package subway.ui;

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

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        final String 강남역 = "강남역";
        ExtractableResponse<Response> response = generateSubwayStation(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(getSubwayStations()).containsExactly("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void 지하철역_조회() {
        //given
        final String 마들역 = "마들역";
        final String 노원역 = "노원역";

        ExtractableResponse<Response> 마들역_생성 = generateSubwayStation(마들역);
        ExtractableResponse<Response> 노원역_생성 = generateSubwayStation(노원역);

        assertThat(마들역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(노원역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        List<String> names = getSubwayStations();

        //then
        assertThat(names).containsOnly("마들역", "노원역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 제거한다.")
    @Test
    void 지하철역_제거() {
        //given
        final String 마들역 = "마들역";
        ExtractableResponse<Response> 마들역_생성 = generateSubwayStation(마들역);

        assertThat(마들역_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> 마들역_삭제 =
                deleteSubwayStation(마들역_생성.body().jsonPath().getObject("id", Long.class));

        assertThat(마들역_삭제.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        assertThat(getSubwayStations()).doesNotContain(마들역);
    }

    private ExtractableResponse<Response> deleteSubwayStation(Long stationId) {
        return RestAssured
                .given().log().all()
                .pathParam("id", stationId)
                .when().delete("/stations/{id}")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> generateSubwayStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private List<String> getSubwayStations() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

}