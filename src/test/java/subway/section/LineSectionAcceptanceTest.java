package subway.section;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineRequest;
import subway.line.LineResponse;
import subway.linesection.LineSectionRepository;
import subway.linesection.LineSectionRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.line.LineFixture.*;
import static subway.station.StationFixture.지하철역_생성_ID;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineSectionAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    LineSectionRepository lineSectionRepository;

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
     * Given 지하철 노선이 1개가 등록되어있다. (노선 (Station: [first,second])
     * Given 새로운 구간 요청(지하철 노선의 하행 종점 역 second , 노선에 존재하지 않는 역 third)
     * When 지하철 노선에 구간을 등록 요청한다
     * Then 지하철 노선 목록에 구간의 하행역 B가 추가 되었는지 확인한다.
     * And 지하철 노선
     */
    @DisplayName("지하찰_구간_등록 - case 1")
    @Test
    void createStation_case_1() {
        //given
        LineSectionRequest request = new LineSectionRequest(secondStationId, thirdStationId, 20);
        //when
        지하철_구간_생성(fistLineId, request);
        //then
        LineResponse response = 지하철_노선_조회(fistLineId);
        assertThat(response.getStations().size()).isEqualTo(3);
        assertThat(response.getStations().get(2).getId()).isEqualTo(thirdStationId);
    }

    /**
     * Given 지하철역이 4개가 등록되어있다.
     * Given 지하철 노선이 1개가 등록되어있다.  (노선 (Station: [first,second])
     * Given 새로운 구간 요청(지하철 노선의 하행 종점 역이 아닌 third , 노선에 존재하지 않는 역 fourth)
     * When 지하철 노선에 구간을 등록 요청한다
     * Then 지하철 구간이 등록이 되지 않고 예외를 발생시킨다.
     */
    @DisplayName("지하찰_구간_등록 - case 2")
    @Test
    void createStation_case_2() {
        //given
        LineSectionRequest request = new LineSectionRequest(thirdStationId, fourthStationId, 20);
        //when
        //then
        지하철_구간_생성_응답_상태값_체크(1L, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * Given 지하철역이 4개가 등록되어있다.
     * Given 지하철 노선이 1개가 등록되어있다. (노선 (Station: [first,second])
     * Given 새로운 구간 요청(지하철 노선의 하행 종점 역 second , 노선에 존재하는 역 first)
     * When 지하철 노선에 구간을 등록 요청한다
     * Then 지하철 구간이 등록이 되지 않고 예외를 발생시킨다.
     */
    @DisplayName("지하찰_구간_등록 - case 3")
    @Test
    void createStation_case_3() {
        //given
        LineSectionRequest request = new LineSectionRequest(secondStationId, firstStationId, 20);
        //when
        //then
        지하철_구간_생성_응답_상태값_체크(1L, request, HttpStatus.BAD_REQUEST);

    }

    private void 지하철_구간_생성_응답_상태값_체크(Long lineId, LineSectionRequest request, HttpStatus expected) {
        RestAssured.given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post(String.format("/lines/%d/sections", lineId))
                .then().log().all()
                .statusCode(expected.value());
    }

    private void 지하철_구간_생성(Long lineId, LineSectionRequest request) {
        RestAssured.given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
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
