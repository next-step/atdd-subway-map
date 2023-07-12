package subway.section;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineRequest;
import subway.line.LineResponse;
import subway.linesection.LineSectionRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineFixture.지하철_노선_목록_조회;
import static subway.line.LineFixture.지하철_노선_생성_ID;
import static subway.station.StationFixture.지하철역_생성_ID;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    int port;

    private Long firstStationId;
    private Long secondStationId;
    private Long thirdStationId;
    private Long fourthStationId;


    private Long fistLineId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        firstStationId = 지하철역_생성_ID("노원역");
        secondStationId = 지하철역_생성_ID("창동역");
        thirdStationId = 지하철역_생성_ID("강남역");
        fourthStationId = 지하철역_생성_ID("사당역");

        fistLineId = 지하철_노선_생성_ID(LineRequest.builder()
                .name("4호선")
                .color("light-blue")
                .upStationId(firstStationId)
                .downStationId(secondStationId)
                .distance(10)
                .build());
    }

    /**
     * Given 지하철역이 4개가 등록되어있다.
     * Given 지하철 노선이 1개가 등록되어있다.
     * When 지하철 노선에 구간을 등록한다
     * Then 지하철 노선 조회 시, 노선의 하행역은 등록한 구간의 하행역이어야한다.
     * Then 지하철 노선의 길이가, 기존 노선의 길이와 등록한 구간의 길이의 합이어야한다.
     */


    @DisplayName("지하찰_구간_등록")
    @Test
    void createStation() {
        //given
        LineSectionRequest request = new LineSectionRequest(secondStationId, thirdStationId,20);
        //when
        지하철_구간_생성(1L, request);
        //then
        List<LineResponse> response = 지하철_노선_목록_조회();
        assertThat(response.get(0).getStations().size()).isEqualTo(3);
    }

    private void 지하철_구간_생성(Long lineId, LineSectionRequest reqeust) {
        RestAssured.given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reqeust)
                .post(String.format("/lines/%d/sections", lineId))
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
    /**
     * Given 지하철역이 4개가 등록되어있다.
     * And 지하철 노선이 1개가 등록되어있다.
     * And 지하철 구간이 등록되어 있다.
     * When 지하철 구간을 제거한다.
     * Then 지하철 노선 조회 시, 하행역은 제거한 구간의 상행역이어야한다.
     * Then 지하철 노선의 길이가, 기존 노선의 길이에서 제거한 구간의 길이를 뺀 값이어야한다.
     */
}
