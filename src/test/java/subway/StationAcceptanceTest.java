package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import subway.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@ActiveProfiles("acceptance")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        지하철역_생성("강남역");
        // then
        List<String> stationNames = 지하철역_조회();

        assertThat(stationNames).containsAnyOf("강남역");
    }

    @Test
    @DisplayName("조회성공 - 2개 지하철 생성")
    void create_and_search() {
        //Given
        지하철역_생성("잠실역");
        지하철역_생성("신림역");
        //When
        List<String> stationNames = 지하철역_조회();
        //Then
        assertThat(stationNames)
                .hasSize(2)
                .contains("잠실역", "신림역");
    }

    @Test
    @DisplayName("조회실패 - 삭제된 지하철 역")
    void create_and_delete() {
        Long id = 지하철역_생성("굽은다리역");
        지하철역_삭제(id);
        List<String> stationNames = 지하철역_조회();
        assertThat(stationNames).doesNotContain("굽은다리역");
    }

    Long 지하철역_생성(String station) {
        Map<String, String> params = new HashMap<>();
        params.put("name", station);

        return given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract().jsonPath().getLong("id");
    }

    List<String> 지하철역_조회() {
        return given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/stations")
                .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract().jsonPath().getList("name", String.class);
    }

    void 지하철역_삭제(Long id) {
        given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", id)
                .when()
                    .delete("/stations/{id}")
                .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .extract();
    }
}
