package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.StationAcceptanceTest.기존지하철역생성;
import static nextstep.subway.acceptance.StationAcceptanceTest.새로운지하철역생성;
import static nextstep.subway.utils.HttpRequestTestUtil.포스트_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionAcceptanceTest extends AcceptanceTest{

    private static final String 기본구간주소 = "/lines/1/sections";
    private static final String 기존노선 = "기존노선";
    private static final String 기존색상 = "기존색상";
    private static final String 기본주소 = "/lines";
    private static final int 종점간거리 = 2;
    private Long 상행종점 = 1L;
    private Long 하행종점 = 2L;

    /**
     *  Given 지하철 역 (상행, 하행)생성을 요청한다.
     *  Given 노선 등록을 요청한다
     *  When 새로운 구간 등록을 요청한다
     *  Then  구간 등록이 완료된다.
     */
    @DisplayName("새로운 구간 등록")
    @Test
    void 새로운_구간_등록_테스트(){
        //given
        지하철역생성();
        기존노선생성();

        //when
        Map<String,Object> params = new HashMap<>();
        params.put("upStationId",하행종점);
        params.put("downStationId",상행종점);
        params.put("distance",10);

        ExtractableResponse<Response> response = 포스트_요청(기본구간주소, params);

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
        지하철역생성();
        기존노선생성();

        //when
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", 상행종점);
        params.put("downStationId", 하행종점);
        params.put("distance", 10);

        //삭제 동시접근시?
        ExtractableResponse<Response> response = 포스트_요청(기본구간주소, params);

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
    void 등록안된_하행구간_등록_테스트(){

    }

    private void 지하철역생성() {
        ExtractableResponse<Response> 기존지하철역생성 = 기존지하철역생성();
        ExtractableResponse<Response> 새로운지하철역생성 = 새로운지하철역생성();
        상행종점 = Long.valueOf(기존지하철역생성.jsonPath().getString("id"));
        하행종점 = Long.valueOf(새로운지하철역생성.jsonPath().getString("id"));
    }

    private ExtractableResponse<Response> 기존노선생성() {
        Map<String, Object> param = 노선파라미터생성(기존노선, 기존색상, 상행종점, 하행종점, 종점간거리);
        return 포스트_요청(기본주소, param);

    }

    private Map<String, Object> 노선파라미터생성(String 노선, String 색상, Long 상행종점, Long 하행종점, int 종점간거리) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", 노선);
        param.put("color", 색상);
        param.put("upStationId", 상행종점);
        param.put("downStationId", 하행종점);
        param.put("distance", 종점간거리);
        return param;
    }

}
