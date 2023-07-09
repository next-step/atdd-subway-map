package subway.section.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.line.domain.Line;
import subway.line.service.LineManageService;
import subway.line.service.LineReadService;
import subway.section.domain.Section;
import subway.section.model.SectionCreateRequest;
import subway.section.repository.SectionRepository;
import subway.station.service.StationService;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SectionManageService {
    private final StationService stationService;
    private final LineManageService lineManageService;
    private final LineReadService lineReadService;
    private final SectionRepository sectionRepository;

    public Section create(Long lineId, SectionCreateRequest request) {
        Line line = lineReadService.getLine(lineId);

        Section section = Section.builder()
                                 .downStation(stationService.get(request.getDownStationId()))
                                 .upStation(stationService.get(request.getUpStationId()))
                                 .distance(request.getDistance())
                                 .build();

        line.addSection(section);

        return sectionRepository.save(section);
    }
}
