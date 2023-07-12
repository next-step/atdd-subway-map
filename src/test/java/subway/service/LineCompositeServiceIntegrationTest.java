package subway.service;

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

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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
        String stationA = "정거장 A";
        String stationB = "정거장 B";
        Station upStation = Station_생성(stationA);
        Station downStation = Station_생성(stationB);

        // when
        LineResponse lineResponse = lineCompositeService.saveLine(LineSaveRequest.builder()
                                                                                 .name("테스트 라인")
                                                                                 .color("color")
                                                                                 .distance(1L)
                                                                                 .upStationId(upStation.getId())
                                                                                 .downStationId(downStation.getId())
                                                                                 .build());

        // then
        생성된_LINE_조회(lineResponse, stationA, stationB);
    }

    private void 생성된_LINE_조회(LineResponse lineResponse, String... stationNames) {

        Line line = lineService.findById(lineResponse.getId());

        assertThat(lineResponse.getStations()).hasSize(stationNames.length);
        assertThat(line.getStations()
                       .stream()
                       .map(Station::getName)
                       .collect(Collectors.toList()))
                .containsAll(List.of(stationNames));
    }

    private Station Station_생성(String stationName) {
        return stationService.save(Station.builder()
                                          .name(stationName)
                                          .build());
    }
}