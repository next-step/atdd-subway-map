package subway.line;

import io.restassured.response.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.annotation.*;
import subway.given.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static subway.given.GivenLineApi.*;
import static subway.given.GivenStationApi.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @BeforeEach
    void setUp() {
        createStationApi(STATION_1);
        createStationApi(STATION_2);
        createStationApi(STATION_3);
    }

    @Test
    @DisplayName("지하철 노선 등록")
    void createLine() throws Exception {
        // Given
        final ExtractableResponse<Response> response =
                GivenLineApi.createLine(LINE_1, STATION_ID_1, STATION_ID_2);

        // Then
        assertThat(response.statusCode()).isEqualTo(201);

        assertThat(response.jsonPath().getLong("id")).isEqualTo(1L);
        assertThat(response.jsonPath().getString("name")).isEqualTo(LINE_1);
        assertThat(response.jsonPath().getString("color")).isEqualTo(BG_COLOR_600);
        assertThat(response.jsonPath().getString("stations[0].name")).isEqualTo(STATION_1);
        assertThat(response.jsonPath().getString("stations[1].name")).isEqualTo(STATION_2);
    }

    @Test
    @DisplayName("지하철 목록 조회")
    void getLines() throws Exception {
        // Given
         GivenLineApi.createLine(LINE_1, STATION_ID_1, STATION_ID_2);
         GivenLineApi.createLine(LINE_2, STATION_ID_1, STATION_ID_3);

        // When
        final var response = getAllLines();

        // Then
        final var names = response.jsonPath().getList("name", String.class);
        assertThat(names).containsAnyOf(LINE_1, LINE_2);

        final var stations1 = response.jsonPath().getList("stations[0].id", Long.class);
        assertThat(stations1).containsAnyOf(1L, 2L);

        final var stations2 = response.jsonPath().getList("stations[1].id", Long.class);
        assertThat(stations2).containsAnyOf(1L, 3L);
    }

    private static ExtractableResponse<Response> getAllLines() {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(LINE_PATH)
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("노선 수정")
    void updateLine() throws Exception {
        // Given
        GivenLineApi.createLine(LINE_1, STATION_ID_1, STATION_ID_2);

        final var updateName = "수정분당선";
        final var updateColor = "bg-red-600";

        final var params = new HashMap<>();
        params.put("name", updateName);
        params.put("color", updateColor);

        // When
        final var response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .put(LINE_PATH + "/1")
                .then().log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(200);

        final var findResponse = getLineById(LINE_ID_1);
        assertThat(findResponse.jsonPath().getString("name")).isEqualTo(updateName);
        assertThat(findResponse.jsonPath().getString("color")).isEqualTo(updateColor);
    }

    @Test
    @DisplayName("노선 삭제")
    void deleteLine() throws Exception {
        // Given
        GivenLineApi.createLine(LINE_1, STATION_ID_1, STATION_ID_2);

        // When
        final var response = given().log().all()
                .when()
                .delete(LINE_PATH + "/1")
                .then().log().all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(204);

        final var findResponses = getAllLines();
        assertThat(findResponses.jsonPath().getList("id", Long.class)).isEmpty();
    }

}
