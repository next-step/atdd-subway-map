package subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.fixture.FieldFixture.노선_간_거리;
import static subway.common.fixture.FieldFixture.노선_상행_종점역_ID;
import static subway.common.fixture.FieldFixture.노선_색깔;
import static subway.common.fixture.FieldFixture.노선_이름;
import static subway.common.fixture.FieldFixture.노선_하행_종점역_ID;
import static subway.common.fixture.StationFixture.강남역;
import static subway.common.fixture.StationFixture.서울대입구역;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Map<String, String> 강남역_데이터 = 강남역.요청_데이터_생성();
        Long 강남역_id = 지하철역_생성_요청(강남역_데이터)
                .jsonPath().getLong("id");

        Map<String, String> 서울대입구역_데이터 = 서울대입구역.요청_데이터_생성();
        Long 서울대입구역_id = 지하철역_생성_요청(서울대입구역_데이터)
                .jsonPath().getLong("id");

        Map<String, Object> 이호선_데이터 = new HashMap<>();
        이호선_데이터.put(노선_이름.필드명(), "2호선");
        이호선_데이터.put(노선_색깔.필드명(), "bg-green-600");
        이호선_데이터.put(노선_상행_종점역_ID.필드명(), 강남역_id);
        이호선_데이터.put(노선_하행_종점역_ID.필드명(), 서울대입구역_id);
        이호선_데이터.put(노선_간_거리.필드명(), "10");

        given().log().all()
                .body(이호선_데이터)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post(LINE_BASE_URL)
        .then().log().all();


        // then
        List<Object> nameList = given().log().all()
                .when().get(LINE_BASE_URL)
                .then().log().all()
                    .extract()
                        .jsonPath().getList(노선_이름.필드명());

        assertThat(nameList).contains("2호선");
    }
}
