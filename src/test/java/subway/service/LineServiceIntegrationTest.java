package subway.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.line.LineResponse;
import subway.controller.dto.line.LineSaveRequest;
import subway.model.line.Line;
import subway.model.line.LineRepository;
import subway.model.station.Station;
import subway.model.station.StationRepository;

@SpringBootTest
class LineServiceIntegrationTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Test
    @Transactional
    void saveLine() {

        Station upStation = stationRepository.save(Station.builder()
                                                      .name("정거장 A")
                                                      .build());
        Station downStations = stationRepository.save(Station.builder()
                                                      .name("정거장 B")
                                                      .build());

        LineResponse lineResponse = lineService.saveLine(LineSaveRequest.builder()
                                                                        .name("테스트 라인")
                                                                        .color("color")
                                                                        .distance(1L)
                                                                        .upStationId(upStation.getId())
                                                                        .downStationId(downStations.getId())
                                                                        .build());

        Line line = lineRepository.findById(lineResponse.getId())
                                  .orElseThrow(() -> new RuntimeException("no saved line"));

        System.out.println(line.getName());
        System.out.println(line.getStations().get(0).getName());
        System.out.println(line.getStations().get(1).getName());
        System.out.println(line.getSections().get(0).getId());
    }
}