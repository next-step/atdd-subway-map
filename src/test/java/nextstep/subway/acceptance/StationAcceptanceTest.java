package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.exception.DuplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.HttpRequestTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private static final String 기본주소 = "/stations";
    private static final String 기존지하철 = "기존지하철";
    private static final String 새로운지하철 = "새로운지하철";

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void 지하철역생성_테스트() {
        // when
        ExtractableResponse<Response> response = 기존지하철역생성();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }


    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void 지하철역목록조회_테스트() {
        /// given
        기존지하철역생성();

        새로운지하철역생성();

        // when
        ExtractableResponse<Response> response = 겟_요청(기본주소);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).contains(기존지하철, 새로운지하철);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void 지하철역삭제_테스트() {
        // given
        ExtractableResponse<Response> createResponse = 기존지하철역생성();

        // when
        ExtractableResponse<Response> response = 딜리트_요청(createResponse.header(HttpHeaders.LOCATION));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 역을 생성한다.
     * When 동일한 이름의 지하철 역 생성을 요청한다.
     * Then 지하철 역 생성이 실패한다.
     */
    @DisplayName("중복된 지하철 역은 생성이 실패한다")
    @Test
    void 중복된지하철역생성_테스트() {
        //given
        기존지하철역생성();

        //when
        ExtractableResponse<Response> response = 기존지하철역생성();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(DuplicationException.MESSAGE);
    }

    private ExtractableResponse<Response> 기존지하철역생성() {
        Map<String, String> params = 지하철역파라미터생성(기존지하철);
        return 포스트_요청(기본주소, params);
    }

    private ExtractableResponse<Response> 새로운지하철역생성() {
        Map<String, String> params = 지하철역파라미터생성(새로운지하철);
        return 포스트_요청(기본주소, params);
    }

    private Map<String, String> 지하철역파라미터생성(String 지하철역) {
        Map<String, String> params = new HashMap<>();
        params.put("name", 지하철역);
        return params;
    }
}
