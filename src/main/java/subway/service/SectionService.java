package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.*;
import subway.exception.IllegalSectionException;
import subway.exception.IllegalStationDeleteException;
import subway.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SectionService {
    private final SectionRepository sectionRepository;
    private final LineService lineService;
    private final StationService stationService;

    public SectionService(SectionRepository sectionRepository, LineService lineService, StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public SectionsResponse findAllSections(Long lineId) {
        List<SectionResponse> sections = sectionRepository.findByLineId(lineId).stream()
                .map(this::createSectionResponse)
                .collect(Collectors.toList());
        return new SectionsResponse(lineId, sections);
    }

    @Transactional
    public SectionResponse createSectionResponse(Section section) {
        Station downStation = section.getDownStation();
        Station upStation = section.getUpStation();
        return new SectionResponse(
                section.getId(),
                section.getDistance(),
                new StationResponse(downStation.getId(), downStation.getName()),
                new StationResponse(upStation.getId(), upStation.getName()));
    }

    @Transactional
    public SectionCreateResponse createSection(Long lineId, SectionRequest request) {
        Line line = lineService.getLineById(lineId);
        Station downStation = stationService.findById(request.getDownStationId());
        Station upStation = stationService.findById((request.getUpStationId()));
        Long distance = request.getDistance();
        validateCreateSection(line, upStation);
        Section section = sectionRepository.save(new Section(
                        line,
                        downStation,
                        upStation,
                        distance
                )
        );
        line.addSection(section);
        line.addDistance(request.getDistance());
        return new SectionCreateResponse(
                line.getId(),
                new SectionResponse(
                        section.getId(),
                        section.getDistance(),
                        new StationResponse(downStation.getId(), downStation.getName()),
                        new StationResponse(upStation.getId(), upStation.getName())
                )
        );
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineService.getLineById(lineId);
        validateDeleteDownStation(line, stationId);
        Section section = line.getSections().stream()
                .filter(s -> s.getDownStation().getId() == stationId)
                .findFirst().orElseThrow();
        line.removeSection(section);

        sectionRepository.delete(section);
    }

    private void validateCreateSection(Line line, Station upStation) {
        if (line.getDownStation() != upStation) {
            throw new IllegalSectionException();
        }
    }

    private void validateDeleteDownStation(Line line, Long stationId) {
        if (line.getDownStationId() != stationId) {
            throw new IllegalStationDeleteException();
        }
    }
}
