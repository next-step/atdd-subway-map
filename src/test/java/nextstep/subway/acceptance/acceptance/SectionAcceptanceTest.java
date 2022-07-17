package nextstep.subway.acceptance.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static nextstep.subway.acceptance.sample.LineSampleData.신분당선_노선을_3개를_생성한다;
import static nextstep.subway.acceptance.sample.LineSampleData.신분당선_노선을_생성한다;
import static nextstep.subway.acceptance.template.LineRequestTemplate.지하철노선_목록_조회를_요청한다;
import static nextstep.subway.acceptance.template.LineRequestTemplate.지하철노선_조회를_요청한다;
import static nextstep.subway.acceptance.template.LineRequestTemplate.지하철노선을_생성을_요청한다;
import static nextstep.subway.acceptance.template.SectionRequestTemplate.지하철구간_등록을_요청한다;
import static nextstep.subway.acceptance.template.SectionRequestTemplate.지하철구간_삭제를_요청한다;
import static nextstep.subway.acceptance.template.StationRequestTemplate.지하철역_생성을_요청한다;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SectionAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * Given 지하철 구간을 등록하고,
     * When 지하철 노선을 조회하면,
     * Then 생성한 지하철 구간이 포함된 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철구간을 등록한다.")
    @Test
    void 지하철구간_등록성공() {
        // given
        long upStationId = 지하철역_생성을_요청한다("강남역").jsonPath().getLong("id");
        long downStationId = 지하철역_생성을_요청한다("신논현역").jsonPath().getLong("id");
        long lineId = 지하철노선을_생성을_요청한다("신분당선", "bg-red-600", upStationId, downStationId, (long) 10).jsonPath().getLong("id");

        // when
        long newStationId = 지하철역_생성을_요청한다("양재역").jsonPath().getLong("id");
        지하철구간_등록을_요청한다(lineId, downStationId, newStationId, 10);

        // then
        ExtractableResponse<Response> lineResponse = 지하철노선_조회를_요청한다(lineId);
        List<String> lineNames = lineResponse.jsonPath().get("stations.name");
        assertThat(lineNames).containsExactly("강남역", "신논현역", "양재역");
    }

    /**
     * Given 2개의 구간을 가진 지하철 노선을 생성하고,
     * When
     * 새로운 구간의 상행역이 기존 지하철 노선의 하행종점역이고,
     * 새로운 구간의 하행역이 기존 지하철 노선에 포함되어 있지 않은
     * 지하철 구간을 등록하면,
     * Then 지하철 구간 등록에 성공한다.
     */
    @DisplayName("새로운 구간의 상행역이 하행종점역이고, 노선에 없으면 구간 등록할 수 있다.")
    @Test
    void 지하철구간이_지하철노선의_하행종점역이고_해당노선에_존재하지않으면_등록성공() {
        // given
        long upStationId = 지하철역_생성을_요청한다("강남역").jsonPath().getLong("id");
        long downStationId = 지하철역_생성을_요청한다("신논현역").jsonPath().getLong("id");
        long lineId = 지하철노선을_생성을_요청한다("신분당선", "bg-red-600", upStationId, downStationId, (long) 10).jsonPath().getLong("id");

        // when
        long newStationId = 지하철역_생성을_요청한다("양재역").jsonPath().getLong("id");
        ExtractableResponse<Response> response = 지하철구간_등록을_요청한다(lineId, downStationId, newStationId, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 2개의 구간을 가진 지하철 노선을 생성하고,
     * When
     * 새로운 구간의 상행역이 기존 지하철 노선의 하행종점역이 아닌
     * 지하철 구간을 추가하면,
     * Then 지하철 구간 등록에 실패한다.
     */
    @DisplayName("새로운 구간의 상행역이 노선의 하행종점역이 아니면 구간을 등록할 수 없다.")
    @Test
    void 지하철구간의_상행역이_하행종점역이_아닐경우_등록실패() {
        // given
        long upStationId = 지하철역_생성을_요청한다("강남역").jsonPath().getLong("id");
        long downStationId = 지하철역_생성을_요청한다("신논현역").jsonPath().getLong("id");
        long lineId = 지하철노선을_생성을_요청한다("신분당선", "bg-red-600", upStationId, downStationId, (long) 10).jsonPath().getLong("id");

        // when
        long newStationId = 지하철역_생성을_요청한다("양재역").jsonPath().getLong("id");
        long failStationId = 지하철역_생성을_요청한다("수원역").jsonPath().getLong("id");
        ExtractableResponse<Response> response = 지하철구간_등록을_요청한다(lineId, failStationId, newStationId, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    /**
     * Given 3개의 구간을 가진 지하철 노선을 생성하고,
     * When
     * 새로운 구간의 상행역이 기존 지하철 노선의 하행종점역이고,
     * 새로운 구간의 하행역이 기존 지하철 노선에 포함되어 있는
     * 지하철 구간을 등록하면,
     * Then 지하철 구간 등록에 실패한다.
     */
    @DisplayName("새로운 구간의 상행역이 하행종점역이지만, 하행역이 노선에 포함되어있으면 구간 등록할 수 없다.")
    @Test
    void 지하철구간의_상행역이_노선의_하행종점역이지만_하행역이_노선에_포함되어있으면_등록실패() {
        // given
        long 강남역 = 지하철역_생성을_요청한다("강남역").jsonPath().getLong("id");
        long 신논현역 = 지하철역_생성을_요청한다("신논현역").jsonPath().getLong("id");
        long 양재역 = 지하철역_생성을_요청한다("양재역").jsonPath().getLong("id");
        long 신분당선 = 지하철노선을_생성을_요청한다("신분당선", "bg-red-600", 강남역, 신논현역, (long) 15).jsonPath().getLong("id");
        지하철구간_등록을_요청한다(신분당선, 신논현역, 양재역, 7);

        // when
        ExtractableResponse<Response> response = 지하철구간_등록을_요청한다(신분당선, 양재역, 신논현역, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 2개의 구간을 가진 지하철 노선을 생성하고,
     * When 지하철 구간을 제거하면,
     * Then 해당 지하철 노선에 지하철 구간 정보를 조회할 수 없다.
     */
    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void 지하철구간_삭제성공() {
        // given
        long 강남역 = 지하철역_생성을_요청한다("강남역").jsonPath().getLong("id");
        long 신논현역 = 지하철역_생성을_요청한다("신논현역").jsonPath().getLong("id");
        long 양재역 = 지하철역_생성을_요청한다("양재역").jsonPath().getLong("id");
        long 신분당선 = 지하철노선을_생성을_요청한다("신분당선", "bg-red-600", 강남역, 신논현역, (long) 15).jsonPath().getLong("id");
        지하철구간_등록을_요청한다(신분당선, 신논현역, 양재역, 7);

        // when
        ExtractableResponse<Response> response = 지하철구간_삭제를_요청한다(신분당선, 양재역);

        // then
        ExtractableResponse<Response> lineResponse = 지하철노선_조회를_요청한다(신분당선);
        List<String> lineNames = lineResponse.jsonPath().get("stations.name");
        assertThat(lineNames).containsExactly("강남역", "신논현역");
    }

    /**
     * Given 2개의 구간을 가진 지하철 노선을 생성하고,
     * When 지하철 하행 종점역이 아닌 역을 제거하면,
     * Then 지하철 구간 삭제 요청은 실패한다.
     */
    @DisplayName("삭제하는 지하철구간이 하행종점역 구간이 아니면 삭제할 수 없다.")
    @Test
    void 지하철구간의_상행역이_하행종점역이_아닐경우_삭제실패() {
        // given
        long 강남역 = 지하철역_생성을_요청한다("강남역").jsonPath().getLong("id");
        long 신논현역 = 지하철역_생성을_요청한다("신논현역").jsonPath().getLong("id");
        long 양재역 = 지하철역_생성을_요청한다("양재역").jsonPath().getLong("id");
        long 신분당선 = 지하철노선을_생성을_요청한다("신분당선", "bg-red-600", 강남역, 신논현역, (long) 15).jsonPath().getLong("id");
        지하철구간_등록을_요청한다(신분당선, 신논현역, 양재역, 7);

        // when
        ExtractableResponse<Response> response = 지하철구간_삭제를_요청한다(신분당선, 강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 1개의 구간을 가진 지하철 노선을 생성하고,
     * WHen 지하철 구간을 제거하면,
     * Then 지하철 구간 삭제 요청은 실패한다.
     */
    @DisplayName("지하철 구간이 1개인 노선은 삭제할 수 없다.")
    @Test
    void 지하철구간이_1개인_노선은_삭제실패() {
        // given

        // when

        // then
    }

}
