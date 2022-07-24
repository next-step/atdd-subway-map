package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.enums.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.LineTestFixtures.노선_생성;
import static nextstep.subway.acceptance.LineTestFixtures.노선_조회;
import static nextstep.subway.acceptance.SectionTestFixtures.*;
import static nextstep.subway.acceptance.StationTestFixtures.역_생성;
import static org.assertj.core.api.Assertions.*;

/**
 * 지하철 노선에 구간을 등록하는 기능을 구현
 * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
 * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
 * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러처리한다.
 */
@DisplayName("지하철 구간 관리")
public class SectionAcceptanceTest extends AbstractAcceptanceTest{
    public String 신림선_시작_아이디;
    public String 신림선_종점_아이디;
    public String 신분당선_시작_아이디;
    public String 신분당선_종점_아이디;

    public String 신림선_라인_아이디;
    public String 신분당선_라인_아이디;
    public String 신림선_새로운_종점_아이디;
    public String 신분당선_새로운_종점_아이디;

    @BeforeEach
    void initialize() {
        신림선_시작_아이디 = 역_생성("신림역");
        신림선_종점_아이디 = 역_생성("당곡역");

        신림선_라인_아이디 = 노선_생성("신림선", "bg-blue-300", 신림선_시작_아이디, 신림선_종점_아이디, "10")
                .jsonPath()
                .getString("id");

        신림선_새로운_종점_아이디 = 역_생성("보라매역");

        신분당선_시작_아이디 = 역_생성("강남역");
        신분당선_종점_아이디 = 역_생성("뱅뱅사거리");

        신분당선_라인_아이디 = 노선_생성("신분당선", "bg-red-100", 신분당선_시작_아이디, 신분당선_종점_아이디, "4")
                .jsonPath()
                .getString("id");

        신분당선_새로운_종점_아이디 = 역_생성("판교");
    }
    /**
     * given : 기존 신림선 노선에 보라매역 구간을 생성하고
     * when : 신림선 노선을 조회하면
     * then : 신림역, 당곡역, 보라매역을 찾을 수 있다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void createStationSection() {
        구간_생성(신림선_라인_아이디, 신림선_종점_아이디, 신림선_새로운_종점_아이디, "5");
        ExtractableResponse<Response> 노선_조회_결과 = 노선_조회(신림선_라인_아이디);

        Assertions.assertAll(() -> {
            assertThat(노선_조회_결과.jsonPath().getList("stations.name").size()).isEqualTo(3);
            assertThat(노선_조회_결과.jsonPath().getList("stations.name")).contains("신림역", "당곡역", "보라매역");
        });
    }

    // given : 신림선의 종점 아이디가 아닌 노선을 제공하고
    // when : 새로운 구간 생성 결과를 확인하면
    // then : 새로운 구간의 상행역은 해당 노선에 등록된 하행 종점역이어야 한다. 의 에러 메세지를 받을 수 있으며, 서버에러를 확인할 수 있다.
    @DisplayName("새로운 구간 등록 시 상행역이 해당 노선에 등록된 하행 종점역이 아닐 때")
    @Test
    void whenCreateInvalidUpStationFailTest() {
        ExtractableResponse<Response> 구간_생성_결과 = 구간_생성(신림선_라인_아이디, 신분당선_종점_아이디, 신림선_새로운_종점_아이디, "3");
        Assertions.assertAll(() -> {
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(구간_생성_결과.jsonPath().getString("message")).isEqualTo(ErrorCode.SAME_STATION.getMessage());
        });
    }

    // when : 기존에 등록되어있는 당곡역을 다시 새로운 구간으로 추가하면
    // then : 이미 등록되어있는 에러 응답 메세지를 받을 수 있다.
    @DisplayName("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void whenAlreadyRegisterStationAgainRegisterFailTest() {
        String 다시_신림역 = 역_생성("신림역");

        ExtractableResponse<Response> 구간_생성_결과 = 구간_생성(신림선_라인_아이디, 신림선_종점_아이디, 다시_신림역, "3");

        Assertions.assertAll(() -> {
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(구간_생성_결과.jsonPath().getString("message")).isEqualTo(ErrorCode.ALREADY_REGISTER_STATION.getMessage());
        });
    }

    /**
     * given : 신림선, 신분당선 노선을 등록하고 신림역, 당곡역, 강남역, 뱅뱅사거리, 판교, 서울숲 역을 차례로 등록하고
     * when : 신분당선 노선의 마지막 구간인 서울 숲 구간을 삭제하고 구간 목록을 조회하면
     * then : 서울숲 구간을 제외하고 신림역, 당곡역, 강남역, 뱅뱅사거리, 판교를 응답 받을 수 있다.
     */
    @DisplayName("구간 제거 기능")
    @Test
    void deleteSection() {
        구간_생성(신분당선_라인_아이디, 신분당선_종점_아이디, 신분당선_새로운_종점_아이디, "8");

        String 신분당선_또_새로운_종점_아이디 = 역_생성("서울 숲");
        String lastStationId = 구간_생성(신분당선_라인_아이디, 신분당선_새로운_종점_아이디, 신분당선_또_새로운_종점_아이디, "4").jsonPath().getString("stations[3].id");

        ExtractableResponse<Response> 구간_삭제_결과 = 구간_삭제(신분당선_라인_아이디, lastStationId);
        assertThat(구간_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> 구간_조회_결과 = 구간_조회();

        Assertions.assertAll(() -> {
            assertThat(구간_조회_결과.jsonPath().getList(".").size()).isEqualTo(3);
            assertThat(구간_조회_결과.jsonPath().getList("upStation.name")).containsExactly("신림역","강남역", "뱅뱅사거리");
            assertThat(구간_조회_결과.jsonPath().getList("downStation.name")).containsExactly("당곡역", "뱅뱅사거리", "판교");
            assertThat(구간_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        });

    }

    /**
     * given : 신분당선 노선과 강남역, 뱅뱅사거리역, 서울숲을 차례로 등록하고
     * when : 신분당선의 마지막 역인 서울 숲을 삭제를 하고 구간 목록을 조회하면
     * then : 신분당선의 서울 숲역 구간을 제외하고 강남역, 뱅뱅사거리를 응답 받을 수 있다.
     */
    @DisplayName("특정 구간 조회 기능")
    @Test
    void getSomeThingSection() {
        구간_생성(신분당선_라인_아이디, 신분당선_종점_아이디, 신분당선_새로운_종점_아이디, "8");

        String 신분당선_또_새로운_종점_아이디 = 역_생성("서울 숲");
        String lastStationId = 구간_생성(신분당선_라인_아이디, 신분당선_새로운_종점_아이디, 신분당선_또_새로운_종점_아이디, "4").jsonPath().getString("stations[3].id");

        ExtractableResponse<Response> 구간_삭제_결과 = 구간_삭제(신분당선_라인_아이디, lastStationId);
        assertThat(구간_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> 특정_구간_조회 = 특정_구간_조회(신분당선_라인_아이디);


        Assertions.assertAll(() -> {
            assertThat(특정_구간_조회.jsonPath().getList(".").size()).isEqualTo(2);
            assertThat(특정_구간_조회.jsonPath().getList("upStation.name")).containsExactly("강남역", "뱅뱅사거리");
            assertThat(특정_구간_조회.jsonPath().getString("lineId")).contains(신분당선_라인_아이디);
        });
    }
}
