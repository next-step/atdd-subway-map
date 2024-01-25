package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.StationRequest;

import java.util.List;

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
        // given
        String gasan = "가산디지털단지역";
        
        createStationRequest(gasan);

        // then
        List<String> stationNames =
                RestAssured.given()
                        .when()
                            .get("/stations")
                        .then()
                        .extract().jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf(gasan);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 2개를 생성하고, 목록을 조회한다.")
    void showStations() {
        //given
        String gasan = "가산디지털단지역";
        String guro = "구로디지털단지역";

        createStationRequest(gasan);
        createStationRequest(guro);

        // when
        List<String> stationNames =
                RestAssured
                        .when()
                            .get("/stations")
                        .then()
                            .statusCode(HttpStatus.OK.value())
                        .extract()
                            .jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames).containsAnyOf(gasan);
        assertThat(stationNames).containsAnyOf(guro);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @Test
    @DisplayName("지하철역이 생성되고, 삭제된다.")
    void deleteStation() {
        //given
        String gasan = "가산디지털단지역";

        createStationRequest(gasan);

        // when
        RestAssured
                .when()
                .delete("/stations/" + 1)
                    .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // when
        List<String> stationNames =
                RestAssured
                        .when()
                            .get("/stations")
                        .then()
                            .statusCode(HttpStatus.OK.value())
                        .extract()
                            .jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames).hasSize(0);

    }

    /**
     * 주어진 지하철역 이름으로 지하철역 생성 요청 및 상태 코드 검증
     * 
     * @param stationName 지하철역 이름
     */
    private static void createStationRequest(String stationName) {
        RestAssured
                .given()
                    .body(new StationRequest(stationName))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
    }

}