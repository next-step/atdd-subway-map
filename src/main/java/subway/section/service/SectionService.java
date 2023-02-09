package subway.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.dto.SectionRequest;
import subway.line.repository.Section;
import subway.section.repository.SectionRepository;
import subway.station.repository.Station;
import subway.station.service.StationService;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public SectionService(SectionRepository sectionRepository, StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    @Transactional
    public Section createSection(SectionRequest sectionRequest) {
        return sectionRepository.save(toSectionEntity(sectionRequest));
    }

    @Transactional
    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }

    private Section toSectionEntity(SectionRequest sectionRequest) {
        Station downStation = stationService.findStation(sectionRequest.getDownStationId());
        Station upStation = stationService.findStation(sectionRequest.getUpStationId());

        return new Section(
                downStation,
                upStation,
                sectionRequest.getDistance()
        );
    }
}
