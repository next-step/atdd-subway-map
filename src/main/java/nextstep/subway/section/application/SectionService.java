package nextstep.subway.section.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.SectionRepository;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    public SectionResponse createSection(Long lineId, SectionRequest sectionRequest) {
        Section section =
                sectionRepository.save(
                        new Section(
                                lineService.getLineById(lineId),
                                stationService.getStationById(sectionRequest.getUpStationId()),
                                stationService.getStationById(sectionRequest.getDownStationId()),
                                sectionRequest.getDistance()
                        )
                );
        return SectionResponse.of(section);
    }

    public List<SectionResponse> getSectionList(Long lineId) {
        List<Section> sectionList = sectionRepository.findAll();
        return sectionList.stream().map(SectionResponse::of).collect(Collectors.toList());
    }
}
