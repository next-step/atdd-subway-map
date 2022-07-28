package nextstep.subway.acceptance.section;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.line.LineSteps.지하철노선_생성_결과;
import static nextstep.subway.acceptance.line.LineSteps.지하철노선_조회_결과;
import static nextstep.subway.acceptance.section.SectionSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.section.SectionSteps.지하철노선에_구간_삭제_요청;
import static nextstep.subway.acceptance.station.StationSteps.지하철역_생성_결과;
import static org.assertj.core.api.Assertions.assertThat;

class SectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 일호선;
    private StationResponse 잠실역;
    private StationResponse 선릉역;
    private StationResponse 강남역;

    @BeforeEach
    void init() throws JsonProcessingException {
        잠실역 = 지하철역_생성_결과("잠실역");
        선릉역 = 지하철역_생성_결과("선릉역");
        강남역 = 지하철역_생성_결과("강남역");
        일호선 = 지하철노선_생성_결과("1호선", "Green", 10, 잠실역.getId(), 선릉역.getId());
    }

    /**
     * Given 지하철 노선을 생성하고 구간을 등록한다.
     * When 이미 등록된 구간의 하행 역을 상행으로 하는 새로운 구간을 등록하면
     * Then 지하철 노선의 역 목록에 새로운 구간의 하행역이 추가된다.
     */
    @DisplayName("하행 종점역에 구간을 추가한다.")
    @Test
    void addSectionAtDownStation() throws JsonProcessingException {
        지하철_노선에_지하철_구간_생성_요청(일호선.getId(), 선릉역.getId(), 강남역.getId(), 5);

        LineResponse 일호선_조회_결과 = 지하철노선_조회_결과("/lines/" + 일호선.getId());

        assertThat(일호선_조회_결과.getStations()).containsExactly(잠실역, 선릉역, 강남역);
    }

    /**
     * Given 지하철 노선을 생성하고 구간을 등록한다.
     * When 이미 등록된 구간의 하행 역이 아닌 다른 역을 상행으로 하는 새로운 구간을 등록하면
     * Then 예외가 발생한다.
     */
    @DisplayName("노선의 하행 종점역이 아닌 역을 상행역으로 하는 구간을 등록할 수 없다.")
    @Test
    void addSectionNotEqualsDownStation() throws JsonProcessingException {
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_생성_요청(일호선.getId(), 잠실역.getId(), 강남역.getId(), 5);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선을 생성하고 구간을 등록한다.
     * When 새로운 구간의 하행역이 이미 노선에 등록되어 있다면
     * Then 예외가 발생한다.
     */
    @DisplayName("새로군 구간의 하행역이 이미 노선에 등록되어 있다면 그 구간을 추가할 수 없다.")
    @Test
    void addSectionAlreadyExistDownStation() throws JsonProcessingException {
        지하철_노선에_지하철_구간_생성_요청(일호선.getId(), 선릉역.getId(), 강남역.getId(), 5);
        ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_생성_요청(일호선.getId(), 강남역.getId(), 잠실역.getId(), 5);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선을 생성하고 두 개 이상의 구간을 등록한다.
     * When 마지막 구간의 삭제를 요청하면
     * Then 지하철 노선의 역 목록에서 하행역이 삭제된다.
     */
    @DisplayName("구간이 두 개 이상일 때 마지막 구간을 삭제한다.")
    @Test
    void removeSectionAtDownStation() throws JsonProcessingException {
        지하철_노선에_지하철_구간_생성_요청(일호선.getId(), 선릉역.getId(), 강남역.getId(), 5);

        지하철노선에_구간_삭제_요청(일호선.getId(), 강남역.getId());

        LineResponse 일호선_조회_결과 = 지하철노선_조회_결과("/lines/" + 일호선.getId());
        assertThat(일호선_조회_결과.getStations()).containsExactly(잠실역, 선릉역);
    }

    /**
     * Given 지하철 노선을 생성하고 두 개 이상의 구간을 등록한다.
     * When 마지막 구간이 아닌 구간의 삭제를 요청하면
     * Then 예외가 발생한다.
     */
    @DisplayName("마지막 구간이 아닌 구간은 삭제할 수 없다.")
    @Test
    void removeSectionAtNotDownStation() throws JsonProcessingException {
        지하철_노선에_지하철_구간_생성_요청(일호선.getId(), 선릉역.getId(), 강남역.getId(), 5);
        ExtractableResponse<Response> response = 지하철노선에_구간_삭제_요청(일호선.getId(), 선릉역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선을 생성하고 한 개의 구간을 등록한다.
     * When 구간 삭제를 요청하면
     * Then 예외가 발생한다.
     */
    @DisplayName("구간이 한 개뿐이라면 삭제할 수 없다.")
    @Test
    void removeSectionAtOneSection() {
        ExtractableResponse<Response> response = 지하철노선에_구간_삭제_요청(일호선.getId(), 선릉역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
