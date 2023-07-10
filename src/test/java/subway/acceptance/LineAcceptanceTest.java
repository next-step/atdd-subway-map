package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.web.LineResponse;
import subway.station.web.StationResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.LineRequestFixture.*;
import static subway.acceptance.StationRequestFixture.지하철_역_생성;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * Given 2개의 지하철 역을 생성하고
     * When (해당 지하철 역을 포함하는) 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성 한다.")
    @Test
    void createLine() {
        // given
        Long 강남역 = 지하철_역_생성("강남역");
        Long 양재역 = 지하철_역_생성("양재역");

        // when
        지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10L);

        // then
        List<String> lineNames = 지하철_노선_목록_조회().stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선을 목록을 조회 한다.")
    @Test
    void getLines() {
        // given
        Long 강남역 = 지하철_역_생성("강남역");
        Long 양재역 = 지하철_역_생성("양재역");
        Long 역삼역 = 지하철_역_생성("역삼역");
        Long 선릉역 = 지하철_역_생성("선릉역");

        지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10L);
        지하철_노선_생성("2호선", "bg-green-600", 역삼역, 선릉역, 10L);

        // when & then
        List<String> lineNames = 지하철_노선_목록_조회().stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());
        assertThat(lineNames).containsAnyOf("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회 한다.")
    @Test
    void getLine() {
        // given
        Long 강남역 = 지하철_역_생성("강남역");
        Long 양재역 = 지하철_역_생성("양재역");

        Long 신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10L);

        // when
        LineResponse 신분당선_응답 = 지하철_노선_조회(신분당선);

        assertThat(신분당선_응답.getId()).isEqualTo(신분당선);
        assertThat(신분당선_응답.getName()).isEqualTo("신분당선");
        assertThat(신분당선_응답.getColor()).isEqualTo("bg-red-600");

        List<Long> stationNames = 신분당선_응답.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(stationNames).containsAnyOf(강남역, 양재역);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정 한다.")
    @Test
    void updateLine() {
        // given
        Long 강남역 = 지하철_역_생성("강남역");
        Long 역삼역 = 지하철_역_생성("역삼역");

        Long 신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 역삼역, 10L);

        // when
        지하철_노선_수정(신분당선, "2호선", "bg-green-600");

        // then
        LineResponse 신분당선_응답 = 지하철_노선_조회(신분당선);

        assertThat(신분당선_응답.getName()).isEqualTo("2호선");
        assertThat(신분당선_응답.getColor()).isEqualTo("bg-green-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제 한다.")
    @Test
    void deleteLIne() {
        // given
        Long 강남역 = 지하철_역_생성("강남역");
        Long 양재역 = 지하철_역_생성("양재역");

        Long 신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10L);

        // when
        지하철_노선_삭제(신분당선);

        // then
        List<String> lineNames = 지하철_노선_목록_조회().stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());
        assertThat(lineNames).doesNotContain("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 새로운 구간을 등록하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void addSection() {
        // given
        Long 강남역 = 지하철_역_생성("강남역");
        Long 양재역 = 지하철_역_생성("양재역");
        Long 양재시민의숲역 = 지하철_역_생성("양재시민의숲역");

        Long 신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10L);

        // when
        지하철_구간_등록(신분당선, 양재역, 양재시민의숲역, 10L);

        // then
        List<String> lineNames = 지하철_노선_목록_조회().stream()
                .map(LineResponse::getStations)
                .flatMap(Collection::stream)
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(lineNames).containsAnyOf("양재시민의숲역");
    }

}
