package subway.section;

import org.springframework.stereotype.Service;
import subway.line.LineRepository;
import subway.section.dto.CreateSectionRequest;
import subway.section.dto.SectionResponse;
import subway.station.StationRepository;

@Service
public class SectionService {

    private SectionRepository sectionRepository;
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository,
                          StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SectionResponse saveSection(Long lineId, CreateSectionRequest request) {
        Section section = sectionRepository.save(this.createEntity(lineId, request));
        return SectionResponse.from(section);
    }

    private Section createEntity(Long lineId, CreateSectionRequest request) {
        return Section.builder().line(lineRepository.getReferenceById(lineId))
                .upStation(stationRepository.getReferenceById(request.getUpStationId()))
                .downStation(stationRepository.getReferenceById(request.getDownStationId()))
                .distance(request.getDistance()).build();
    }

}
