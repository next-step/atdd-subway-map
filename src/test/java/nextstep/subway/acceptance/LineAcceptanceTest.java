package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.exception.DuplicationException;
import nextstep.subway.applicaion.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static nextstep.subway.utils.HttpRequestTestUtil.딜리트_요청;
import static nextstep.subway.utils.LineStepUtil.기본주소;
import static nextstep.subway.utils.LineStepUtil.*;
import static nextstep.subway.utils.StationStepUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다.
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void 노선생성_테스트() {
        //given
        테스트준비_지하철역들생성();

        //when
        ExtractableResponse<Response> response = 기존노선생성();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * When 없는 역을 종점으로 선언하고 노선 생성 요청을 한다
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("없는 역을 노선에 등록한다")
    @Test
    void notFoundSection() {

        //when
        ExtractableResponse<Response> response = 노선생성("신규노선", "신규색상", Long.MAX_VALUE, Long.MIN_VALUE, Integer.MAX_VALUE);

        //then
        assertThat(response.jsonPath().getString("message")).isEqualTo(NotFoundException.MESSAGE);
    }


    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다.
     * Given 지하철 노선을 생성한다.
     * Given 새로운 지하철 노선을 생성한다.
     * When 지하철 노선 조회를 요청한다.
     * Then 지하철 노선을 반환한다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void 노선목록조회_테스트() {
        //given
        테스트준비_지하철역들생성();
        기존노선생성();
        새로운노선생성();

        //when
        ExtractableResponse<Response> response = 노선조회(기본주소);

        //then
        assertThat(response.jsonPath().getList("name")).contains(새로운노선, 기존노선);
        assertThat(response.jsonPath().getList("color")).contains(새로운색상, 새로운색상);
    }

    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다.
     * Given 지하철 노선을 생성하고
     * When 생성된 지하철 노선을 요청한다.
     * Then 생성된 지하철 노선을 반환한다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void 노선조회_테스트() {
        //given
        테스트준비_지하철역들생성();
        ExtractableResponse<Response> createResponse = 기존노선생성();

        //when
        ExtractableResponse<Response> response = 노선조회(createResponse.header(HttpHeaders.LOCATION));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(기존노선);
        assertThat(response.jsonPath().getList("stations.name")).contains(기존지하철, 새로운지하철);

    }

    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다.
     * Given 지하철 노선을 생성하고
     * When 지하철 노선 수정을 요청한다,
     * Then 지하철 노선이 수정이 완료된다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void 노선업데이트_테스트() {
        //given
        테스트준비_지하철역들생성();
        ExtractableResponse<Response> createResponse = 기존노선생성();

        //when
        ExtractableResponse<Response> updateResponse = 노선수정(createResponse);

        //then
        ExtractableResponse<Response> response = 노선조회(createResponse.header(HttpHeaders.LOCATION));

        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(수정노선);
    }


    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다.
     * Given 지하철 노션을 생성을 요청한다.
     * When 생성된 지하철 노션을 삭제를 요청한다.
     * Then 지하철 노션이 삭제된다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void 노선삭제_테스트() {
        //given
        테스트준비_지하철역들생성();
        ExtractableResponse<Response> createResponse = 기존노선생성();

        //when
        ExtractableResponse<Response> response = 딜리트_요청(createResponse.header(HttpHeaders.LOCATION));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 역 (상행, 하행)생성을 요청한다.
     * Given 지하철 노선을 생성한다.
     * When 중복된 이름의 지하철 노선 생성을 요청한다.
     * Then 지하철 노선 생성이 실패한다.
     */

    @DisplayName("중복된 노선 생성은 실패한다")
    @Test
    void duplicationLine() {
        //given
        테스트준비_지하철역들생성();
        기존노선생성();

        //when
        ExtractableResponse<Response> response = 기존노선생성();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(DuplicationException.MESSAGE);
    }




}
