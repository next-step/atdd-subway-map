package subway.section.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.section.application.dto.request.SectionCreateRequest;
import subway.section.domain.Section;
import subway.section.domain.SectionCommandRepository;
import subway.station.application.StationService;
import subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionCommandRepository sectionCommandRepository;
    private final StationService stationService;

    public SectionService(final SectionCommandRepository sectionCommandRepository,
                          final StationService stationService) {
        this.sectionCommandRepository = sectionCommandRepository;
        this.stationService = stationService;
    }

    @Transactional
    public Long saveSection(final Line line, final SectionCreateRequest sectionCreateRequest) {
        Station upStation = stationService.findStationById(sectionCreateRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionCreateRequest.getDownStationId());
        Section section = Section.createSection(line, upStation, downStation, sectionCreateRequest.getDistance());

        line.validateSectionRegistered(upStation, downStation);

        sectionCommandRepository.save(section);
        line.changeDownStation(downStation);

        return section.getId();
    }
}
