package acceptance;

import static fixture.given.StationModifyRequestFixture.강남역;
import static fixture.given.StationModifyRequestFixture.새로운지하철역이름;
import static fixture.given.StationModifyRequestFixture.지하철역이름;
import static fixture.then.ApiStatusFixture.API_삭제_응답코드_검사;
import static fixture.then.ApiStatusFixture.API_생성_응답코드_검사;
import static fixture.then.ApiStatusFixture.API_요청성공_응답코드_검사;
import static fixture.then.StationThenFixture.지하철역_목록_리스트_크기_검사;
import static fixture.then.StationThenFixture.지하철역_목록_조회_두번째_지하철역_이름_검사;
import static fixture.then.StationThenFixture.지하철역_목록_조회_첫번째_지하철역_이름_검사;
import static fixture.then.StationThenFixture.지하철역_목록_조회시_생성한역을_포함하는지_검사;
import static fixture.then.StationThenFixture.지하철역_목록_조회시_아무런값도_조회되지않음_검사;
import static fixture.when.StationApiFixture.지하철역_리스트_조회;
import static fixture.when.StationApiFixture.지하철역_삭제;
import static fixture.when.StationApiFixture.지하철역_생성_요청;

import config.AcceptanceTestConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTestConfig {

    /**
     * When 지하철역을 생성하면 <br>
     * Then 지하철역이 생성된다 <br>
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> 응답결과 = 지하철역_생성_요청(강남역);

        // then
        API_생성_응답코드_검사(응답결과);
        지하철역_목록_조회시_생성한역을_포함하는지_검사(강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고 <br>
     * When 지하철역 목록을 조회하면 <br>
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void showStations() {

        //given
        지하철역_생성_요청(지하철역이름);
        지하철역_생성_요청(새로운지하철역이름);

        //when
        ExtractableResponse<Response> 응답결과 = 지하철역_리스트_조회();

        //then
        Assertions.assertAll(
            () -> API_요청성공_응답코드_검사(응답결과),
            () -> 지하철역_목록_리스트_크기_검사(응답결과, 2),
            () -> 지하철역_목록_조회_첫번째_지하철역_이름_검사(응답결과, 지하철역이름),
            () -> 지하철역_목록_조회_두번째_지하철역_이름_검사(응답결과, 새로운지하철역이름)
        );
    }

    /**
     * Given 지하철역을 생성하고 <br>
     * When 그 지하철역을 삭제하면 <br>
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철역 삭제")
    void removeStation() {

        //given
        long 신규생성_지하철역_id = 지하철역_생성_요청(새로운지하철역이름)
                .jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> 응답결과 = 지하철역_삭제(신규생성_지하철역_id);

        //then
        API_삭제_응답코드_검사(응답결과);
        지하철역_목록_조회시_아무런값도_조회되지않음_검사();

    }
}