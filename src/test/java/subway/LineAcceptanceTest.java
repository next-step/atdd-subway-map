package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.StationResponse;
import subway.feature.LineFeature;
import subway.feature.StationFeature;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {
    private Long shinUpStationId = 0L;
    private Long shinDownStationId = 0L;
    private Long bunUpStationId = 0L;
    private Long bunDownStationId = 0L;

    @BeforeEach
    void setUp(){

        //#. 상행역 하행역이 필수로 필요함.
        shinUpStationId = StationFeature.callCreateStation("신사역")
                .body()
                .as(StationResponse.class)
                .getId();
        shinDownStationId = StationFeature.callCreateStation("광교역")
                .body()
                .as(StationResponse.class)
                .getId();

        bunUpStationId = StationFeature.callCreateStation("청량리")
                .body()
                .as(StationResponse.class)
                .getId();

        bunDownStationId = StationFeature.callCreateStation("인천")
                .body()
                .as(StationResponse.class)
                .getId();
    }

    /**
     * 노선 이름(name)
     * 노선 색(color)
     */

    /**
     * Given 지하철 노선을 생성한다.
     * When 지하철 노선을 조회하면.
     * Then 생성된 지하철 노선이 조회된다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine(){
        //#. given 라인 생성.
        ExtractableResponse<Response> createResponse = LineFeature.callCreateLine("신분당선", "bg-red-600", shinUpStationId, shinDownStationId, 10);

        //#. when 라인 조회.
        ExtractableResponse<Response> showResponse = LineFeature.callGetLines();
        List<String> name = showResponse.jsonPath().getList("name", String.class);

        //#. then 생성된 지하철 노선이 조회.
        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertEquals(name.size(), 1),
                () -> assertThat(name.get(0)).isEqualTo("신분당선")
        );
    }

    /**
     * Given 지하철 노선을 2개 생성한다.
     * When 지하철 노선 목록을 조회하면
     * Then 생성한 지하철 노선 2개가 조회된다.
     */
    @DisplayName("지하철 노석 목록 조회")
    @Test
    void getLines(){
        //#. given 라인 2개 생성
        LineFeature.callCreateLine("신분당선", "bg-red-600", shinUpStationId, shinDownStationId, 10);
        LineFeature.callCreateLine("분당선", "bg-green-600", bunUpStationId, bunDownStationId, 20);

        //#. when 지하철 노선 목록을 조회하면
        ExtractableResponse<Response> showResponse = LineFeature.callGetLines();
        List<String> name = showResponse.jsonPath().getList("name", String.class);

        //#. then 지하철 노선 목록 조회시 2개의 노선을 조회할 수 있다.
        assertAll(
                () -> assertThat(showResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(name.size()).isEqualTo(2),
                () -> assertThat(name).containsExactly("신분당선", "분당선")
        );
    }

    /**
     * Given 지하철 노선을 생성한다.
     * When 지하철 노선을 조회한다.
     * Then 생선한 지하철 노선이 조회된다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine(){
        //#. given 라인 생성
        ExtractableResponse<Response> createLineResponse = LineFeature.callCreateLine("신분당선", "bg-red-600", shinUpStationId, shinDownStationId, 10);
        Long id = createLineResponse.jsonPath().getLong("id");

        //#. when 특정 라인 조회
        ExtractableResponse<Response> getLineResponse = LineFeature.callGetLine(id);
        String lineName = getLineResponse.jsonPath().getString("name");

        //#. then 생성된 특정 라인이 조회된다.
        assertAll(
                () -> assertThat( getLineResponse.statusCode() ).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat( lineName ).isEqualTo("신분당선")
        );
    }

    /**
     * Given 지하철 노선을 생성한다.
     * When 지하철 노선을 수정하고 조회한다.
     * Then 수정된 노선이 조회 된다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void modifyLine(){
        //#. given 라인 생성
        ExtractableResponse<Response> createLineResponse = LineFeature.callCreateLine("신분당선", "bg-red-600", shinUpStationId, shinDownStationId, 10);
        Long id = createLineResponse.jsonPath().getLong("id");

        //#. when 라인 수정 및 조회
        ExtractableResponse<Response> modifyResponse = LineFeature.callModifyLine(id, "다른분당선", "bg-blue-500");
        ExtractableResponse<Response> response = LineFeature.callGetLine(id);

        String responseName = response.jsonPath().getString("name");
        String responseColor = response.jsonPath().getString("color");

        //#. then 수정된 노선이 조회된다.
        assertAll(
                () -> assertThat( modifyResponse.statusCode() ).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat( responseName ).isEqualTo("다른분당선"),
                () -> assertThat( responseColor ).isEqualTo("bg-blue-500")
        );
    }

    /**
     * Given 지하철 노선을 생성한다.
     * When 지하철 노선을 삭제하고 조회한다.
     * Then 조회된 목록에서 삭제된 노선이 존재하지 않는다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine(){
        //#. given 라인 생성
        ExtractableResponse<Response> createLineResponse = LineFeature.callCreateLine("신분당선", "bg-red-600", shinUpStationId, shinDownStationId, 10);
        LineFeature.callCreateLine("분당선", "bg-green-600", bunUpStationId, bunDownStationId, 20);
        Long id = createLineResponse.jsonPath().getLong("id");

        //#. when 라인을 삭제 한 후 조회한다.
        ExtractableResponse<Response> deleteResponse = LineFeature.callDeleteLine(id);
        ExtractableResponse<Response> showResponse = LineFeature.callGetLines();

        List<String> responseLinesName = showResponse.jsonPath().getList("name", String.class);

        //#then 목록이 존재하지 않음.
        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(responseLinesName.size()).isEqualTo(1),
                () -> assertThat(responseLinesName).doesNotContain("신분당선")

        );
    }
}