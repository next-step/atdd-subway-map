package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.exception.NotLastSectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.utils.LineStepUtil.*;
import static nextstep.subway.utils.SectionStepUtil.구간등록;
import static nextstep.subway.utils.SectionStepUtil.구간삭제요청;
import static nextstep.subway.utils.StationStepUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionAcceptanceTest extends AcceptanceTest {

    public Long 상행종점;
    public Long 하행종점;

    /**
     * Given 노선 생성을 요청한다.
     */
    @BeforeEach
    void setup() {
        상행종점 = 지하철역생성(기존지하철).jsonPath().getLong("id");
        하행종점 = 지하철역생성(새로운지하철).jsonPath().getLong("id");
        노선생성(기존노선, 기존색상, 상행종점, 하행종점, 종점간거리);
    }

    /**
     * Given 상행이 될 지하철 역 생성
     * Given 노선 등록을 요청한다
     * When 새로운 구간 등록을 요청한다
     * Then  구간 등록이 완료된다.
     */
    @DisplayName("새로운 구간 등록")
    @Test
    void 새로운_구간_등록_테스트() {
        //given
        ExtractableResponse<Response> 아무개 = 지하철역생성("아무개");
        Long 지하철역_ID = 아무개.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = 구간등록(하행종점, 지하철역_ID, 종점간거리);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    /**
     * When  새로운 상행이 기존의 하행과 일치하지 않는 구간 등록을 요청한다
     * Then  구간 등록이 실패한다
     */
    @DisplayName("새로운 구간 등록")
    @Test
    void 잘못된_상행_하행_구간_등록_테스트() {
        //when
        ExtractableResponse<Response> response = 구간등록(상행종점, 하행종점, 종점간거리);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     *  When  등록되지않은 하행역 구간을 요청한다.
     *  Then  구간 등록이 실패한다
     */
    @DisplayName("새로운 구간 등록")
    @Test
    void 등록안된_하행구간_등록_테스트() {
        //when
        Long 없는지하철역 = Long.MAX_VALUE;
        ExtractableResponse<Response> response = 구간등록(하행종점, 없는지하철역, 종점간거리);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 구간 등록을 요청한다
     * When  하행 종점인 구간 삭제를 요청한다.
     * Then 삭제 된다.
     */

    @DisplayName("하행 종점인 구간 삭제를 요청한다")
    @Test
    void 하행종점_구간_삭제() {
        //given
        ExtractableResponse<Response> 아무개 = 지하철역생성("아무개");
        Long 지하철역_ID = 아무개.jsonPath().getLong("id");
        ExtractableResponse<Response> 구간등록 = 구간등록(하행종점, 지하철역_ID, 종점간거리);

        //when
        Long 하행_지하철역_ID = 구간등록.jsonPath().getLong("downStation.id");
        ExtractableResponse<Response> response = 구간삭제요청(하행_지하철역_ID);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }


    /**
     * Given 구간 등록을 요청한다
     * When  하행 종점이 아닌 구간 삭제를 요청한다.
     * Then 삭제 되지 않는다.
     */

    @DisplayName("하행 종점 구간만 삭제가 가능하다")
    @Test
    void 하행종점_구간만_삭제가능() {
        //given
        ExtractableResponse<Response> 아무개 = 지하철역생성("아무개");
        Long 지하철역_ID = 아무개.jsonPath().getLong("id");
        구간등록(하행종점, 지하철역_ID, 종점간거리);

        //when
        ExtractableResponse<Response> response = 구간삭제요청(하행종점);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(NotLastSectionException.MESSAGE);

    }

    /**
     * Scenario 마지막 구간은 삭제가 불가능하다
     * When  하나 남은 구간을 삭제 요청한다.
     * Then 삭제 요청이 실패한다.
     */

    @DisplayName("마지막 구간만 삭제가 가능하다")
    @Test
    void 마지막_구간만_삭제가능() {
        //when
        ExtractableResponse<Response> response = 구간삭제요청(하행종점);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("마지막 구간 삭제 불가");

    }

}
