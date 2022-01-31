package nextstep.subway.acceptance.section;

import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.line.LineSteps.*;
import static nextstep.subway.acceptance.section.SectionSteps.구간_등록_요청;
import static nextstep.subway.acceptance.section.SectionSteps.구간_제거_요청;
import static nextstep.subway.acceptance.station.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private String lineId;
    private String upStationId;
    private String downStationId;
    private String distance;

    @BeforeEach
    public void setUp() {
        super.setUp();
        upStationId = 지하철_역_생성_요청_및_위치_반환("강남역");
        downStationId = 지하철_역_생성_요청_및_위치_반환("양재역");
        distance = "5";
        lineId = 지하철_노선_생성_요청("신분당선", "red", upStationId, downStationId, distance).header("Location").split("/")[2];
    }

    /**
     * Given 새로운 구간의 상행역이 해당 노선에 하행 종점역으로 등록되어 있을 때
     * When 상행역과 미등록된 하행역을 구간 등록 요청하면
     * Then 요청이 성공한다
     */
    @DisplayName("구간 등록 성공")
    @Test
    void should_return_201_when_request_to_create_section() {
        // given
        var newDownStationId = 지하철_역_생성_요청_및_위치_반환("양재시민의숲");
        var distance = "10";

        // when
        var response = 구간_등록_요청(lineId, downStationId, newDownStationId, distance);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 새로운 구간의 상행역이 해당 노선에 하행 종점역으로 등록 되어 있지 않을때
     * When 상행역과 미등록된 하행역을 구간 등록 요청하면
     * Then 요청이 실패한다
     */
    @DisplayName("하행 종점역으로 등록되지 않은 역을 상행으로 등록 요청시 실패")
    @Test
    void should_return_400_when_request_to_create_section_that_contain_unregistered_up_station() {
        // given
        var unregisteredUpStationId = 지하철_역_생성_요청_및_위치_반환("청계산입구");
        var newDownStationId = 지하철_역_생성_요청_및_위치_반환("양재시민의숲");
        var distance = "7";

        // when
        var response = 구간_등록_요청(lineId, unregisteredUpStationId, newDownStationId, distance);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 새로운 구간의 상행역이 해당 노선에 하행 종점역으로 등록 되어 있을 때
     * When 상행역 - 이미 노선에 등록된 하행역을 구간 등록 요청하면
     * Then 요청이 실패한다
     */
    @DisplayName("이미 노선에 등록되어 있는 역을 하행역으로 등록 요청시 실패")
    @Test
    void should_return_400_when_request_to_create_section_that_contain_already_registered_down_station() {
        // when
        var response = 구간_등록_요청(lineId, downStationId, upStationId, distance);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 등록된 구간이 2개 이상일때
     * When 하행 종점역을 제거 요청하면
     * Then 요청이 성공한다.
     */
    @DisplayName("구간 제거 성공")
    @Test
    void should_return_204_when_request_to_delete_section() {
        // given
        var newDownStationId = 지하철_역_생성_요청_및_위치_반환("양재시민의숲");
        var distance = "10";
        구간_등록_요청(lineId, downStationId, newDownStationId, distance);

        // when
        var response = 구간_제거_요청(lineId, newDownStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 등록된 구간이 1개일때
     * When 하행 종점역을 제거 요청하면
     * Then 요청이 실패한다.
     */
    @DisplayName("구간이 1개일 경우 제거 실패")
    @Test
    void should_return_400_when_section_is_only_one() {
        // when
        var response = 구간_제거_요청(lineId, downStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 등록된 구간이 2개 이상일때
     * When 등록되지 않은 역을 제거 요청하면
     * Then 요청이 실패한다.
     */
    @DisplayName("미등록된 역 제거 실패")
    @Test
    void should_return_400_when_request_to_delete_unregistered_station() {
        // given
        var newDownStationId = 지하철_역_생성_요청_및_위치_반환("양재시민의숲");
        var distance = "10";
        구간_등록_요청(lineId, downStationId, newDownStationId, distance);

        var unregisteredStationId = 지하철_역_생성_요청_및_위치_반환("청계산입구");

        // when
        var response = 구간_제거_요청(lineId, unregisteredStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
