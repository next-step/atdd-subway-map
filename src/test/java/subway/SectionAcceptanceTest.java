package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineRequest;
import subway.section.SectionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.AcceptanceTestHelper.get;
import static subway.AcceptanceTestHelper.post;

@DisplayName("지하철 구간 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/stations.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SectionAcceptanceTest {
    private static final LineRequest 신분당선 =
            LineRequest.of("신분당선", "bg-red-600", 1L, 2L, 10);
    private static final SectionRequest 구간 = SectionRequest.of(2L, 3L, 3);

    /**
     * Given 처음 지하철 노선이 생성되면 구간도 함꼐 생성된다.
     * When 지하철 노선에 새로운 구간을 추가한다.
     * Then 지하철 노선 조회 시 모든 구간의 지하철역들을 찾을 수 있다.
     */
    @DisplayName("지하철 노선에 지하철 구간을 추가한다")
    @Test
    void createSection() {
        //given
        post("/lines", 신분당선);

        //when
        ExtractableResponse<Response> postResponse = post("/lines/{id}/sections", 1, 구간);
        ExtractableResponse<Response> listResponse = get("/lines/{id}", 1);

        //then
        assertThat(postResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(listResponse.jsonPath().getList("stations.name")).containsExactly("지하철역", "또다른지하철역", "새로운지하철역");
    }
}
