package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.station.StationRestAssuredTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.line.LineStationAssuredTemplate.*;
import static nextstep.subway.acceptance.station.StationRestAssuredTemplate.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    private static final String 신분당선 = "신분당선";
    private static final String 분당선 = "분당선";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COLOR = "color";

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // when 지하철 노선을 생성하면
        ExtractableResponse<Response> 신분당선_생성 = 노선_생성(신분당선, "bg-red-600", 1L, 2L, 10);
        assertThat(신분당선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        ExtractableResponse<Response> 노선_목록_조회 = 노선_목록_조회();
        assertThat(노선_목록_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Object> lineNames = 노선_목록_이름_조회(노선_목록_조회);
        assertThat(lineNames).contains(신분당선);
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void findLines() {
        // given 2개의 지하철 노선을 생성하고
        ExtractableResponse<Response> 강남역 = 지하철역_생성("강남역");
        ExtractableResponse<Response> 분당역 = 지하철역_생성("분당역");
        System.out.println(강남역.jsonPath().getString("id"));

        Long 강남역_ID = Long.parseLong(강남역.jsonPath().getString("id"));
        Long 분당역_ID = Long.parseLong(분당역.jsonPath().getString("id"));

        ExtractableResponse<Response> 신분당선_생성 = 노선_생성(신분당선, "bg-red-600", 강남역_ID, 분당역_ID, 10);
        assertThat(신분당선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> 분당선_생성 = 노선_생성(분당선, "bg-green-600", 강남역_ID, 분당역_ID, 10);
        assertThat(분당선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 지하철 노선 목록을 조회하면
        ExtractableResponse<Response> 노선_목록_조회 = 노선_목록_조회();
        assertThat(노선_목록_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        List<Object> lineNames = 노선_목록_이름_조회(노선_목록_조회);
        assertAll(
            () -> assertThat(lineNames).hasSize(2),
            () -> assertThat(lineNames).contains(분당선),
            () -> assertThat(lineNames).contains(신분당선)
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void findLine() {
        // given 지하철 노선을 생성하고
        ExtractableResponse<Response> 신분당선_생성 = 노선_생성(신분당선, "bg-red-600", 1L, 2L, 10);
        assertThat(신분당선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String 신분당선_ID = 노선_정보_조회(신분당선_생성, ID);

        // when 생성한 지하철 노선을 조회하면
        ExtractableResponse<Response> 노선_조회 = 노선_조회(신분당선_ID);
        assertThat(노선_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        String 노선_이름 = 노선_정보_조회(노선_조회, NAME);
        assertThat(노선_이름).isEqualTo(신분당선);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void editLine() {
        // given 지하철 노선을 생성하고
        ExtractableResponse<Response> 신분당선_생성 = 노선_생성(신분당선, "bg-red-600", 1L, 2L, 10);
        assertThat(신분당선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String 신분당선_ID = 노선_정보_조회(신분당선_생성, ID);

        // when 생성한 지하철 노선을 수정하면
        ExtractableResponse<Response> 노선_수정 = 노선_수정(신분당선_ID, "다른분당선", "bg-red-600");
        assertThat(노선_수정.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then 해당 지하철 노선 정보는 수정된다
        ExtractableResponse<Response> 노선_조회 = 노선_조회(신분당선_ID);
        assertThat(노선_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        String 수정된_노선_이름 = 노선_정보_조회(노선_조회, NAME);
        String 수정된_노선_색 = 노선_정보_조회(노선_조회, COLOR);

        assertAll(
            () -> assertThat(수정된_노선_이름).isEqualTo("다른분당선"),
            () -> assertThat(수정된_노선_색).isEqualTo("bg-red-600")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    @DisplayName("지하철 노선을 삭제한다.")
    void deleteLine() {
        // given 지하철 노선을 생성하고
        ExtractableResponse<Response> 신분당선_생성 = 노선_생성(신분당선, "bg-red-600", 1L, 2L, 10);
        assertThat(신분당선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String 신분당선_ID = 노선_정보_조회(신분당선_생성, ID);

        // when 생성한 지하철 노선을 삭제하면
        ExtractableResponse<Response> 노선_삭제 = 노선_삭제(신분당선_ID);
        assertThat(노선_삭제.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then 해당 지하철 노선 정보는 삭제된다
        ExtractableResponse<Response> 노선_목록_조회 = 노선_목록_조회();
        assertThat(노선_목록_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Object> lineNames = 노선_목록_이름_조회(노선_목록_조회);
        assertThat(lineNames).doesNotContain(신분당선);
    }

    private java.util.List<Object> 노선_목록_이름_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(NAME);
    }

    private String 노선_정보_조회(ExtractableResponse<Response> response, String data) {
        return response.jsonPath().getString(data);
    }

}
