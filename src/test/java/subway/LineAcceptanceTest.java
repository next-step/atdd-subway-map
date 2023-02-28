package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.dto.StationResponse;
import subway.feature.LineFeature;
import subway.feature.StationFeature;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private Long upStationId = 0L;
    private Long downStationId = 0L;

    @BeforeEach
    void setUp(){
        //#. 상행역 하행역이 필수로 필요함.
        StationResponse createdUpStation = StationFeature.callCreateStation("신사역").body().as(StationResponse.class);
        StationResponse createdDownStation = StationFeature.callCreateStation("광교역").body().as(StationResponse.class);

        upStationId = createdUpStation.getId();
        downStationId = createdDownStation.getId();
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
        ExtractableResponse<Response> createResponse = LineFeature.callCreateLine("신분당선", "bg-red-600", upStationId, downStationId, 10);

        //#. when 라인 조회.
        ExtractableResponse<Response> response = LineFeature.callGetLines();

        //#. then 생성된 지하철 노선이 조회.
        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value())
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

    }

    /**
     * Given 지하철 노선을 생성한다.
     * When 지하철 노선을 조회한다.
     * Then 생선한 지하철 노선이 조회된다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine(){

    }

    /**
     * Given 지하철 노선을 생성한다.
     * When 지하철 노선을 수정하고 조회한다.
     * Then 수정된 노선이 조회 된다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void modifyLine(){

    }

    /**
     * Given 지하철 노선을 생성한다.
     * When 지하철 노선을 삭제하고 조회한다.
     * Then 조회된 목록에서 삭제된 노선이 존재하지 않는다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine(){

    }
}