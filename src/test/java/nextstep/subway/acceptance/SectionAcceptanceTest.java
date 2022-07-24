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
     * 지하철 노선에 구간을 등록하는 기능을 구현
     * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러처리한다.
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

    // given : 잘못된 종점 아이디를 제공
    // when : 새로운 구간의 상행역은 해당 노선에 등록된 하행 종점역이어야 한다.
    // then : IllegalArgumentException 발생
    @DisplayName("새로운 구간 등록 시 상행역이 해당 노선에 등록된 하행 종점역이 아닐 때")
    @Test
    void whenCreateInvalidUpStationFailTest() {
        ExtractableResponse<Response> 구간_생성_결과 = 구간_생성(신림선_라인_아이디, 신분당선_종점_아이디, 신림선_새로운_종점_아이디, "3");
        Assertions.assertAll(() -> {
            assertThat(구간_생성_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(구간_생성_결과.jsonPath().getString("message")).isEqualTo(ErrorCode.SAME_STATION.getMessage());
        });
    }

    // when : 기존에 등록되어있는 당곡역을 새로운 구간으로 다시 추가하면
    // then : ALREADY_REGISTER_STATION Exception 발생
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
     * 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉 마지막 구간만 제거할 수 있다.
     * 지하철 노선에상행 종점역과 하행 종점역만 있는경우 (구간이 1개인 경우) 역을 삭제 할 수 없다.
     * 새로운 구간 제거시 위 조건에 부합하지 않으면 에러 처리한다.
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
