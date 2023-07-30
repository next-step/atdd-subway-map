package subway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        assertThat(line.getName()).isEqualTo("신분당선");
    }

    @DisplayName("지하철 노선 구간 등록")
    @Test
    void addLine() {
        //given 지하철 노선을 생성한다. 새로운 지하철 역을 등록한다.
        LineRequest lineRequest = new LineRequest("신분당선", "red", 10, 강남역.getId(), 광교역.getId());
        LineResponse line = lineService.createLine(lineRequest);
        StationResponse 수원역 = beforeTestCreateStation("수원역");
        // when 지하철 노선 구간을 만들어서 기존 노선에 추가한다.
        LineResponse lineResponse = lineService.addLineStation(line.getId(), new AddLineRequest(광교역.getId(), 수원역.getId(), 10));
        // then 노선 추가 하행종점, 상행종점을 확인한다.
        assertThat(lineResponse.getStationResponseList().get(0).getId()).isEqualTo(강남역.getId());
        assertThat(lineResponse.getStationResponseList().get(1).getId()).isEqualTo(수원역.getId());
    }

    @DisplayName("단일 노선 구간 제거 테스트")
    @Test
    void deleteSection() {
        //given 단일 노선을 만든다. 노선 추가를 한다.
        LineRequest lineRequest = new LineRequest("신분당선", "red", 10, 강남역.getId(), 광교역.getId());
        LineResponse line = lineService.createLine(lineRequest);
        StationResponse 수원역 = stationService.saveStation(new StationRequest("수원역"));
        lineService.addLineStation(line.getId(), new AddLineRequest(광교역.getId(), 수원역.getId(), 10));
        //when 상행선 구간 제거를 한다
        lineService.deleteLineDownStation(line.getId(), 수원역.getId());

        //then 라인을 조회한다.
        LineResponse lineResponse = lineService.findLine(line.getId());

        assertThat(lineResponse.getStationResponseList().get(0).getId()).isEqualTo(강남역.getId());
        assertThat(lineResponse.getStationResponseList().get(1).getId()).isEqualTo(광교역.getId());
    }

    @DisplayName("단일 노선 구간 중 상행선 제거시 익셉션 테스트")
    @Test
    void deleteUpSection() {
        //given 단일 노선을 만든다.
        LineRequest lineRequest = new LineRequest("신분당선", "red", 10, 강남역.getId(), 광교역.getId());
        LineResponse line = lineService.createLine(lineRequest);
        //when 상행선 구간 제거를 한다

        //then 익셉션을 확인한다.
        assertThatThrownBy(()->lineService.deleteLineDownStation(line.getId(), lineRequest.getUpStationId()))
                .isInstanceOf(RuntimeException.class);
    }

    private StationResponse beforeTestCreateStation(String name) {
       return stationService.saveStation(new StationRequest(name));
    }
}
