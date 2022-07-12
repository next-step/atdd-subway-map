package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.common.AcceptanceTest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.LineAcceptanceTest.노선을_만들다;
import static nextstep.subway.acceptance.StationAcceptanceTest.역을_만들다;
import static nextstep.subway.acceptance.StationAcceptanceTest.지하철역_목록을_조회한다;
import static nextstep.subway.acceptance.common.RestAssuredTemplate.postRequestWithParameterAndRequestBody;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long _8호선;
    private Long 모란역;
    private Long 암사역;

    @BeforeEach
    void init() {
        모란역 = 역을_만들다("모란역").as(StationResponse.class).getId();
        암사역 = 역을_만들다("암사역").as(StationResponse.class).getId();
        _8호선 = 노선을_만들다("8호선", "bg-pink-500", 모란역, 암사역, 17L).as(LineResponse.class).getId();
    }

    /**
     * Given 지하철역 구간을 생성하고
     * When 지하철역 노선 목록 조회 시
     * Then 지하철 노선 목록 조회 시 추가 된 구간을 조회할 수 있다.
     */
    @Test
    void 지하철역_구간을_생성한다() {
        // given
        var 한성백제역 = 역을_만들다("한성백제역").as(StationResponse.class).getId();
        구간을_만들다(_8호선, 암사역, 한성백제역, 10L);

        // when
        var 지하철역_목록 = 지하철역_목록을_조회한다();

        // then
        var 지하철역_이름_목록 = 지하철역_목록.jsonPath().getList("name");
        assertThat(지하철역_이름_목록).containsExactly("모란역", "암사역", "한성백제역");

    }

    /**
     * Given 지하철역 구간을 2개 생성하고
     * When 지하철역 노선 목록 조회 시
     * Then 지하철 노선 목록 조회 시 추가 된 구간을 조회할 수 있다.
     */
    @Test
    void 지하철역_구간을_2개_생성한다() {
        // given
        var 한성백제역 = 역을_만들다("한성백제역").as(StationResponse.class).getId();
        구간을_만들다(_8호선, 암사역, 한성백제역, 10L);

        var 문정역 = 역을_만들다("문정역").as(StationResponse.class).getId();
        구간을_만들다(_8호선, 한성백제역, 문정역, 10L);

        // when
        var 지하철역_목록 = 지하철역_목록을_조회한다();

        // then
        var 지하철역_이름_목록 = 지하철역_목록.jsonPath().getList("name");
        assertThat(지하철역_이름_목록).containsExactly("모란역", "암사역", "한성백제역", "문정역");

    }

    private ExtractableResponse<Response> 구간을_만들다(Long id, Long upStationId, Long downStationId, Long distance) {
        var sectionRequest = new SectionRequest(upStationId, downStationId, distance);
        var response = postRequestWithParameterAndRequestBody("/lines/{id}/sections", id, sectionRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }
}
