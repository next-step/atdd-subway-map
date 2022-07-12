package nextstep.subway.acceptance;


import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static nextstep.subway.acceptance.LineAcceptanceTest.createLine;
import static nextstep.subway.acceptance.StationAcceptanceTest.NAME;
import static nextstep.subway.acceptance.StationAcceptanceTest.createStation;

@DisplayName("지하철구간 관련 기능")
public class LineSectionAcceptanceTest extends BaseAcceptanceTest {


    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        createStation(Map.of(NAME, "남태령역"));
        createStation(Map.of(NAME, "사당역"));
        createStation(Map.of(NAME, "총신대입구역"));

        createLine("4호선", "bg-blue-300", 1, 2, 10);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 노선의 하행 종점역에 등록된 역을 상행 종점역으로 생성하면
     * Then 노선에 구간이 추가된다.
     */
    @Test
    @DisplayName("지하철 노선에 구간이 추가된다.")
    void createSectionTest() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 노선의 하행 종점역에 등록되지 않은 역을 상행 종점역으로 생성하면
     * Then 노선에 구간 추가가 실패한다.
     */
    @Test
    @DisplayName("지하철 노선에 하행 종점역이 아닌 역을 상행 종점역으로 설정하면 구간 추가가 실패한다.")
    void createSectionFailTest() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 노선에 등록된 역을 상행 또는 하행 종점역으로 생성하면
     * Then 노선에 구간 추가가 실패한다.
     */
    @Test
    @DisplayName("지하철 노선에 존재하는 구간을 추가하면 구간 추가가 실패한다.")
    void createSectionExistStationFailTest() {

    }


    /**
     * Given 지하철 노선을 생성하고
     * Given 구간을 추가로 생성한다음
     * When 노선에 등록된 마지막 역을 제거하면
     * Then 노선에 구간 제거가 성공한다.
     */
    @Test
    @DisplayName("지하철 노선에 구간 제거가 성공한다.")
    void deleteSelectionTest() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 노선에 등록된 마지막 역을 제거하면
     * Then 노선에 구간 제거에 실패한다 (구간이 1개)
     */
    @Test
    @DisplayName("지하철 노선에 구간 제거가 실패한다.")
    void deleteSelectionFailTest() {

    }


}
