package subway.line;

import io.restassured.response.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static subway.given.GivenStationApi.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private static final String LINE_PATH = "/lines";
    private static final String LINE_1 = "신분당선";
    private static final String BG_COLOR_600 = "bg-color-600";

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
        final Map<String, String> params = new HashMap<>();
        params.put("name", LINE_1);
        params.put("color", BG_COLOR_600);
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        // When
        final var createResponse = given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(LINE_PATH)
                .then().log().all()
                .extract();

        // Then
        assertThat(createResponse.statusCode()).isEqualTo(201);

        final var findResponse = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(LINE_PATH + "/1")
                .then().log().all()
                .extract();
        assertThat(findResponse.jsonPath().getLong("id")).isEqualTo(1L);
        assertThat(findResponse.jsonPath().getString("name")).isEqualTo(LINE_1);
        assertThat(findResponse.jsonPath().getString("color")).isEqualTo(BG_COLOR_600);
        assertThat(findResponse.jsonPath().getString("stations[0].name")).isEqualTo(STATION_1);
        assertThat(findResponse.jsonPath().getString("stations[1].name")).isEqualTo(STATION_2);
    }

}
