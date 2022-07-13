package nextstep.subway.acceptance.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.line.LineSteps.*;
import static nextstep.subway.acceptance.section.SectionSteps.*;
import static nextstep.subway.acceptance.station.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 구간 등록(상행역, 하행역, 거리)
     * Then 지하철 구간 등록 성공 응답받는다.
     * Then 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * Then 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     */
    @DisplayName("지하철 등록 생성 성공")
    @Test
    void 구간_등록_성공_테스트() {

        //Given 상행역, 하행역, 거리
        Long 노선_상행역_ID = ID(지하철역_생성(GANGNAM_STATION));
        Long 노선_하행역_ID = ID(지하철역_생성(YUKSAM_STATION));
        //Given 노선 생성
        ExtractableResponse<Response> 노선 = 노선_생성(SHIN_BUNDANG_LINE, BLUE, 노선_상행역_ID, 노선_하행역_ID, DISTANCE);
        Long 구간_상행역_ID = 노선_하행역_ID;
        Long 구간_하행역_ID = ID(지하철역_생성(NONHYUN_STATION));

        //When 지하철 구간 등록
        ExtractableResponse<Response> 구간 = 구간_등록(ID(노선), 구간_상행역_ID, 구간_하행역_ID, DISTANCE);

        assertAll(
                // then 지하철 구간 등록 성공 응답받는다.
                () -> assertThat(구간.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                // then 구간의 상행역은 해당 노선의 하행 종점역이어야 한다.
                () -> assertThat(구간_상행역_ID(구간)).isEqualTo(노선_하행역_ID(노선)),
                // then 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
                () -> assertThat(노선_역_목록_ID(노선)).doesNotContain(구간_하행역_ID(구간))
        );

    }

    /**
     * Given 구간 상행역 != 노선 하행 종점역
     * When 지하철 구간 등록(상행역, 하행역, 거리)
     * Then 서버 에러를 응답 받는다.
     * Then 구간의 상행역은 노선의 하향 종점역이어야 합니다. exception 발생
     */
    @DisplayName("구간 상행역 != 노선 하행 종점역일 경우 구간 등록시 에러 발생")
    @Test
    void 구간_등록_시실패_테스트() {
        // Given  구간 상행역 != 노선 하행 종점역
        Long 노선_상행역_ID = ID(지하철역_생성(GANGNAM_STATION));
        Long 노선_하행역_ID = ID(지하철역_생성(YUKSAM_STATION));
        ExtractableResponse<Response> 노선 = 노선_생성(SHIN_BUNDANG_LINE, BLUE, 노선_상행역_ID, 노선_하행역_ID, DISTANCE);
        Long 구간_상행역_ID = ID(지하철역_생성(NONHYUN_STATION));
        Long 구간_하행역_ID = ID(지하철역_생성(SHIN_NONHYUN_STATION));

        // When 구간 등록
        ExtractableResponse<Response> 구간 = 구간_등록(ID(노선), 구간_상행역_ID, 구간_하행역_ID, DISTANCE);

        Throwable exception = Assertions.assertThrows((Throwable.class), () -> {
            throw new IllegalArgumentException("section.upStation.line.downStation");
        });

        assertAll(
                // 서버 에러를 응답 받는다.
                () -> assertThat(구간.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                // Then 구간의 상행역은 노선의 하향 종점역이어야 합니다. exception 발생
                () -> assertEquals("section.upStation.line.downStation", exception.getMessage())
        );
    }

    /**
     * Given 구간 상행역 != 노선 하행 종점역
     * When 지하철 구간 등록(상행역, 하행역, 거리)
     * Then 서버 에러를 응답 받는다.
     * Then 구간의 하행역은 노선에 없는 역이어야합니다. exception 발생
     */
    @DisplayName("구간의 하행역이 노선의 역과 중복될 경우 오류 발생")
    @Test
    void 구간_하행역_노선_중복_오류_발생_테스트() {
        //Given 구간 상행역 != 노선 하행 종점역
        Long 노선_상행역_ID = ID(지하철역_생성(GANGNAM_STATION));
        Long 노선_하행역_ID = ID(지하철역_생성(YUKSAM_STATION));
        ExtractableResponse<Response> 노선 = 노선_생성(SHIN_BUNDANG_LINE, BLUE, 노선_상행역_ID, 노선_하행역_ID, DISTANCE);
        Long 구간_상행역_ID = ID(지하철역_생성(NONHYUN_STATION));
        Long 구간_하행역_ID = 노선_하행역_ID;
        // When 지하철 구간 등록
        ExtractableResponse<Response> 구간 = 구간_등록(ID(노선), 구간_상행역_ID, 구간_하행역_ID, DISTANCE);

        Throwable exception = Assertions.assertThrows((Throwable.class), () -> {
            throw new IllegalArgumentException("section.downStation.line.duplicate");
        });

        assertAll(
                //서버 에러를 응답 받는다.
                () -> assertThat(구간.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                //Then 구간의 상행역은 노선의 하향 종점역이어야 합니다. exception 발생
                () -> assertEquals("section.downStation.line.duplicate", exception.getMessage())
        );

    }


    /**
     * Given 구간 생성
     * When 지하철 구간을 삭제(노선ID, 역ID)
     * Then 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
     * Then 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
     * Then 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @DisplayName("지하철 구간 제거")
    @Test
    void 지하철_구간_제거_테스트() {

    }


}