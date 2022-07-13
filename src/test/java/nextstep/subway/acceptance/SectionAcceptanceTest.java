package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.LineRequestCollection.지하철_노선_생성;
import static nextstep.subway.acceptance.LineRequestCollection.지하철_단일_노선_조회;
import static nextstep.subway.acceptance.SectionRequestCollection.지하철_구간_등록;
import static nextstep.subway.acceptance.SectionRequestCollection.지하철_구간_삭제;
import static nextstep.subway.acceptance.StationRequestCollection.성수역_생성;
import static nextstep.subway.acceptance.StationRequestCollection.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 괸련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    Long upStationId;
    Long downStationId;

    @BeforeEach
    void init() {
        upStationId = 지하철역_생성("강남역").jsonPath().getLong("id");
        downStationId = 지하철역_생성("건대입구역").jsonPath().getLong("id");
    }

    /**
     * Given 지하철 노선을 1개 생성
     * Given 추가할 역 1개 생성
     * When 기존의 노선에 등록된 하행역을 상행역으로 하고
     * When 새로 생성한 역을 하행역으로 하고
     * When 구간 생성 요청을 하면
     * Then 정상적으로 구간이 노선에 추가된다.
     */
    @Test
    @DisplayName("정상적으로 역을 생성하여 기존 노선 끝에 구간을 추가한다")
    public void addSection() {
        // given
        Long lineId = 지하철_노선_생성("2호선", "bg-blue-600", upStationId, downStationId, 10).jsonPath().getLong("id");
        Long stationId = 성수역_생성();

        // when
        int statusCode = 지하철_구간_등록(lineId, downStationId, stationId, 1);

        // then
        ExtractableResponse<Response> response = 지하철_단일_노선_조회(lineId);
        List<String> stationList = response.jsonPath().getList("stations.name", String.class);
        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationList).containsExactly("강남역", "건대입구역", "성수역")
        );
    }

    /**
     * Given 지하철 노선을 1개 생성
     * Given 추가할 역 1개 생성
     * When 기존 노선에 등록된 상행역을 상행역으로 하고
     * When 새로 생성한 역을 하행역으로 하고
     * When 구간 생성 요청을 하면
     * Then 예외를 발생시킨다
     */
    @Test
    @DisplayName("기존 노선의 하행이 신규 구간의 상행과 일치하지 않는경우 실패한다")
    public void addSectionWithInvalidUpStation() {
        // given
        Long lineId = 지하철_노선_생성("2호선", "bg-blue-600", upStationId, downStationId, 10).jsonPath().getLong("id");
        Long stationId = 성수역_생성();

        // when
        int statusCode = 지하철_구간_등록(lineId, upStationId, stationId, 3);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 1개 생성
     * When 기존의 노선에 등록된 하행역을 상행역으로 하고
     * When 기존 노선에 등록되있는 역중 하나를 하행역으로 하고
     * When 구간 생성 요청을 하면
     * Then 예외를 발생시킨다
     */
    @Test
    @DisplayName("기존 노선에 있는 역은 새 구간의 하행역이 될수없다")
    public void addSectionWithInvalidDownStation() {
        // given
        Long lineId = 지하철_노선_생성("2호선", "bg-blue-600", upStationId, downStationId, 10).jsonPath().getLong("id");
        성수역_생성();

        // when
        int statusCode = 지하철_구간_등록(lineId, downStationId, upStationId, 3);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 1개 생성
     * Given 추가할 역 1개 생성
     * Given 구간을 생성
     * When 지하철 구간 제거를 요청하면
     * Then 정상 삭제된다
     */
    @Test
    @DisplayName("지하철 노선에 등록된 하행 종점역을 제거할 수 있다")
    public void deleteSection() {
        // given
        Long lineId = 지하철_노선_생성("2호선", "bg-blue-600", upStationId, downStationId, 10).jsonPath().getLong("id");
        Long stationId = 성수역_생성();
        지하철_구간_등록(lineId, downStationId, stationId, 1);

        // when
        int statusCode = 지하철_구간_삭제(lineId, stationId);

        // then
        ExtractableResponse<Response> response = 지하철_단일_노선_조회(lineId);
        List<String> stationNames = response.jsonPath().getList("station.name", String.class);

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(stationNames).doesNotContain("성수역")
        );
    }

    /**
     * Given : 지하철 노선을 1개 생성
     * Given : 추가할 역 1개 생성
     * Given : 구간을 생성
     * When : 하행 종점역이 아닌 지하철 구간 제거를 요청하면
     * Then : 예외를 발생한다.
     */
    @Test
    @DisplayName("하행 종점역이 아닌 역을 삭제요청할 경우 예외를 발생시킨다")
    public void deleteSectionUnsuitableStation() {
        // given
        Long lineId = 지하철_노선_생성("2호선", "bg-blue-600", upStationId, downStationId, 10).jsonPath().getLong("id");
        Long stationId = 성수역_생성();
        지하철_구간_등록(lineId, downStationId, stationId, 1);

        // when
        int statusCode = 지하철_구간_삭제(lineId, upStationId);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 1개 생성
     * When 노선에 역이 딱 하행역, 상행역 2개만 있는 경우에 삭제를 요청하면
     * Then 예외를 발생한다.
     */
    @Test
    @DisplayName("지하철 노선에 상행, 하행 역 2개만 있는 경우 삭제할수 없다")
    public void deleteWithOnlyOneSection() {
        // given
        Long lineId = 지하철_노선_생성("2호선", "bg-blue-600", upStationId, downStationId, 10).jsonPath().getLong("id");

        // when
        int statusCode = 지하철_구간_삭제(lineId, downStationId);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
