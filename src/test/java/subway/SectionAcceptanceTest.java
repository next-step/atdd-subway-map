package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.fixture.AcceptanceTest;
import subway.fixture.LineSteps;
import subway.fixture.SectionSteps;
import subway.fixture.StationSteps;
import subway.line.Color;
import subway.line.LineResponse;
import subway.section.SectionResponse;
import subway.station.StationResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 구간 등록 기능<br>
 *  지하철 노선에 구간을 등록하는 기능을 구현<br>
 *  새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.<br>
 *  이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.<br>
 *  새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.<br>
 */
@DisplayName("지하철 구간 관련 기능")
@AcceptanceTest
@Transactional
class SectionAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     *  <p>지하철 노선에 구간을 등록한다</p>
     *  <p>given 라인을 생성하고 역을 3개 생성한다</p>
     *  <p>when 새로운 구간을 등록하면</p>
     *  <p>then 새로운 구간의 상행역은 노선에 등록되어있는 하행 종점역이 된다</p>
     */
    @DisplayName("구간 등록")
    @Test
    void 구간_등록() {

        StationResponse 건대입구역 = StationSteps.createStation("건대입구역");
        StationResponse 구의역 = StationSteps.createStation("구의역");
        StationResponse 강변역 = StationSteps.createStation("강변역");

        Map<String, Object> param1 = new HashMap<>();
        param1.put("name", "2호선");
        param1.put("color", Color.GREEN.name());
        param1.put("upStationId", 건대입구역.getId());
        param1.put("downStationId", 구의역.getId());

        LineResponse lineResponse = LineSteps.createLine(param1);

        // given
        Map<String, Object> param2 = new HashMap<>();
        param2.put("upStationId", String.valueOf(구의역.getId()));
        param2.put("downStationId", String.valueOf(강변역.getId()));
        param2.put("distance", 10);

        // when
        SectionResponse section = SectionSteps.createSection(lineResponse.getId(), param2);

        // then
        assertThat(section.getUpStationId()).isEqualTo(구의역.getId());
        assertThat(section.getDownStationId()).isEqualTo(강변역.getId());
    }

    /**
     * <p>지하철 노선에 구간을 제거하는 기능 구현</p>
     * <p>지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.</p>
     * <p>지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.</p>
     * <p>새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.</p>
     */
    @DisplayName("구간 제거")
    @Test
    void delete_section() {

        StationResponse 건대입구역 = StationSteps.createStation("건대입구역");
        StationResponse 구의역 = StationSteps.createStation("구의역");
        StationResponse 강변역 = StationSteps.createStation("강변역");

        Map<String, Object> param1 = new HashMap<>();
        param1.put("name", "2호선");
        param1.put("color", Color.GREEN.name());
        param1.put("upStationId", 건대입구역.getId());
        param1.put("downStationId", 구의역.getId());

        LineResponse lineResponse = LineSteps.createLine(param1);

        Map<String, Object> param2 = new HashMap<>();
        param2.put("upStationId", String.valueOf(구의역.getId()));
        param2.put("downStationId", String.valueOf(강변역.getId()));
        param2.put("distance", 10);

        SectionSteps.createSection(lineResponse.getId(), param2);

        // when
        SectionSteps.deleteSection(lineResponse.getId(), 강변역.getId());

        // then
        LineResponse response = LineSteps.getLine(lineResponse.getId());
        assertThat(response.getStations().stream().map(StationResponse::getId).collect(Collectors.toList()))
                .containsExactly(건대입구역.getId(), 구의역.getId());

    }


    @DisplayName("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다")
    @Test
    void shouldDeleteLastStation() {

        StationResponse 건대입구역 = StationSteps.createStation("건대입구역");
        StationResponse 구의역 = StationSteps.createStation("구의역");
        StationResponse 강변역 = StationSteps.createStation("강변역");

        Map<String, Object> param1 = new HashMap<>();
        param1.put("name", "2호선");
        param1.put("color", Color.GREEN.name());
        param1.put("upStationId", 건대입구역.getId());
        param1.put("downStationId", 구의역.getId());

        LineResponse lineResponse = LineSteps.createLine(param1);

        Map<String, Object> param2 = new HashMap<>();
        param2.put("upStationId", String.valueOf(구의역.getId()));
        param2.put("downStationId", String.valueOf(강변역.getId()));
        param2.put("distance", 10);

        SectionSteps.createSection(lineResponse.getId(), param2);

        // when
        RestAssured.given().log().all()
                .pathParam("lineId", lineResponse.getId())
                .pathParam("stationId", 건대입구역.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .delete("lines/{lineId}/sections/{stationId}")
                .then().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("구간이 1개인 경우 역을 삭제할 수 없다")
    @Test
    void shouldDeleteSectionGreaterThan1() {

        StationResponse 건대입구역 = StationSteps.createStation("건대입구역");
        StationResponse 구의역 = StationSteps.createStation("구의역");
        StationResponse 강변역 = StationSteps.createStation("강변역");

        Map<String, Object> param1 = new HashMap<>();
        param1.put("name", "2호선");
        param1.put("color", Color.GREEN.name());
        param1.put("upStationId", 건대입구역.getId());
        param1.put("downStationId", 구의역.getId());

        LineResponse lineResponse = LineSteps.createLine(param1);


        // when
        RestAssured.given().log().all()
                .pathParam("lineId", lineResponse.getId())
                .pathParam("stationId", 건대입구역.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .delete("lines/{lineId}/sections/{stationId}")
                .then().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
