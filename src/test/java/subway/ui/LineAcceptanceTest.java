package subway.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.util.AbstractAcceptanceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.ui.LineSteps.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //when
        final String 신분당선 = "신분당선";
        지하철_노선_생성_요청(신분당선);

        //then
        assertThat(지하철_노선_목록_조회_요청()).containsExactly(신분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void showLines() {
        //given
        final String 신분당선 = "신분당선";
        final String 분당선 = "분당선";
        지하철_노선_여러개_생성_요청(List.of(신분당선, 분당선));

        //when
        List<String> subwayLines = 지하철_노선_목록_조회_요청();

        //then
        assertThat(subwayLines).containsOnly(분당선, 신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void showLine() {
        //given
        final String 신분당선 = "신분당선";
        LineResponse 신분당선_생성_응답 = 지하철_노선_생성_요청_Response_반환(신분당선);

        //when
        LineResponse 신분당선_조회_응답 = 지하철_노선_조회_요청(신분당선_생성_응답.getId());

        //then
        assertThat(신분당선_조회_응답.getName()).isEqualTo(신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {
        //given
        final String 신분당선 = "신분당선";
        LineResponse 신분당선_생성_응답 = 지하철_노선_생성_요청_Response_반환(신분당선);

        //when
        final String 다른분당선 = "다른분당선";
        LineUpdateRequest request = new LineUpdateRequest(다른분당선, "bg-red-600");
        LineResponse 다른분당선_수정_응답 = 지하철_노선_수정_요청(신분당선_생성_응답.getId(), request);

        //then
        assertThat(다른분당선_수정_응답.getName()).isEqualTo(다른분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine() {
        //given
        final String 신분당선 = "신분당선";
        LineResponse 신분당선_생성_응답 = 지하철_노선_생성_요청_Response_반환(신분당선);

        //when
        ExtractableResponse<Response> 신분당선_삭제_응답 = 지하철_노선_삭제_요청(신분당선_생성_응답.getId());

        //then
        assertThat(신분당선_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선에 구간 등록을 하면
     * Then 지하철 노선 조회 시 새로운 구간의 하행역이 조회된다
     */
    @Test
    @DisplayName("지하철 노선에 구간 등록")
    void createSection() {
        //given
        StationResponse 신사역 = StationSteps.지하철역_생성_요청_Response_반환("신사역");
        StationResponse 논현역 = StationSteps.지하철역_생성_요청_Response_반환("논현역");
        LineResponse 신분당선 = 지하철_노선_생성_요청_Response_반환("신분당선", 신사역.getId(), 논현역.getId());

        //when
        final String 신논현역명 = "신논현역";
        StationResponse 신논현역 = StationSteps.지하철역_생성_요청_Response_반환(신논현역명);
        지하철_노선_구간_등록_요청(신분당선.getId(), new SectionRequest(논현역.getId(), 신논현역.getId(), 5L));

        //then
        List<String> stationNames = 지하철_노선_조회_요청_노선에_속한_역_반환(신분당선.getId());
        assertThat(stationNames).contains(신논현역명);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 노선의 하행 종점역이 아닌 상행역을 가진 구간을 등록하면
     * Then 예외를 발생한다.
     */
    @Test
    @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닌 경우")
    void 새로운_구간의_상행역이_해당_노선에_등록되어있는_하행_종점역이_아닌_경우() {
        //given
        StationResponse 신사역 = StationSteps.지하철역_생성_요청_Response_반환("신사역");
        StationResponse 논현역 = StationSteps.지하철역_생성_요청_Response_반환("논현역");
        LineResponse 신분당선 = 지하철_노선_생성_요청_Response_반환("신분당선", 신사역.getId(), 논현역.getId());

        //when
        final String 신논현역명 = "신논현역";
        StationResponse 신논현역 = StationSteps.지하철역_생성_요청_Response_반환(신논현역명);
        ExtractableResponse<Response> 지하철_노선_구간_등록_응답 =
                지하철_노선_구간_등록_요청(신분당선.getId(), new SectionRequest(신사역.getId(), 신논현역.getId(), 5L));

        //then
        assertThat(지하철_노선_구간_등록_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 노선에 등록되어있는 역이 하행역인 구간을 등록하면
     * Then 예외를 발생한다.
     */
    @Test
    @DisplayName("새로운 구간의 하행역이 해당 노선에 등록되어있는 역인 경우")
    void 새로운_구간의_하행역이_해당_노선에_등록되어있는_역인_경우() {
        //given

        //when

        //then
    }

    /**
     * Given 3개의 역을 가지는 지하철 노선을 생성하고
     * When 지하철 노선에 등록된 하행 종점역을 제거하면
     * Then 지하철 노선 조회 시 제거한 종점역은 존재하지 않는다.
     */
    @Test
    @DisplayName("지하철 노선에 구간 제거")
    void deleteSection() {
        //given

        //when

        //then
    }

    /**
     * Given 3개의 역을 가지는 지하철 노선을 생성하고
     * When 지하철 노선의 하행 종점역이 아닌 역을 제거하면
     * Then 예외를 발생한다.
     */
    @Test
    @DisplayName("지하철 노선의 하행 종점역이 아닌 역을 제거하는 경우")
    void 지하철_노선의_하행_종점역이_아닌_역을_제거하는_경우() {
        //given

        //when

        //then
    }

    /**
     * Given 상행 종점역과 하행 종점역만 등록된 노선을 생성하고
     * When 지하철 노선의 하행 종점역을 제거하면
     * Then 예외를 발생한다.
     */
    @Test
    @DisplayName("상행 종점역과 하행 종점역만 등록된 지하철 노선의 하행 종점역을 제거하는 경우")
    void 상행_종점역과_하행_종점역만_등록된_지하철_노선의_하행_종점역을_제거하는_경우() {
        //given

        //when

        //then
    }
}
