package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.LineTestFixtures.*;
import static nextstep.subway.acceptance.StationTestFixtures.역_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선도 관련 기능")
public class LineAcceptanceTest extends AbstractAcceptanceTest{
    /**
     * when 지하철 노선을 생성하면
     * then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createSubwayLine() {
        //given
        String upStationId = 역_생성("강남역");
        String downStationId = 역_생성("양재역");

        ExtractableResponse<Response> response = generateLine("신분당선", "bg-red-600", upStationId, downStationId, "10");

        Assertions.assertAll(
                () -> assertThat(response.jsonPath().getList("stations.name")).containsExactly("강남역", "양재역"),
                () -> assertThat(response.jsonPath().getString("name")).contains("신분당선"),
                () -> assertThat(response.jsonPath().getList("stations.id")).contains(Integer.valueOf(upStationId), Integer.valueOf(downStationId)),
                () -> assertThat(response.response().statusCode()).isEqualTo(HttpStatus.CREATED.value())
        );

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void getSubwayLines() {
        String upSinbundagStationId = 역_생성("강남역");
        String downSinbundangStationId = 역_생성("양재역");

        generateLine("신분당선", "bg-red-600", upSinbundagStationId, downSinbundangStationId, "10");

        String upSinlimStationId = 역_생성("신림역");
        String downSinlimStationId = 역_생성("당곡역");

        generateLine("신림선", "bg-blue-500", upSinlimStationId, downSinlimStationId, "20");

        ExtractableResponse<Response> response = showLine();

        Assertions.assertAll(
                () -> assertThat(response.jsonPath().getList("name")).contains("신분당선", "신림선"),
                () -> assertThat(response.jsonPath().getList("station").size()).isEqualTo(2),
                () -> assertThat(response.response().statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void getSubwayLine() {
        String upSinlimStationId = 역_생성("신림역");
        String downSinlimStationId = 역_생성("당곡역");

        String id =
                generateLine("신림선", "bg-blue-500", upSinlimStationId, downSinlimStationId, "20")
                        .jsonPath()
                        .getString("id");

        ExtractableResponse<Response> extract = showLine(id);

        Assertions.assertAll(
                () -> assertThat(extract.jsonPath().getString("name")).isEqualTo("신림선"),
                () -> assertThat(extract.jsonPath().getList("stations").size()).isEqualTo(2),
                () -> assertThat(extract.jsonPath().getList("stations.name")).containsExactly("신림역","당곡역")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateSubwayLine() {
        String upSinlimStationId = 역_생성("신림역");
        String downSinlimStationId = 역_생성("당곡역");

        String id =
                generateLine("신림선", "bg-blue-500", upSinlimStationId, downSinlimStationId, "20")
                    .jsonPath()
                    .getString("id");

        updateLine("구미선", "bg-white-200", id);

        //수정된 데이터 출력
        ExtractableResponse<Response> response = showLine(id);

        assertThat(response.jsonPath().getString("name")).isEqualTo("구미선");
        assertThat(response.response().statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteSubwayLine() {
        String upSinlimStationId = 역_생성("신림역");

        String downSinlimStationId = 역_생성("당곡역");

        String id =
                generateLine("신림선", "bg-blue-500", upSinlimStationId, downSinlimStationId, "20")
                    .jsonPath()
                    .getString("id");

        ExtractableResponse<Response> extract = deleteLine(id);

        assertThat(extract.response().statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
