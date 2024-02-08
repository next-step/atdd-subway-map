package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.line.presentation.request.LineCreateRequest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.LineSteps.*;

@Sql(value = "/sql/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    /**
     * given : 역과 노선을 생성한다.
     * when : 새로운 역을 구간으로 등록한다.
     * then : 추가한 역이 마지막 역인지 확인한다.
     */
    @Test
    void 구간을_생성하고_노선에_추가한다() {
        //given
        Long 선릉역_id = StationSteps.createStation("선릉역");
        Long 역삼역_id = StationSteps.createStation("역삼역");
        Long 강남역_id = StationSteps.createStation("강남역");

        LineCreateRequest lineCreateRequest = new LineCreateRequest(
                "2호선",
                "green",
                선릉역_id,
                역삼역_id,
                10
        );
        Long 이호선_id = createLine(lineCreateRequest);

        // when
        ExtractableResponse<Response> response = createSection(이호선_id, 역삼역_id, 강남역_id, 20);

        // then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.jsonPath().getLong("downStationId")).isEqualTo(2);
        assertThat(response.jsonPath().getLong("upStationId")).isEqualTo(3);
    }

    /**
     * given : 역과 노선을 생성한다.
     * when : 새로운 역을 구간으로 등록한다.
     * then : 추가하는 구간이 노선의 마지막 역이 아니면 실패한다.
     */
    @Test
    void 추가하는_구간이_노선의_마지막역이_아니면_실패한다() {
        // given
        Long 선릉역_id = StationSteps.createStation("선릉역");
        Long 역삼역_id = StationSteps.createStation("역삼역");
        Long 강남역_id = StationSteps.createStation("강남역");

        LineCreateRequest lineCreateRequest = new LineCreateRequest(
                "2호선",
                "green",
                선릉역_id,
                역삼역_id,
                10
        );
        Long 이호선_id = createLine(lineCreateRequest);

        // when
        ExtractableResponse<Response> response = createSection(이호선_id, 선릉역_id, 강남역_id, 20);

        // then
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat(response.jsonPath().getString("message")).isEqualTo("구간의 상행역과 노선의 하행역이 일치하지 않습니다.");
    }

    /**
     * given : 역과 노선을 생성한다.
     * when : 새로운 역을 구간으로 등록한다.
     * then : 추가하는 구간이 이미 노선에 존재하는 역이면 실패한다.
     */
    @Test
    void 추가하는_구간이_이미_노선에_존재하는_역이면_실패한다() {
        // given
        Long 선릉역_id = StationSteps.createStation("선릉역");
        Long 역삼역_id = StationSteps.createStation("역삼역");
        Long 강남역_id = StationSteps.createStation("강남역");

        LineCreateRequest lineCreateRequest = new LineCreateRequest(
                "2호선",
                "green",
                선릉역_id,
                역삼역_id,
                10
        );
        Long 이호선_id = createLine(lineCreateRequest);
        createSection(이호선_id, 역삼역_id, 강남역_id, 20);

        // when
        ExtractableResponse<Response> response = createSection(이호선_id, 강남역_id, 역삼역_id, 20);

        // then
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat(response.jsonPath().getString("message")).isEqualTo("이미 등록된 상태입니다.");
    }

    private ExtractableResponse<Response> createSection(Long lineId, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("downStationId", downStationId);
        params.put("upStationId", upStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}