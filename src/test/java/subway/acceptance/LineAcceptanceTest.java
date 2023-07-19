package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.line.web.LineResponse;
import subway.station.web.StationResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.LineRequestFixture.*;
import static subway.acceptance.StationRequestFixture.지하철_역_생성;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    Long 강남역;
    Long 양재역;
    Long 양재시민의숲역;
    Long 청계산입구역;
    Long 판교역;

    @BeforeEach
    void beforeEach() {
        강남역 = 지하철_역_생성("강남역");
        양재역 = 지하철_역_생성("양재역");
        양재시민의숲역 = 지하철_역_생성("양재시민의숲역");
        청계산입구역 = 지하철_역_생성("청계산입구역");
        판교역 = 지하철_역_생성("판교역");
    }

    /**
     * Given 2개의 지하철 역을 생성하고
     * When (해당 지하철 역을 포함하는) 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성 한다.")
    @Test
    void createLine() {
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
        지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10L);
        지하철_노선_생성("2호선", "bg-green-600", 양재시민의숲역, 청계산입구역, 10L);

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
        Long 신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10L);

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

    /**
     * Given 지하철 노선을 생성하고
     * When 하행 종점이 아닌 역으로 새로운 구간을 등록하면
     * Then 요청이 실패 한다.
     */
    @Test
    void addSectionFail() {
        // given
        Long 신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10L);

        // when
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(신분당선, 판교역, 양재시민의숲역, 10L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 생성하고 새로운 구간이 등록 되었을 때
     * When 마지막 구간을 삭제하면
     * Then 지하철 노선 목록 조회 시 삭제한 노선을 찾을 수 없다
     *
     */
    @Test
    void removeSection() {
        // given
        Long 신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10L);
        Long sectionId = 지하철_구간_등록(신분당선, 양재역, 양재시민의숲역, 10L);

        // when
        지하철_구간_삭제(신분당선, sectionId);

        // then
        List<String> lineNames = 지하철_노선_목록_조회().stream()
                .map(LineResponse::getStations)
                .flatMap(Collection::stream)
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(lineNames).doesNotContain("양재시민의숲역");
    }

    /**
     * Given 지하철 노선을 생성하고 2개의 구간을 추가로 등록 했을 때
     * When 마지막 구간이 아닌 구간을 삭제 요청 하면
     * Then 요청이 실패 한다.
     *
     */
    @Test
    void removeSectionFail() {
        // given
        Long 신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, 10L);
        Long 양재_양재시민의숲 = 지하철_구간_등록(신분당선, 양재역, 양재시민의숲역, 10L);
        Long 양재시민의숲_청계산입구 = 지하철_구간_등록(신분당선, 양재시민의숲역, 청계산입구역, 10L);

        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(신분당선, 양재_양재시민의숲);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
