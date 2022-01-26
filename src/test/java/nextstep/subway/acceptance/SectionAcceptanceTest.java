package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.exception.NotLastSectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.utils.LineStepUtil.*;
import static nextstep.subway.utils.SectionStepUtil.구간삭제요청;
import static nextstep.subway.utils.SectionStepUtil.새로운구간등록;
import static nextstep.subway.utils.StationStepUtil.새로운지하철역생성;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionAcceptanceTest extends AcceptanceTest{

    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다. + 상행이 될 지하철 역 생성
     * Given 노선 등록을 요청한다
     * When 새로운 구간 등록을 요청한다
     * Then  구간 등록이 완료된다.
     */
    @DisplayName("새로운 구간 등록")
    @Test
    void 새로운_구간_등록_테스트() {
        //given
        테스트준비_노선등록();
        ExtractableResponse<Response> 아무개 = 새로운지하철역생성("아무개");
        Long 지하철역_ID = 아무개.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = 새로운구간등록(하행종점, 지하철역_ID, 종점간거리);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다.
     * Given 노선 등록을 요청한다
     * When  새로운 상행이 기존의 하행과 일치하지 않는 구간 등록을 요청한다
     * Then  구간 등록이 실패한다
     */
    @DisplayName("새로운 구간 등록")
    @Test
    void 잘못된_상행_하행_구간_등록_테스트() {
        //given
        테스트준비_노선등록();

        //when

        //삭제 동시접근시?
        ExtractableResponse<Response> response = 새로운구간등록(상행종점, 하행종점, 종점간거리);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     *  Given 지하철 역 (상행, 하행)생성을 요청한다.
     *  Given 노선 등록을 요청한다
     *  When  등록되지않은 하행역 구간을 요청한다.
     *  Then  구간 등록이 실패한다
     */
    @DisplayName("새로운 구간 등록")
    @Test
    void 등록안된_하행구간_등록_테스트() {
        //given
        테스트준비_노선등록();
        //when
        Long 없는지하철역 = Long.MAX_VALUE;
        ExtractableResponse<Response> response = 새로운구간등록(하행종점, 없는지하철역, 종점간거리);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다. + 노선 등록을 요청한다
     * Given 구간 등록을 요청한다
     * When  하행 종점인 구간 삭제를 요청한다.
     * Then 삭제 된다.
     */


    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다. + 노선 등록을 요청한다
     * Given 구간 등록을 요청한다
     * When  하행 종점이 아닌 구간 삭제를 요청한다.
     * Then 삭제 되지 않는다.
     */

    @DisplayName("하행 종점 구간만 삭제가 가능하다")
    @Test
    void 하행종점_구간만_삭제가능() {
        //given
        테스트준비_노선등록();
        테스트준비_구간등록();

        //when
        ExtractableResponse<Response> response = 구간삭제요청(하행종점);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(NotLastSectionException.MESSAGE);

    }

    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다.
     * Given 노선 등록을 요청한다
     * Given 구간 등록을 요청한다
     * When  마지막 구간 삭제를 요청한다.
     * Then 삭제 요청이 실패한다.
     */

    @DisplayName("마지막 구간만 삭제가 가능하다")
    @Test
    void 마지막_구간만_삭제가능() {
        //given
        테스트준비_노선등록();

        //when
        ExtractableResponse<Response> response = 구간삭제요청(하행종점);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("마지막 구간 삭제 불가");

    }

    private ExtractableResponse<Response> 테스트준비_구간등록() {
        ExtractableResponse<Response> 아무개 = 새로운지하철역생성("아무개");
        Long 지하철역_ID = 아무개.jsonPath().getLong("id");
        return 새로운구간등록(하행종점, 지하철역_ID, 종점간거리);
    }


}
