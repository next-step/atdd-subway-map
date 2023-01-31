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
import subway.presentation.line.dto.response.LineResponse;
import subway.station.StationAcceptanceFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.line.LineNameConstraints.Line2;
import static subway.line.LineNameConstraints.Line9;
import static subway.station.StationNameConstraints.*;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        StationAcceptanceFactory.createStation(DANG_SAN);
        StationAcceptanceFactory.createStation(HAP_JEONG);
        StationAcceptanceFactory.createStation(HONG_DAE);
        StationAcceptanceFactory.createStation(YEOM_CHANG);
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

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 2개의 지하철 노선을 응답 받는다
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getAllLines() {
        // given
        LineAcceptanceFactory.createFixtureLine();
        LineAcceptanceFactory.createLine(
                Line9,
                "bg-brown-600",
                1,
                4,
                20

        );
        // when
        ExtractableResponse<Response> response = LineAcceptanceFactory.getAllLine();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        //then
        List<LineResponse> lineResponses = getLineResponses(response);
        assertThat(lineResponses).extracting("name")
                .containsExactlyInAnyOrder(Line2, Line9);
    }

    private static List<LineResponse> getLineResponses(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("$", LineResponse.class);
    }
}