package subway.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.line.LineResponse;
import subway.controller.dto.line.LineSaveRequest;
import subway.model.line.Line;
import subway.model.line.LineService;
import subway.model.station.Station;
import subway.model.station.StationService;

@SpringBootTest
class LineCompositeServiceIntegrationTest {

    @Autowired
    private LineCompositeService lineCompositeService;

    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @Test
    @Transactional
    void saveLine() {

        // given
        Station upStation = Station_생성("정거장 A");
        Station downStation = Station_생성("정거장 B");

        // when
        LineResponse lineResponse = lineCompositeService.saveLine(LineSaveRequest.builder()
                                                                                 .name("테스트 라인")
                                                                                 .color("color")
                                                                                 .distance(1L)
                                                                                 .upStationId(upStation.getId())
                                                                                 .downStationId(downStation.getId())
                                                                                 .build());

        // then
        생성된_LINE_조회(lineResponse);
    }

    private void 생성된_LINE_조회(LineResponse lineResponse, String... stationNames) {

        Line line = lineService.findById(lineResponse.getId());

        for (int i = 0; i < stationNames.length; i++) {
            Assertions.assertThat(line.getStations()
                                      .get(i)
                                      .getName()).isEqualTo(stationNames[i]);
        }
    }

    private Station Station_생성(String stationName) {
        return stationService.save(Station.builder()
                                          .name(stationName)
                                          .build());
    }
}