package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    @BeforeEach
    void setUp() {
        StationSteps.createStation("강남역");
        StationSteps.createStation("역삼역");
        StationSteps.createStation("선릉역");
        LineSteps.createLine("2호선", "green", 1L, 2L, 10L);
    }

    /**
     * When 지하철 노선에 구간을 등록하면
     * Then 지하철 노선에 구간이 등록된다.
     */
    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createSection() {
        // when
        ExtractableResponse<Response> response = SectionSteps.createSection(1L, 2L, 3L, 10L);
        String locationHeader = response.header("Location");

        // then
        List<String> lineStationNames = LineSteps.getLineStationNames(locationHeader);
        assertThat(lineStationNames).contains("역삼역", "선릉역");
    }

}
