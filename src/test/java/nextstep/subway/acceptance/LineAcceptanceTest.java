package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nextstep.subway.fixture.CommonFixture.uri;
import static nextstep.subway.fixture.LineFixture.*;
import static nextstep.subway.fixture.SectionFixture.구간;
import static nextstep.subway.fixture.StationFixture.*;
import static nextstep.subway.utils.HttpRequestResponse.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성 성공")
    @Test
    void success() {
        // given
        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);
        
        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);

        // when
        ExtractableResponse<Response> 노선생성_결과 = 지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);
        

        // then
        assertThat(노선생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(uri(노선생성_결과)).isNotBlank();
        assertThat(노선생성_결과.jsonPath().getList("stations")).isNotEmpty(); // TODO : station 리턴을 여기서 검증해야할지 생각해보기
    }

    /**
     * Given 지하철 노선 생성을 요청하고
     * When 같은 이름으로 지하철 노선 생성을 요청하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선명이 중복이면, 노선 생성 실패")
    @Test
    void duplicateNameIsNotAllowed() {
        // given
        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);

        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);
        지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);

        //when
        ExtractableResponse<Response> 중복생성_결과_response = 지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);

        // then
        assertThat(중복생성_결과_response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

     /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        /// given
        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);

        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);
        지하철_노선_생성(구분당선_이름, 구분당선_색상, 상행종점_ID, 하행종점_ID, 10);
        지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회("/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(신분당선_이름, 구분당선_이름);
    }

    /**
     * Given 지하철 노선 생성을 요청 하면
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // when
        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);

        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);
        ExtractableResponse<Response> 생성_요청_응답 = 지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);
        String 생성된_노선_uri = uri(생성_요청_응답);

        Map<String, String> 구분당선 = 노선(구분당선_이름, 구분당선_색상, 상행종점_ID, 하행종점_ID, 10);
        ExtractableResponse<Response> response = put(구분당선, 생성된_노선_uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> updatedLine = 지하철_노선_조회(생성된_노선_uri);
        String updateName = updatedLine.jsonPath().get("name");
        assertThat(updateName).isEqualTo(구분당선_이름);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {

        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);

        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);
        ExtractableResponse<Response> 생성_요청_응답 = 지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);
        String 생성된_노선_uri = uri(생성_요청_응답);

        // when
        ExtractableResponse<Response> response = delete(생성된_노선_uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철역을 2개 생성하고
     * Given 지하철 노선 생성을 요청하고
     * When 지하철 구간 생성을 요청하면
     * Then 지하철 구간 생성이 성공한다.
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void createSection() {
        // given
        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);

        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);

        ExtractableResponse<Response> 생성_요청_응답 = 지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);
        String 생성된_노선_uri = uri(생성_요청_응답);

        ExtractableResponse<Response> 새구간_하행역_생성결괴 = 역_생성(대림역_이름);

        Long 하행역_ID = stationId(새구간_하행역_생성결괴);
        int 상행역과_하행역_사이_거리 = 10;

        // when
        Map<String, String> 구간_생성요청_dto = 구간(하행종점_ID, 하행역_ID, 상행역과_하행역_사이_거리);

        ExtractableResponse<Response> 생성결과 = post(구간_생성요청_dto, 생성된_노선_uri+"/sections");

        // then
        assertThat(생성결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(uri(생성결과)).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청하고
     * Given 새 역을 생성하고
     * When 노선의 역 중, 하행 종점역 외의 역을 새 구간의 상행역으로 지정하고
     * When 구간 생성요청시
     * Then 구간 생성이 실패한다.
     */
    @DisplayName("히헹 종점역 이외의 역을 구간의 상행역으로 지정시, 구간 생성 실패")
    @Test
    void newUpStation() {
        // given
        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);

        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);

        ExtractableResponse<Response> 생성_요청_응답 = 지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);

        // given
        String 생성된_노선_uri = uri(생성_요청_응답);
        ExtractableResponse<Response> 새구간_하행역_생성결괴 = 역_생성(대림역_이름);
        Long 하행역_ID = stationId(새구간_하행역_생성결괴);
        int 상행역과_하행역_사이_거리 = 10;

        // when
        Map<String, String> 구간_생성요청_dto = 구간(상행종점_ID, 하행역_ID, 상행역과_하행역_사이_거리);

        ExtractableResponse<Response> 생성결과 = post(구간_생성요청_dto, 생성된_노선_uri+"/sections");

        // then
        assertThat(생성결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청하고
     * When 노선에 이미 등록된 역을 새 구간의 하행역으로 지정하고
     * When 구간 생성요청시
     * Then 구간 생성이 실패한다.
     */
    @DisplayName("노선에 이미 등록된 역을 새 구간의 하행역으로 지정시, 구간 생성 실패")
    @Test
    void newDownStation() {
        // given
        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);

        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);

        ExtractableResponse<Response> 생성_요청_응답 = 지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);

        String 생성된_노선_uri = uri(생성_요청_응답);
        int 상행역과_하행역_사이_거리 = 10;

        // when
        Map<String, String> 구간_생성요청_dto = 구간(하행종점_ID, 하행종점_ID, 상행역과_하행역_사이_거리);

        ExtractableResponse<Response> 생성결과 = post(구간_생성요청_dto, 생성된_노선_uri+"/sections");

        // then
        assertThat(생성결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청하고
     * Given 지하철 구간을 생성을 새로 요청하고
     * When 구간 삭제 요청시
     * Then 마지막 구간 삭제 성공한다
     */
    @DisplayName("지하철 구간 삭제")
    @Test
    void deleteSection() {
        // given
        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);

        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);

        ExtractableResponse<Response> 생성_요청_응답 = 지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);
        String 생성된_노선_uri = uri(생성_요청_응답);

        ExtractableResponse<Response> 새구간_하행역_생성결괴 = 역_생성(대림역_이름);

        Long 하행역_ID = stationId(새구간_하행역_생성결괴);
        int 상행역과_하행역_사이_거리 = 10;

        // given
        Map<String, String> 구간_생성요청_dto = 구간(하행종점_ID, 하행역_ID, 상행역과_하행역_사이_거리);
        post(구간_생성요청_dto, 생성된_노선_uri+"/sections");

        // when
        ExtractableResponse<Response> response = delete(생성된_노선_uri+"/sections?stationId=3");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청하고
     * Given 두번째 지하철 구간 생성을 요청하고
     * When 첫번째 구간 제거 요청하면
     * Then 구간 삭제 실패한다.
     */
    @DisplayName("지하철 구간 삭제")
    @Test
    void onlyLastSectionIsRemovable() {
        // given
        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);

        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);

        ExtractableResponse<Response> 생성_요청_응답 = 지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);
        String 생성된_노선_uri = uri(생성_요청_응답);

        ExtractableResponse<Response> 새구간_하행역_생성결괴 = 역_생성(대림역_이름);

        Long 하행역_ID = stationId(새구간_하행역_생성결괴);
        int 상행역과_하행역_사이_거리 = 10;

        // given
        Map<String, String> 구간_생성요청_dto = 구간(하행종점_ID, 하행역_ID, 상행역과_하행역_사이_거리);
        post(구간_생성요청_dto, 생성된_노선_uri+"/sections");

        // when
        ExtractableResponse<Response> response = delete(생성된_노선_uri+"/sections?stationId=1");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 구간 생성을 요청하고
     * When 지하철 노선을 조회하면
     * Then 지하철 역이 구간 순서대로 조회된다.
     */
    @DisplayName("지하철 역 목록 조회")
    @Test
    void getStations() {
        // given
        ExtractableResponse<Response> 상행종점_생성결과 = 역_생성(역삼역_이름);
        ExtractableResponse<Response> 하행종점_생성결과 = 역_생성(강남역_이름);

        Long 상행종점_ID = stationId(상행종점_생성결과);
        Long 하행종점_ID = stationId(하행종점_생성결과);

        ExtractableResponse<Response> 생성_요청_응답 = 지하철_노선_생성(신분당선_이름, 신분당선_색상, 상행종점_ID, 하행종점_ID, 10);
        String 생성된_노선_uri = uri(생성_요청_응답);

        ExtractableResponse<Response> 새구간_하행역_생성결괴 = 역_생성(대림역_이름);

        Long 하행역_ID = stationId(새구간_하행역_생성결괴);
        int 상행역과_하행역_사이_거리 = 10;

        // given
        Map<String, String> 구간_생성요청_dto = 구간(하행종점_ID, 하행역_ID, 상행역과_하행역_사이_거리);
        post(구간_생성요청_dto, 생성된_노선_uri+"/sections");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회("/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(((ArrayList<?>)response.jsonPath().getList("stations").get(0)).size()).isEqualTo(3);
    }

}
