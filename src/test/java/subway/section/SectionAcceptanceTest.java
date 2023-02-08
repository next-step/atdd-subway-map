package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.AcceptanceTest;
import subway.global.error.ErrorCode;
import subway.line.LineAcceptanceFactory;
import subway.presentation.line.dto.response.LineResponse;
import subway.station.StationAcceptanceFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineNameConstraints.Line2;
import static subway.station.StationNameConstraints.*;
import static subway.utils.JsonPathUtil.getLineResponse;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        StationAcceptanceFactory.createStation(DANG_SAN);
        StationAcceptanceFactory.createStation(HAP_JEONG);
        StationAcceptanceFactory.createStation(HONG_DAE);
        StationAcceptanceFactory.createStation(YEOM_CHANG);

        LineAcceptanceFactory.createLine(
                Line2,
                "green",
                1,
                2,
                7
        );
    }

    /**
     * When 한 개의 구간을 생성하면
     * Then 새로운 구간의 상행역은 해당 노선에 등록되어 있는 하행 종점역이다.
     */
    @DisplayName("지하철 노선 구간을 생성한다.")
    @Test
    void createSection() {
        // given && when
        ExtractableResponse<Response> response = SectionAcceptanceFactory.createSection(
                "4",
                "2",
                10
        );
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        // then
        LineResponse lineResponse = getLineResponse(LineAcceptanceFactory.getLine(1L));
        assertThat(lineResponse.getStations()).extracting("name")
                .containsExactly(DANG_SAN, HAP_JEONG, YEOM_CHANG);
    }

    /**
     * Given 새로운 구간을 만들고
     * When 또 하나의 새로운 구간을 만드는데 해당 구간의 하행역이 이미 노선에 등록된 경우라면
     * Then 예외가 발생한다.
     */
    @DisplayName("새로운 구간의 하행역이 이미 해당 노선에 등록되어 있는 경우 예외가 발생한다.")
    @Test
    void createSection_fail1() {
        // given
        SectionAcceptanceFactory.createSection(
                "3",
                "2",
                10
        );
        // when
        ExtractableResponse<Response> response = SectionAcceptanceFactory.createSection(
                "3",
                "2",
                10
        );
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        // then
        assertThat(response.body()).isEqualTo(ErrorCode.ALREADY_EXIST_STATION.getMessage());
    }

    /**
     * When 새로운 구간의 상행역이 노선에 등록되어 있는 하행 종점역이 아니라면
     * Then 예외가 발생한다.
     */
    @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어 있는 하행 종점역이 아닐 경우 예외가 발생한다.")
    @Test
    void createSection_fail2() {
        // when
        ExtractableResponse<Response> response = SectionAcceptanceFactory.createSection(
                "3",
                "1",
                10
        );
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        // then
        assertThat(response.body()).isEqualTo(ErrorCode.SECTION_MUST_BE_ADDED_END_OF_LINE);
    }


}
