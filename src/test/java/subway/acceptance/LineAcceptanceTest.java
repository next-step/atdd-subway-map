package subway.acceptance;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.line.presentation.request.CreateLineRequest;
import subway.line.presentation.request.UpdateLineRequest;
import subway.station.presentation.request.CreateStationRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.extractableResponse.LineApiExtractableResponse.*;
import static subway.acceptance.extractableResponse.StationApiExtractableResponse.createStation;

@DisplayName("지하철 노선 관련 기능")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선을_생성() {
        // given
        String 신분당선 = "신분당선";
        Long 강남역_ID = createStation(CreateStationRequest.from("강남역")).jsonPath().getLong("id");
        Long 신논현역_ID = createStation(CreateStationRequest.from("신논현역")).jsonPath().getLong("id");
        CreateLineRequest 신분당선_생성_정보 = CreateLineRequest.of(신분당선, "bg-red-600", 강남역_ID, 신논현역_ID, 10);

        // when
        assertThat(createLine(신분당선_생성_정보).statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames =
                selectLines().jsonPath().getList("lines.name", String.class);
        assertThat(lineNames).contains(신분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록을_조회() {
        // given
        String 신분당선 = "신분당선";
        Long 강남역_ID = createStation(CreateStationRequest.from("강남역_ID")).jsonPath().getLong("id");
        Long 신논현역_ID = createStation(CreateStationRequest.from("신논현역")).jsonPath().getLong("id");
        CreateLineRequest 신분당선_생성_정보 = CreateLineRequest.of(신분당선, "bg-red-600", 강남역_ID, 신논현역_ID, 10);

        createLine(신분당선_생성_정보);

        String 수인분당선 = "수인분당선";
        Long 압구정로데오역_ID = createStation(CreateStationRequest.from("압구정로데오역")).jsonPath().getLong("id");
        Long 강남구청역_ID = createStation(CreateStationRequest.from("강남구청역")).jsonPath().getLong("id");
        CreateLineRequest 수인분당선_생성_정보 = CreateLineRequest.of(수인분당선, "bg-yellow-600", 압구정로데오역_ID, 강남구청역_ID, 10);

        createLine(수인분당선_생성_정보);

        // when & then
        List<String> lineNames =
                selectLines().jsonPath().getList("lines.name", String.class);
        assertThat(lineNames).contains(신분당선, 수인분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선을_조회() {
        // given
        String 신분당선 = "신분당선";
        Long 강남역_ID = createStation(CreateStationRequest.from("강남역")).jsonPath().getLong("id");
        Long 신논현역_ID = createStation(CreateStationRequest.from("신논현역")).jsonPath().getLong("id");
        CreateLineRequest 신분당선_생성_정보 = CreateLineRequest.of(신분당선, "bg-red-600", 강남역_ID, 신논현역_ID, 10);

        Long 신분당선_ID = createLine(신분당선_생성_정보).jsonPath().getLong("id");

        // when & then
        String responseLineName = selectLine(신분당선_ID).jsonPath().get("name");
        assertThat(responseLineName).isEqualTo(신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void 지하철_노선을_수정() {
        // given
        String 신분당선 = "신분당선";
        Long 강남역_ID = createStation(CreateStationRequest.from("강남역")).jsonPath().getLong("id");
        Long 신논현역_ID = createStation(CreateStationRequest.from("신논현역")).jsonPath().getLong("id");
        CreateLineRequest 신분당선_생성_정보 = CreateLineRequest.of(신분당선, "bg-red-600", 강남역_ID, 신논현역_ID, 10);

        Long 신분당선_ID = createLine(신분당선_생성_정보).jsonPath().getLong("id");

        // when
        String 양재역 = "양재역";
        Long 양재역_ID = createStation(CreateStationRequest.from(양재역)).jsonPath().getLong("id");
        String 구분당선 = "구분당선";
        UpdateLineRequest 신분당선_수정_정보 = UpdateLineRequest.of("bg-blue-600", 15);

        modifyLine(신분당선_ID, 신분당선_수정_정보);

        // then
        JsonPath responseJsonPath = selectLine(신분당선_ID).jsonPath();

        assertThat((String) responseJsonPath.get("color")).isEqualTo("bg-blue-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void 지하철_노선을_삭제() {
        // given
        String 신분당선 = "신분당선";
        Long 강남역_ID = createStation(CreateStationRequest.from("강남역")).jsonPath().getLong("id");
        Long 신논현역_ID = createStation(CreateStationRequest.from("신논현역")).jsonPath().getLong("id");
        CreateLineRequest 신분당선_생성_정보 = CreateLineRequest.of(신분당선, "bg-red-600", 강남역_ID, 신논현역_ID, 10);

        Long 신분당선_ID = createLine(신분당선_생성_정보).jsonPath().getLong("id");

        // when
        deleteLine(신분당선_ID);

        // then
        assertThat(selectLine(신분당선_ID).statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
