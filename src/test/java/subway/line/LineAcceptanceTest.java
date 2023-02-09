package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.AcceptanceTest;
import subway.presentation.line.dto.response.LineResponse;
import subway.station.StationAcceptanceFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineAcceptanceFactory.*;
import static subway.line.LineNameConstraints.*;
import static subway.station.StationNameConstraints.*;
import static subway.utils.JsonPathUtil.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setUp() {
        StationAcceptanceFactory.createStation(DANG_SAN);
        StationAcceptanceFactory.createStation(HAP_JEONG);
        StationAcceptanceFactory.createStation(HONG_DAE);
        StationAcceptanceFactory.createStation(YEOM_CHANG);
    }

    /**
     * When 한 개의 노선을 생성하면
     * Then 한 개의 노선에는 상행역과 하행역이 존재한다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선_생성() {
        // given && when
        ExtractableResponse<Response> response = createLine(
                Line2,
                "bg-green-600",
                1,
                3,
                10
        );
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        // then
        LineResponse lineResponse = getLineResponse(response);
        assertThat(lineResponse).extracting("name").isEqualTo(Line2);
        assertThat(lineResponse.getStations()).extracting("name")
                .containsExactlyInAnyOrder(DANG_SAN, HONG_DAE);

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 2개의 지하철 노선을 응답 받는다
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록_조회() {
        // given
        createLine(
                Line2,
                "bg-green-600",
                1,
                3,
                10
        );
        createLine(
                Line9,
                "bg-brown-600",
                1,
                4,
                20

        );
        // when
        ExtractableResponse<Response> response = getAllLine();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        //then
        List<LineResponse> lineResponses = getLineResponses(response);
        assertThat(lineResponses).extracting("name")
                .containsExactlyInAnyOrder(Line2, Line9);
    }



    /**
     * Given 1개의 지하철 노선을 생성하고
     * When 지하철 노선을 조회하면
     * Then 1개의 지하철 노선과 그 노선에 포함된 역들을 응답받는다
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void 지하철_노선_조회() {
        // given
        ExtractableResponse<Response> line = createLine(
                Line2,
                "bg-green-600",
                1,
                3,
                10
        );


        Long lineId = getId(line);
        // when
        ExtractableResponse<Response> response = getLine(lineId);
        // then
        LineResponse lineResponse = getLineResponse(response);
        assertThat(lineResponse).extracting("name").isEqualTo(Line2);
        assertThat(lineResponse.getStations()).extracting("name")
                .containsExactlyInAnyOrder(DANG_SAN, HONG_DAE);
    }

    /**
     * Given 1개의 지하철 노선을 생성하고
     * When 지하철 노선을 수정했을 때
     * Then 200 응답을 받는다.
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void 지하철_노선_수정() {
        // given
        ExtractableResponse<Response> line = createLine(
                Line2,
                "bg-green-600",
                1,
                3,
                10
        );
        Long lineId = getId(line);
        // when
        ExtractableResponse<Response> response = updateLine(
                lineId,
                Line1,
                "bg-blue-600"
        );
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 1개의 지하철 노선을 생성하고
     * When 그 지하철 노선을 삭제하면
     * Then 지하철 노선 목록 조회 시 그 지하철 노선을 찾을 수 없다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void 지하철_노선_삭제() {
        // given
        ExtractableResponse<Response> line = createLine(
                Line2,
                "bg-green-600",
                1,
                3,
                10
        );
        Long lineId = getId(line);
        // when
        ExtractableResponse<Response> response = deleteLine(lineId);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}