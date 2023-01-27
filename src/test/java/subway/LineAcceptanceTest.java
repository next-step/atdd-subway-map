package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.common.DatabaseCleaner;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.executor.LineAcceptanceExecutor;
import subway.executor.StationServiceExecutor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {


    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleaner cleaner;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        cleaner.afterPropertiesSet();
        cleaner.execute();
    }

    @Test
    @DisplayName("지하철 노선 생성")
    void createLine() {
        //when 지하철 노선을 생성하면
        Station 정자역 = StationServiceExecutor.createStation("정자역").as(Station.class);
        Station 판교역 = StationServiceExecutor.createStation("판교역").as(Station.class);
        ExtractableResponse<Response> response = createLine("신분당선", "red", 정자역, 판교역);

        //then  지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()), () -> assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선"), () -> assertThat(response.jsonPath().getList("stations", Station.class)).hasSize(2));

    }

    @Test
    @DisplayName("지하철 노선 목록 조회")
    void showLines() {
        //given 2개의 지하철 노선을 생성하고
        Station 정자역 = StationServiceExecutor.createStation("정자역").as(Station.class);
        Station 판교역 = StationServiceExecutor.createStation("판교역").as(Station.class);
        createLine("신분당선", "red", 정자역, 판교역);

        Station 광화문역 = StationServiceExecutor.createStation("광화문역").as(Station.class);
        Station 서대문역 = StationServiceExecutor.createStation("서대문역").as(Station.class);
        createLine("5호선", "purple", 광화문역, 서대문역);

        //when 지하철 노선 목록을 조회하면
        ExtractableResponse<Response> response = LineAcceptanceExecutor.showLines();

        //then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()), () -> assertThat(response.jsonPath().getList("name", String.class)).hasSize(2), () -> assertThat(response.jsonPath().getList("name", String.class)).containsAnyOf("신분당선", "5호선"));

    }

    @Test
    @DisplayName("지하철 노선 조회")
    void showLine() {
        //given 지하철 노선을 생성하고
        Station 정자역 = StationServiceExecutor.createStation("정자역").as(Station.class);
        Station 판교역 = StationServiceExecutor.createStation("판교역").as(Station.class);
        LineResponse newLine = createLine("신분당선", "red", 정자역, 판교역).as(LineResponse.class);

        //when 생성한 지하철 노선을 조회하면
        ExtractableResponse<Response> response = LineAcceptanceExecutor.showLine(newLine.getId());

        //then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()), () -> assertThat(response.jsonPath().getString("name")).isEqualTo(newLine.getName()), () -> assertThat(response.jsonPath().getString("color")).isEqualTo(newLine.getColor()), () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(newLine.getId()), () -> assertThat(response.jsonPath().getList("stations.name", String.class)).hasSize(2), () -> assertThat(response.jsonPath().getList("stations.name", String.class)).containsAnyOf("정자역", "판교역"));

    }

    @Test
    @DisplayName("지하철 노선 수정")
    void updateLine() {
        //given 지하철 노선을 생성하고
        Station 정자역 = StationServiceExecutor.createStation("정자역").as(Station.class);
        Station 판교역 = StationServiceExecutor.createStation("판교역").as(Station.class);
        LineResponse newLine = createLine("신분당선", "red", 정자역, 판교역).as(LineResponse.class);

        //when 생성한 지하철 노선을 수정하면

        LineRequest updateRequest = new LineRequest();
        updateRequest.setName("경강선");
        updateRequest.setColor("blue");
        ExtractableResponse<Response> updateResponse = LineAcceptanceExecutor.updateLine(newLine.getId(), updateRequest);

        //then 해당 지하철 노선 정보는 수정된다
        LineResponse updatedLine = LineAcceptanceExecutor.showLine(newLine.getId()).as(LineResponse.class);

        assertAll(() -> assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value()), () -> assertThat(updatedLine.getName()).isEqualTo(updateRequest.getName()), () -> assertThat(updatedLine.getColor()).isEqualTo(updateRequest.getColor()));

    }


    @Test
    @DisplayName("지하철 노선 삭제")
    void deleteLine() {
        //given 지하철 노선을 생성하고
        Station 정자역 = StationServiceExecutor.createStation("정자역").as(Station.class);
        Station 판교역 = StationServiceExecutor.createStation("판교역").as(Station.class);
        LineResponse newLine = createLine("신분당선", "red", 정자역, 판교역).as(LineResponse.class);

        //when 생성한 지하철 노선을 삭제하면
        ExtractableResponse<Response> deleteLineResponse = LineAcceptanceExecutor.deleteLine(newLine.getId());

        //then 해당 지하철 노선 정보는 삭제된다
        assertThat(deleteLineResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    private static ExtractableResponse<Response> createLine(String lineName, String red, Station upStation, Station downStation) {

        LineRequest lineRequest = new LineRequest();
        lineRequest.setName(lineName);
        lineRequest.setColor(red);
        lineRequest.setUpStationId(upStation.getId());
        lineRequest.setDownStationId(downStation.getId());
        lineRequest.setDistance(10L);

        return LineAcceptanceExecutor.createLine(lineRequest);
    }

}
