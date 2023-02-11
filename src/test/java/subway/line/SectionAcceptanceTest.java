package subway.line;

import io.restassured.response.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.http.HttpStatus;
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
     * Then: 구간이 추가 된다.
     */
    @Test
    @DisplayName("구간 등록")
    void addSection() throws Exception {
        // given
        final var params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "10");

        // when
        final var response = given().log().all()
                .when()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .post("/lines/1/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());

        final var lineResponse = getLineById(LINE_ID_1);
        final var stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds).containsAnyOf(1L, 2L, 3L);
    }
}
