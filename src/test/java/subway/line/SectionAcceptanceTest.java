package subway.line;

import io.restassured.response.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.annotation.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static subway.given.GivenLineApi.*;
import static subway.given.GivenStationApi.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    @BeforeEach
    void setUp() {
        createStationApi(STATION_1);
        createStationApi(STATION_2);
        createStationApi(STATION_3);
        createLine(LINE_1, STATION_ID_1, STATION_ID_2);
    }

    /**
     * Given: 노선에 구간이 등록되어 있을 때,
     * When: 구간을 추가 등록하면
     * Then: 목록 조회 시 추가된 구간을 확인할 수 있다.
     */
    @Test
    @DisplayName("구간 등록")
    void addSection() throws Exception {
        // Given
        final var params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "10");

        // When
        final var response = addSection(params);

        // Then
        assertThat(response.statusCode()).isEqualTo(OK.value());

        final var lineResponse = getLineById(LINE_ID_1);
        final var stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds).containsAnyOf(1L, 2L, 3L);
    }

    private static ExtractableResponse<Response> addSection(HashMap<Object, Object> params) {
        return given().log().all()
                .when()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .post("/lines/1/sections")
                .then().log().all()
                .extract();
    }

    /**
     * Given: 노선에 구간이 등록되어 있고,
     * When: 구간을 추가 등록할 때,
     * Then: 상행역이 노선에 등록되어 있는 하행 종점역이 아니면 예외가 발생한다.
     */
    @Test
    @DisplayName("구간 등록 - 상행역은 노선에 등록되어 있는 하행 종점역이 아니면 예외가 발생한다.")
    void addSectionThrow1() throws Exception {
        // Given
        final var params = new HashMap<>();
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "10");

        // When
        final var response = addSection(params);
        
        // Then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    /**
     * Given: 노선에 구간이 등록되어 있고,
     * When: 구간을 추가 등록할 때,
     * Then: 새로운 구간의 하행역이 해당 노선에 등록되어 있는 역이면 예외가 발생한다.
     */
    @Test
    @DisplayName("구간 등록 - 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    void addSectionThrow2() throws Exception {
        // Given
        final var params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "1");
        params.put("distance", "10");

        // When
        final var response = addSection(params);

        // Then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }
}
