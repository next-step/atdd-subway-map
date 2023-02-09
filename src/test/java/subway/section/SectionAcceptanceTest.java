package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.AcceptanceTest;
import subway.global.error.ErrorCode;
import subway.presentation.line.dto.response.LineResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineAcceptanceFactory.createLine;
import static subway.line.LineNameConstraints.Line2;
import static subway.section.SectionAcceptanceFactory.*;
import static subway.station.StationAcceptanceFactory.createStation;
import static subway.station.StationNameConstraints.*;
import static subway.utils.JsonPathUtil.getLineResponse;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        createStation(DANG_SAN);
        createStation(HAP_JEONG);
        createStation(HONG_DAE);
        createStation(YEOM_CHANG);

        createLine(
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
    void 지하철_구간_생성() {
        // given && when
        ExtractableResponse<Response> response = createSection(
                "4",
                "2",
                10
        );
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        // then
        LineResponse lineResponse = getLineResponse(response);
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
    void 지하철_구간_생성_예외1() {
        // given && when
        ExtractableResponse<Response> response = createSection(
                "1",
                "2",
                20
        );
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        // then
        assertThat(response.body().asString()).isEqualTo(ErrorCode.ALREADY_EXIST_STATION.getMessage());
    }

    /**
     * When 새로운 구간의 상행역이 노선에 등록되어 있는 하행 종점역이 아니라면
     * Then 예외가 발생한다.
     */
    @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어 있는 하행 종점역이 아닐 경우 예외가 발생한다.")
    @Test
    void 지하철_구간_생성_예외2() {
        // when
        ExtractableResponse<Response> response = createSection(
                "3",
                "1",
                20
        );
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        // then
        assertThat(response.body().asString()).isEqualTo(ErrorCode.SECTION_MUST_BE_ADDED_END_OF_LINE.getMessage());
    }

    /**
     * Given: 새로운 지하철 구간 1개를 추가와 함께 추가한 구간의 하행역 ID가 주어졌을 때
     * When: 노선에서 주어진 하행역를 이용해 지하철역을 제거한다면
     * Then: 해당 역이 포함된 구간은 사라진다.
     */
    @DisplayName("구간을 삭제한다.")
    @Test
    void 지하철_구간_삭제() {
        // given
        createSection(
                "3",
                "2",
                20
        );
        long stationId = 3L;
        // when
        ExtractableResponse<Response> response = deleteSection(stationId);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given: 새로운 지하철 구간 1개를 추가와 지하철역 ID가 주어졌을 때
     * When: 노선에서 주어진 지하철역을 제거할려고 할 때 주어진 지하철역이 하행 종점역이 아니라면
     * Then: 예외가 발생한다.
     */
    @DisplayName("제거할 지하철 역이 하행 종점역이 아니라면 예외가 발생한다.")
    @Test
    void 지하철_구간_삭제_예외1() {
        // given
        createSection(
                "3",
                "2",
                20
        );
        long stationId = 2L;
        // when
        ExtractableResponse<Response> response = deleteSection(stationId);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        // then
        assertThat(response.body().asString()).isEqualTo(ErrorCode.SECTION_MUST_BE_DELETED_END_OF_LINE.getMessage());
    }

    /**
     * Given: 지하철역 ID가 주어졌을 때
     * When: 노선에서 주어진 지하철역을 제거할려고 할 때 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우
     * Then: 예외가 발생한다.
     */
    @DisplayName("제거할 지하철 역이 하행 종점역이 아니라면 예외가 발생한다.")
    @Test
    void 지하철_구간_삭제_예외2() {
        // given
        long stationId = 2L;
        // when
        ExtractableResponse<Response> response = deleteSection(stationId);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        // then
        assertThat(response.body().asString()).isEqualTo(ErrorCode.CANNOT_DELETE_WHEN_LINE_HAS_ONLY_ONE_SECTION.getMessage());
    }

}
