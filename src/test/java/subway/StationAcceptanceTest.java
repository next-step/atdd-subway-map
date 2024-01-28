package subway;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.dto.StationRequest;
import subway.station.repository.StationRepository;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @Autowired
    private StationRepository stationRepository;

    @AfterEach
    void tearDown() {
        stationRepository.deleteAll();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철역_생성() {
        // when
        final String stationName = "강남역";
        final StationRequest request = new StationRequest(stationName);
        final ExtractableResponse<Response> response = this.createStation(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        final JsonPath jsonPath = this.getStationList();
        final List<String> stationNames = jsonPath.getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철 목록을 조회한다.")
    @Test
    void 지하철역_목록_조회() {
        // given
        final String stationName1 = "교대역";
        final StationRequest request1 = new StationRequest(stationName1);
        this.createStation(request1);

        final String stationName2 = "역삼역";
        final StationRequest request2 = new StationRequest(stationName2);
        this.createStation(request2);

        // when
        final JsonPath jsonPath = this.getStationList();

        // then
        final List<String> stationNames = jsonPath.getList("name", String.class);

        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).contains("역삼역", "교대역");
        assertThat(stationNames).doesNotContain("강남역");
        assertThat(stationNames).containsExactly("교대역", "역삼역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void 지하철역_삭제() {
        // given
        final String stationName = "강남역";
        final StationRequest request = new StationRequest(stationName);
        final ExtractableResponse<Response> createStationResponse = this.createStation(request);

        // when
        final String location = createStationResponse.header("Location");
        final String stationId = location.replaceAll(".*/(\\d+)$", "$1");

        given()
        .when()
            .delete("/stations/{id}", stationId)
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .log().all();

        // then
        final JsonPath jsonPathAfterStationDeletion = this.getStationList();
        final List<String> stationNames = jsonPathAfterStationDeletion.getList("name", String.class);

        assertThat(stationNames).isEmpty();
        assertThat(stationNames).doesNotContain("강남역");
    }

    private JsonPath getStationList() {
        final JsonPath response =
                given()
                    .log().all()
                .when()
                    .get("/stations")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .log().all()
                .extract()
                    .jsonPath();

        return response;
    }

    private ExtractableResponse<Response> createStation(final StationRequest request) {
        final ExtractableResponse<Response> response =
                given()
                    .log().all()
                    .body(request)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .log().all()
                .extract();

        return response;
    }

}