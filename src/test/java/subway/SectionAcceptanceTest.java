package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.SectionAcceptanceTestUtil.*;
import static subway.StationAcceptanceTestUtil.*;

@DisplayName("지하철 구간 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    int port;

    private Long stationId1;
    private Long stationId2;
    private Long lineId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        stationId1 = 지하철_역_등록_요청("송파역").jsonPath().getLong("id");
        stationId2 = 지하철_역_등록_요청("석촌역").jsonPath().getLong("id");

        lineId = LineAcceptanceTestUtil.지하철_노선_생성_요청(stationId1, stationId2, "8호선").jsonPath().getLong("id");
    }

    /**
     * Given 새로운 역을 생성하고
     * When 지하철 구간을 등록하면
     * Then 생성한 지하철 구간의 정보를 응답받을 수 있다.
     */

    @DisplayName("지하철 구간 등록")
    @Test
    public void 지하철_구간_등록() {
        // Given
        long stationId3 = 지하철_역_등록_요청("잠실역").jsonPath().getLong("id");

        // When
        ExtractableResponse<Response> response = 지하철_구간_생성(lineId, stationId2, stationId3, 10);

        // Then
        등록한_지하철_구간_정보_응답_확인(response, stationId3);
    }

    private void 등록한_지하철_구간_정보_응답_확인(ExtractableResponse<Response> response, long stationId3) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("downStationId")).isEqualTo(stationId3);
    }


    /**
     * Given 새로운 역 생성 및 구간을 등록하고
     * When 등록한 구간을 삭제하면
     * Then 등록한 구간 조회 시 찾을 수 없다.
     */

    @DisplayName("지하철 구간 삭제")
    @Test
    void 구간_삭제() {
        // Given
        long stationId3 = 지하철_역_등록_요청("잠실역").jsonPath().getLong("id");
        지하철_구간_생성(lineId, stationId1, stationId2, 10);
        지하철_구간_생성(lineId, stationId2, stationId3, 10);

        지하철_구간_삭제(lineId, stationId3);

        ExtractableResponse<Response> findSectionsResponse = 지하철_구간_전체_조회();
        삭제된_지하철_구간이_없는지_확인(findSectionsResponse, stationId3);
    }


    private void 삭제된_지하철_구간이_없는지_확인(ExtractableResponse<Response> response, long deletedSectionId) {
        List<Long> downStationIds = response.jsonPath().getList("downStationId", Long.class);
        assertThat(downStationIds).doesNotContain(deletedSectionId);
    }
}
