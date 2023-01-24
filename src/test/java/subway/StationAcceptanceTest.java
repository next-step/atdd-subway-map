package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationAcceptanceTestHelper.*;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class StationAcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    @Order(1)
    void createStation() {
        // when
        final ExtractableResponse<Response> 지하철역_생성_응답 = 지하철역_생성_요청(지하철역_생성_요청_파라미터("강남역"));

        // then
        응답_코드_검증(지하철역_생성_응답, HttpStatus.CREATED);

        // then
        final List<String> 지하철역_이름_목록 = 지하철역_목록_조회함();
        assertThat(지하철역_이름_목록).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    @Order(2)
    void getStations() {
        // given
        지하철역_생성함(지하철역_생성_요청_파라미터("역삼역"));
        지하철역_생성함(지하철역_생성_요청_파라미터("선릉역"));

        // when
        final ExtractableResponse<Response> 지하철역_목록_조회_응답 = 지하철역_목록_조회_요청();

        // then
        응답_코드_검증(지하철역_목록_조회_응답, HttpStatus.OK);

        final List<String> 지하철역_이름_목록 = 지하철역_목록_조회_응답.jsonPath().getList("name", String.class);
        assertThat(지하철역_이름_목록).containsOnly("역삼역", "선릉역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    @Order(3)
    void deleteStation() {
        // given
        final String location = 지하철역_생성함(지하철역_생성_요청_파라미터("강남역"));

        // when
        final ExtractableResponse<Response> 지하철역_삭제_응답 = 지하철역_삭제_요청(location);

        // then
        응답_코드_검증(지하철역_삭제_응답, HttpStatus.NO_CONTENT);

        final List<String> 지하철역_이름_목록 = 지하철역_목록_조회함();
        assertThat(지하철역_이름_목록).doesNotContain("강남역");
    }

    private Map<String, Object> 지하철역_생성_요청_파라미터(final String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    private void 응답_코드_검증(final ExtractableResponse<Response> response, final HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }
}