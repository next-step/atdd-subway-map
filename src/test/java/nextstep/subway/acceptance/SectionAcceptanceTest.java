package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@DisplayName("지하철 구간 관리 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SectionAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선에 구간을 등록하면
     * Then 지하철 노선 구간 조회 시 생성한 구간을 찾을 수 있다
     */
    @DisplayName("지하철 노선에 구간을 생성한다.")
    @Test
    void createSection() {
        // given
        // when
        // then
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선에 구간을 등록할 때, 노선의 하행 종점역과 새로운 구간의 상행역이 일치하지 않으면
     * Then 에러가 발생한다
     */
    @DisplayName("지하철 노선에 구간을 생성 시 노선의 하행 종점역과 새로운 구간의 상행역이 일치하지 않으면 익셉션 발생한다.")
    @Test
    void createSectionStationMismatchException() {
        // given
        // when
        // then
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선에 구간을 등록할 때, 새로운 구간의 하행역이 노선에 등록되어 있다면
     * Then 에러가 발생한다
     */
    @DisplayName("지하철 노선에 구간을 생성 시 새로운 구간의 하행역이 노선에 등록되어 있다면 익셉션 발생한다.")
    @Test
    void createSectionAlreadyExistStationException() {
        // given
        // when
        // then
    }

    /**
     * Given 구간을 등록하고
     * When 구간에 등록된 지하철역을 삭제하면
     * Then 지하철 노선에서 지하철 역은 없어진다
     */
    @DisplayName("지하철 노선 구간에서 지하철역을 삭제한다.")
    @Test
    void deleteStationInSection() {
        // given
        // when
        // then
    }

    /**
     * Given 구간을 등록하고
     * When 구간에 등록된 하행 종점역이 아닌 역을 삭제하면
     * Then 에러가 발생한다
     */
    @DisplayName("지하철 노선 구간에서 지하철역을 삭제 시 하행 종점역이 아니면 익셉션이 발생한다.")
    @Test
    void deleteStationDownTerminalStationException() {
        // given
        // when
        // then
    }


    /**
     * Given 구간을 등록하고
     * When 구간에 등록된 상행 종점역과 하행 종점역만 있는 경우 삭제 시
     * Then 에러가 발생한다
     */
    @DisplayName("지하철 노선 구간에서 지하철역을 삭제 시 상행 종점역과 하행 종점역만 있는 경우 익셉션이 발생한다.")
    @Test
    void deleteStationInOnlyOneStationSectionException() {
        // given
        // when
        // then
    }


}
