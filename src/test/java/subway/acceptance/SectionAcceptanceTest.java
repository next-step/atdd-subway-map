package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.utils.LineTestRequests.지하철_노선_조회_응답값_반환;
import static subway.utils.LineTestRequests.지하철_노선도_등록;
import static subway.utils.SectionTestRequests.지하철_구간_등록;
import static subway.utils.StationTestRequests.지하철_역_등록;
import static subway.utils.StatusCodeAssertions.응답코드_검증;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import subway.line.controller.dto.LineResponse;
import subway.station.controller.dto.StationResponse;

@DisplayName("지하철 구간 관련 기능")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SectionAcceptanceTest {

    @BeforeEach
    void initStations() {
        지하철_역_등록("첫번째역");
        지하철_역_등록("두번째역");
        지하철_역_등록("세번째역");
        지하철_역_등록("네번째역");
    }

    /**
     * Given 노선을 생성한다
     * When 구간을 생성한다
     * Then 생성한 노선의 하행선이 변경된다.
     */
    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createSection() {
        //given
        지하철_노선도_등록("7호선", "bg-1234", 1L, 2L, 5);

        //when
        ExtractableResponse<Response> response = 지하철_구간_등록(1L, 2L, 3L, 7);

        //then
        응답코드_검증(response, HttpStatus.OK);
        LineResponse line7 = 지하철_노선_조회_응답값_반환(1L);
        StationResponse downwardLastStation = line7.getStations().get(1);
        assertThat(downwardLastStation.getId()).isEqualTo(3L);
        assertThat(downwardLastStation.getName()).isEqualTo("세번째역");
    }

    /**
     * Given 노선을 생성한다
     * When 구간의 상행이 노선의 하행이 아닐 때에
     * Then 에러 상태 값을 리턴한다
     */
    @DisplayName("지하철 구간을 생성한다. 구간의 상행이 노선의 하행이 아닐 때에 에러 반환")
    @Test
    void createSectionExceptionWhenUpwardDoesNotMatchWithDownward() {
        //given
        지하철_노선도_등록("7호선", "bg-1234", 1L, 2L, 5);

        //when
        ExtractableResponse<Response> response = 지하철_구간_등록(1L, 4L, 3L, 7);

        //then
        응답코드_검증(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Given 노선을 생성한다
     * When 구간의 하행이 이미 노선에 등록되어 있다면
     * Then 에러 상태 값을 리턴한다
     */
    @DisplayName("지하철 구간을 생성한다. 구간의 하행이 이미 노선에 등록되어 있다면 에러 반환")
    @Test
    void createSectionExceptionWhenAlreadyDownwardStationRegistered() {
        //given
        지하철_노선도_등록("7호선", "bg-1234", 1L, 2L, 5);

        //when
        ExtractableResponse<Response> response = 지하철_구간_등록(1L, 4L, 1L, 7);

        //then
        응답코드_검증(response, HttpStatus.BAD_REQUEST);
    }
}
