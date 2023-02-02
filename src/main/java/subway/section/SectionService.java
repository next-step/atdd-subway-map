package subway.section;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineService;
import subway.station.StationResponse;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final StationService stationService;
    private final LineService lineService;

    public SectionService(SectionRepository sectionRepository, StationService stationService, LineService lineService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public List<SectionResponse> findAllSection(Long lineId) {
        Line line = lineService.findLineById(lineId);
        return findAllSection(line);
    }

    public List<SectionResponse> findAllSection(Line line) {
        return sectionRepository.findAllByLine(line).stream()
                .map(this::createSectionResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SectionResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineService.findLineById(lineId);
        Section section = sectionRepository.save(sectionRequest.toEntity(line));
        return createSectionResponse(section);
    }

    @Transactional
    public void deleteLineOfSection(Long lineId, Long stationId) {
        Line line = lineService.findLineById(lineId);
        line.deleteSection(stationId);
    }

    @Transactional
    public void deleteAllLineOfSection(Long lineId) {
        Line line = lineService.findLineById(lineId);
        sectionRepository.deleteByLine(line);
    }

    private SectionResponse createSectionResponse(Section section) {
        List<StationResponse> stations = section.getStationIds()
                .stream()
                .map(stationService::findStationById)
                .collect(Collectors.toList());
        return SectionResponse.toResponse(section, stations);
    }
}
