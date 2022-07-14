package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.SubwayApiCaller.지하철구간_등록;
import static nextstep.subway.acceptance.SubwayApiCaller.지하철구간_삭제;
import static nextstep.subway.acceptance.SubwayApiCaller.지하철노선_조회;
import static nextstep.subway.acceptance.SubwayApiCaller.지하철역_노선_등록;
import static nextstep.subway.acceptance.SubwayApiCaller.지하철역_등록;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.applicaion.dto.StationSectionResponse;
import nextstep.subway.common.BaseAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 구간 관련 기능")
public class StationSectionAcceptanceTest extends BaseAcceptanceTest {

    private ExtractableResponse<Response> 신분당선;
    private Long 강남역;
    private Long 역삼역;
    private Long 선릉역;

    @BeforeEach
    void init() {
        강남역 = 지하철역_등록("강남역").as(StationResponse.class).getId();
        역삼역 = 지하철역_등록("역삼역").as(StationResponse.class).getId();
        선릉역 = 지하철역_등록("선릉역").as(StationResponse.class).getId();
        신분당선 = 지하철역_노선_등록("신분당선", "bg-red-600", 강남역, 역삼역, 10);
    }

    /**
     * When 지하철 구간을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 구간을 찾을 수 있다.
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void createStationSection() {
        //when
        String 신분당선_저장_위치 = 신분당선.header("Location");
        지하철구간_등록(신분당선_저장_위치,역삼역,선릉역,7);

        //then
        StationLineResponse stationLineResponses = 지하철노선_조회(신분당선_저장_위치);

        assertThat(stationLineResponses.getName()).isEqualTo("신분당선");
        assertThat(stationLineResponses.getStations()).containsExactly(
                new StationResponse(강남역,"강남역"),
                new StationResponse(역삼역,"역삼역"),
                new StationResponse(선릉역,"선릉역"));
    }

    /**
     * Given 새로운 지하철 역을 생성하고
     * When 기존 하행 종점역이 아닌 역을 새로운 구간의 상행 종점역으로 지하철 구간을 생성하면
     * Then 400에러가 발생한다.
     */
    @DisplayName("새로운 구간 상행선이 기존 하행 종점역이 아닐때 예외발생")
    @Test
    void createBadRequestCase1() {
        //given
        Long 삼성역 = 지하철역_등록("삼성역").as(StationResponse.class).getId();

        //when
        String lineUrl = 신분당선.header("Location");

        ExtractableResponse<Response> 지하철구간_등록 = 지하철구간_등록(lineUrl, 삼성역, 선릉역, 7);

        //then
        assertThat(지하철구간_등록.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 노선에 등록되어 있는 기존 역을 새로운 구간의 하행역으로 등록될 경우
     * Then 400에러가 발생한다.
     */
    @DisplayName("새로운 구간의 하행역이 기존의 등록되어 있는 역일 경우 예외발생")
    @Test
    void createBadRequestCase2() {
        //when
        String lineUrl = 신분당선.header("Location");

        ExtractableResponse<Response> 지하철구간_등록 = 지하철구간_등록(lineUrl, 역삼역, 강남역, 7);

        //then
        assertThat(지하철구간_등록.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 존재하지 않는 지하철역으로 지하철 노선을 생성하면
     * Then 400에러가 발생한다.
     */
    @DisplayName("존재하지 않는 지하철역으로 지하철 노선을 생성시 예외발생")
    @Test
    void createBadRequestCase3() {
        //given
        Long 대림역 = 4L;

        //when
        String lineUrl = 신분당선.header("Location");

        ExtractableResponse<Response> 지하철구간_등록 = 지하철구간_등록(lineUrl, 역삼역, 대림역, 7);

        //then
        assertThat(지하철구간_등록.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 구간을 생성하고
     * When 생성한 지하철 구간을 삭제하면
     * Then 해당 지하철 노선 조회 시 해당 지하철 구간 정보는 삭제된다
     */
    @DisplayName("지하철 구간 제거")
    @Test
    void deleteStationSection() {
        //given
        String lineUrl = 신분당선.header("Location");
        Long 지하철구간 = 지하철구간_등록(lineUrl, 역삼역, 선릉역, 7)
                .as(StationSectionResponse.class).getDownStationId();

        //when
        지하철구간_삭제(lineUrl, 지하철구간);

        //then
        StationLineResponse stationLineResponses = 지하철노선_조회(lineUrl);

        assertThat(stationLineResponses.getName()).isEqualTo("신분당선");
        assertThat(stationLineResponses.getStations()).containsExactly(
                new StationResponse(강남역,"강남역"),
                new StationResponse(역삼역,"역삼역"));
    }


    /**
     * Given 지하철 구간을 생성하고
     * When 마지막 구간이 아닌 구간을 삭제시
     * Then 400에러가 발생한다.
     */
    @DisplayName("마지막 구간이 아닌 구간을 제거시 예외발생")
    @Test
    void deleteBadRequestCase() {
        //given
        String lineUrl = 신분당선.header("Location");
        Long 지하철구간 = 지하철구간_등록(lineUrl, 역삼역, 선릉역, 7)
                .as(StationSectionResponse.class).getDownStationId();

        //when
        지하철구간_삭제(lineUrl, 지하철구간);

        //then
        StationLineResponse stationLineResponses = 지하철노선_조회(lineUrl);

        assertThat(stationLineResponses.getName()).isEqualTo("신분당선");
        assertThat(stationLineResponses.getStations()).containsExactly(
                new StationResponse(강남역,"강남역"),
                new StationResponse(역삼역,"역삼역"));
    }

    /**
     * Given 상행 종점역과 하행 종점역만 있는 지하철 노선을 생성하고
     * When  구간을 삭제하면
     * Then 400에러가 발생한다.
     */
    @DisplayName("상행 종점역과 하행 종점역만 있는 지하철 노선의 구간 삭제시 예외 발생")
    @Test
    void deleteBadRequestCase2() {
        //given
        String lineUrl = 신분당선.header("Location");
        Long 지하철구간 = 지하철구간_등록(lineUrl, 역삼역, 선릉역, 7)
                .as(StationSectionResponse.class).getDownStationId();

        //when
        지하철구간_삭제(lineUrl, 지하철구간);

        //then
        StationLineResponse stationLineResponses = 지하철노선_조회(lineUrl);

        assertThat(stationLineResponses.getName()).isEqualTo("신분당선");
        assertThat(stationLineResponses.getStations()).containsExactly(
                new StationResponse(강남역,"강남역"),
                new StationResponse(역삼역,"역삼역"));
    }
}
