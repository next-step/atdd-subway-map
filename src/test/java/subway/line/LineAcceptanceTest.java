package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.station.StationNameConstraints;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.line.LineNameConstraints.*;
import static subway.station.StationNameConstraints.*;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/sql/stations.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given && when
        ExtractableResponse<Response> response = LineAcceptanceFactory.createFixtureLine();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        // then
        LineResponse lineResponse = getLineResponse(response);
        assertThat(lineResponse).extracting("name").isEqualTo(Line2);
        assertThat(lineResponse.getStations()).extracting("name")
                .containsExactlyInAnyOrder(DANG_SAN, HONG_DAE);
    }

    private static LineResponse getLineResponse(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("$", LineResponse.class);
    }
}