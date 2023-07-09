package subway.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.util.AbstractAcceptanceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.ui.StationSteps.*;
import static subway.util.AcceptanceTestUtil.get;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AbstractAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        final String 강남역 = "강남역";
        ExtractableResponse<Response> 강남역_반환값 = 지하철역_생성_요청(강남역);

        // then
        assertThat(강남역_반환값.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(get("/stations", "name", String.class)).containsExactly("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void 지하철역_조회() {
        //given
        final String 마들역 = "마들역";
        final String 노원역 = "노원역";

        지하철역_생성_요청_Response_반환(마들역);
        지하철역_생성_요청_Response_반환(노원역);

        //when
        List<String> names = 지하철역_목록_조회_요청();

        //then
        assertThat(names).containsOnly(마들역, 노원역);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 제거한다.")
    @Test
    void 지하철역_제거() {
        //given
        final String 마들역 = "마들역";
        StationResponse 마들역_반환값 = 지하철역_생성_요청_Response_반환(마들역);

        //when
        지하철역_삭제_요청(마들역_반환값.getId());

        //then
        assertThat(지하철역_목록_조회_요청()).doesNotContain(마들역);
    }

}