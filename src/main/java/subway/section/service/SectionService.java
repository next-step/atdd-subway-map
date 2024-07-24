package subway.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.entity.Line;
import subway.line.service.LineService;
import subway.section.dto.SectionRequest;
import subway.section.dto.SectionResponse;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.section.exception.SectionException;
import subway.section.repository.SectionRepository;
import subway.station.entity.Station;
import subway.station.service.StationService;

import static subway.common.constant.ErrorCode.SECTION_NOT_FOUND;
import static subway.converter.SectionConverter.convertToSectionResponseByLineAndSection;
import static subway.section.validator.SectionValidator.validateCreateSection;
import static subway.section.validator.SectionValidator.validateDeleteSection;

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

    @Transactional
    public SectionResponse createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineService.getLineByIdOrThrow(lineId);
        Sections sections = line.getSections();

        Station upStation = stationService.getStationByIdOrThrow(sectionRequest.getUpStationId());
        Station downStation = stationService.getStationByIdOrThrow(sectionRequest.getDownStationId());
        Section section = Section.of(upStation, downStation, sectionRequest.getDistance(), sections.getCurrentSectionsPosition() + 1);

        validateCreateSection(line, upStation, downStation);

        sections.addSection(section);
        lineService.saveLine(line);

        return convertToSectionResponseByLineAndSection(line, section);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Section section = getByDownStationId(stationId);

        Line line = lineService.getLineByIdOrThrow(lineId);
        Sections sections = line.getSections();

        validateDeleteSection(sections, stationId);

        sections.removeSection(section);

        deleteSection(section);
        lineService.saveLine(line);
    }

    public Section getByDownStationId(Long stationId) {
        return sectionRepository.findByDownStationId(stationId).orElseThrow(
                () -> new SectionException(String.valueOf(SECTION_NOT_FOUND))
        );
    }

    public void deleteSection(Section section) {
        sectionRepository.delete(section);
    }

}
