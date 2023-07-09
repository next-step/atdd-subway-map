package subway.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.section.SectionResponse;
import subway.controller.dto.section.SectionSaveRequest;
import subway.model.line.Line;
import subway.model.line.LineService;
import subway.model.section.Section;
import subway.model.station.Station;
import subway.model.station.StationService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SectionCompositeServiceIntegrationTest {

    @Autowired
    private SectionCompositeService sectionCompositeService;

    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @Test
    @Transactional
    void saveSection() {

        // given
        Station upStation = Station_생성("정거장 A");
        Station downStation = Station_생성("정거장 B");
        Line line = LINE_생성(upStation, downStation);

        Station newStation = Station_생성("정거장 C");

        // when
        SectionResponse sectionResponse = sectionCompositeService.saveSection(line.getId(), SectionSaveRequest_생성(downStation, newStation));

        // then
        생성된_Section_조회(sectionResponse, downStation.getName(), newStation.getName());
        LINE_조회_및_검증(line.getId(), upStation.getName(), downStation.getName(), newStation.getName());
    }

    private static SectionSaveRequest SectionSaveRequest_생성(Station downStation, Station newStation) {
        return SectionSaveRequest.builder()
                                 .upStationId(downStation.getId())
                                 .downStationId(newStation.getId())
                                 .distance(1L)
                                 .build();
    }

    private void LINE_조회_및_검증(Long lineId, String... stationNames) {

        Line line = lineService.findById(lineId);

        List<Station> stations = line.getStations();

        assertThat(stations.size()).isEqualTo(stationNames.length);

        for (int i = 0; i < stationNames.length; i++) {
            assertThat(stations.get(i)
                               .getName()).isEqualTo(stationNames[i]);
        }
    }

    private void 생성된_Section_조회(SectionResponse sectionResponse, String upStationName, String downStationName) {
        assertThat(sectionResponse.getUpStation()
                                  .getName()).isEqualTo(upStationName);
        assertThat(sectionResponse.getDownStation()
                                  .getName()).isEqualTo(downStationName);
    }

    private Line LINE_생성(Station upStation, Station downStation) {
        long distance = 10L;
        return lineService.save(Line.builder()
                                    .name("라인1")
                                    .color("color")
                                    .sections(List.of(Section.builder()
                                                                .upStation(upStation)
                                                                .downStation(downStation)
                                                                .distance(distance)
                                                                .build()))
                                    .distance(distance)
                                    .build());
    }

    private Station Station_생성(String stationName) {
        return stationService.save(Station.builder()
                                          .name(stationName)
                                          .build());
    }
}