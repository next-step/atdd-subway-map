package nextstep.subway.acceptance.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.line.LineSteps.*;
import static nextstep.subway.acceptance.section.SectionSteps.지하철_구간_등록;
import static nextstep.subway.acceptance.station.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 구간 등록(상행역, 하행역, 거리)
     * Then 지하철 구간 등록 성공 응답받는다.
     * Then 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * Then 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * Then 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @DisplayName("지하철 등록 생성")
    @Test
    void 지하철_구간_등록_테스트() {

        Long gangnamStationId = ID(지하철역_생성(GANGNAM_STATION));
        Long yuksamStationId = ID(지하철역_생성(YUKSAM_STATION));
        Long shinbundangLineId = ID(지하철_노선_생성(SHIN_BUNDANG_LINE, BLUE, gangnamStationId, yuksamStationId, DISTANCE));

        ExtractableResponse<Response> response = 지하철_구간_등록(shinbundangLineId, gangnamStationId, yuksamStationId, DISTANCE);

        assertAll(
                // then 지하철 구간 등록 성공 응답받는다.
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        );

    }

    /**
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