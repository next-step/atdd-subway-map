package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SectionResponse;
import subway.dto.SectionsResponse;
import subway.dto.StationResponse;
import subway.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final LineService lineService;

    public SectionService(SectionRepository sectionRepository, LineService lineService) {
        this.sectionRepository = sectionRepository;
        this.lineService = lineService;
    }

    public SectionsResponse findAllSections(Long lineId) {
        List<SectionResponse> sections = sectionRepository.findByLineId(lineId).stream()
                .map(this::createSectionResponse)
                .collect(Collectors.toList());
        return new SectionsResponse(lineId, sections);
    }

    private SectionResponse createSectionResponse(Section section) {
        Station downStation = section.getDownStation();
        Station upStation = section.getUpStation();
        return new SectionResponse(
                section.getId(),
                new StationResponse(downStation.getId(), downStation.getName()),
                new StationResponse(upStation.getId(), upStation.getName()));
    }
}
