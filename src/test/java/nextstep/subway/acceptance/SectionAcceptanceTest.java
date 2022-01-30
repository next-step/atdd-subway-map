package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.LineSteps;
import nextstep.subway.acceptance.step.SectionSteps;
import nextstep.subway.acceptance.step.StationSteps;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionAcceptanceTest extends AcceptanceTest {
    final String 이름 = "name";
    final String 번호 = "id";

    final Map<String, String> 광교역 = Map.of(이름, "광교역");
    final Map<String, String> 양재역 = Map.of(이름, "양재역");
    final Map<String, String> 판교역 = Map.of(이름, "판교역");

    final int distance = 100;

    /**
     * When 지하철 구간 생성을 요청하면
     * Then 지하철 구간 생성이 성공한다.
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void createSection() {
        // given
        final Long 광교역_번호 = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);
        final Long 양재역_번호 = StationSteps.지하철_역_생성_요청(양재역).jsonPath().getLong(번호);
        final Long 판교역_번호 = StationSteps.지하철_역_생성_요청(판교역).jsonPath().getLong(번호);

        // when
        final Long lineId = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", 광교역_번호, 양재역_번호, distance)
                .jsonPath().getLong(번호);

        ExtractableResponse<Response> createResponse = SectionSteps.지하철_구간_생성_요청(lineId, 양재역_번호, 판교역_번호, distance);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철 노선과 구간이 있는 상태에서
     * When 생성한 지하철 마지막 구간 삭제를 요청 하면
     * Then 생성한 지하철 구간 삭제가 성공한다.
     */
    @DisplayName("지하철 구간 삭제")
    @Test
    void deleteSection() {
        // given
        final Long 광교역_번호 = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);
        final Long 양재역_번호 = StationSteps.지하철_역_생성_요청(양재역).jsonPath().getLong(번호);
        final Long 판교역_번호 = StationSteps.지하철_역_생성_요청(판교역).jsonPath().getLong(번호);

        // when
        final Long lineId = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", 광교역_번호, 양재역_번호, distance)
                .jsonPath().getLong(번호);

        SectionSteps.지하철_구간_생성_요청(lineId, 양재역_번호, 판교역_번호, distance);

        ExtractableResponse<Response> deleteReponse = SectionSteps.지하철_구간_삭제_요청(lineId, 판교역_번호);

        assertThat(deleteReponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선과 구간이 있는 상태에서
     * When 생성한 지하철 마지막 구간이 아닌 역을 삭제 요청하면
     * Then 생성한 지하철 구간 삭제가 실패한다.
     */
    @DisplayName("마지막 구간 하행 종점이 아닌 역으로 구간 삭제 요청")
    @Test
    void deleteNotLastSection() {
        // given
        final Long 광교역_번호 = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);
        final Long 양재역_번호 = StationSteps.지하철_역_생성_요청(양재역).jsonPath().getLong(번호);
        final Long 판교역_번호 = StationSteps.지하철_역_생성_요청(판교역).jsonPath().getLong(번호);

        // when
        final Long lineId = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", 광교역_번호, 양재역_번호, distance)
                .jsonPath().getLong(번호);

        SectionSteps.지하철_구간_생성_요청(lineId, 양재역_번호, 판교역_번호, distance);

        ExtractableResponse<Response> deleteReponse = SectionSteps.지하철_구간_삭제_요청(lineId, 광교역_번호);

        assertThat(deleteReponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    /**
     * Given 지하철 구간이 하나만 생성한 상태에서
     * When 지하철 마지막 구간을 삭제 요청하면
     * Then 생성한 지하철 구간 삭제가 실패한다.
     */
    @DisplayName("구간이 하나만 있을 때 삭제 요청")
    @Test
    void deleteOnlyOneSection() {
        // given
        final Long 광교역_번호 = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);
        final Long 양재역_번호 = StationSteps.지하철_역_생성_요청(양재역).jsonPath().getLong(번호);

        // when
        final Long lineId = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", 광교역_번호, 양재역_번호, distance)
                .jsonPath().getLong(번호);

        ExtractableResponse<Response> deleteReponse = SectionSteps.지하철_구간_삭제_요청(lineId, 양재역_번호);

        assertThat(deleteReponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 구간 생성을 요청 하고
     * Given 새로운 지하철 구간 생성을 요청하고
     * When 지하철 구간 목록 조회를 요청 하면
     * Then 해당 노선의 모든 지하철 역 목록을 응답받는다
     */
    @DisplayName("지하철 구간 목록 조회")
    @Test
    void getSections() {
        final Long 광교역_번호 = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);
        final Long 양재역_번호 = StationSteps.지하철_역_생성_요청(양재역).jsonPath().getLong(번호);
        final Long 판교역_번호 = StationSteps.지하철_역_생성_요청(판교역).jsonPath().getLong(번호);

        final Long lineId = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", 광교역_번호, 양재역_번호, distance)
                .jsonPath().getLong(번호);

        SectionSteps.지하철_구간_생성_요청(lineId, 양재역_번호, 판교역_번호, distance);

        //when
        ExtractableResponse<Response> searchResponse = SectionSteps.지하철_구간_조회_요청(lineId);

        // then
        assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> stationNames = searchResponse.jsonPath().getList("stations", Station.class)
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertThat(stationNames).contains("광교역", "양재역", "판교역");
    }
}
