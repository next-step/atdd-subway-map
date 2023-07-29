package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.station.Station;
import subway.station.StationService;

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
    public SectionResponse saveSection(SectionRequest sectionRequest) {
        Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        Section section = new Section(upStation, downStation, sectionRequest.getDistance());
        Section savedSection = sectionRepository.save(section);

        return createSectionResponse(savedSection);
    }

    @Transactional
    public Section saveSectionByEntity(Section section) {
        Section savedSection = sectionRepository.save(section);
        return savedSection;
    }

    public Section findById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void deleteAllSection(Line line) {
        sectionRepository.deleteAll(line.getSections());
    }

    private SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(
                section.getId()
        );
    }
}