package subway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

@SpringBootTest
public class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    private StationResponse 광교역;
    private StationResponse 강남역;

    @BeforeEach
    void setUp() {
        this.광교역 = beforeTestCreateStation("광교역");
        this.강남역 = beforeTestCreateStation("강남역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        LineRequest lineRequest = new LineRequest("신분당선", "red", 10, 강남역.getId(), 광교역.getId());
        LineResponse line = lineService.createLine(lineRequest);
        System.out.println(line);
    }

    private StationResponse beforeTestCreateStation(String name) {
       return stationService.saveStation(new StationRequest(name));
    }



}
