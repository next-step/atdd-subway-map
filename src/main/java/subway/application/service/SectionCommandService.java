package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.input.LineLoadUseCase;
import subway.application.service.input.SectionCommandUseCase;
import subway.application.service.output.SectionCommandRepository;
import subway.application.service.output.SectionLoadRepository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.SectionCreateDto;
import subway.domain.Station;

import java.util.List;

@Service
@Transactional
public class SectionCommandService implements SectionCommandUseCase {

    private final SectionCommandRepository sectionCommandRepository;
    private final SectionLoadRepository sectionLoadRepository;
    private final LineLoadUseCase lineLoadUseCase;
    private final StationService stationService;

    public SectionCommandService(SectionCommandRepository sectionCommandRepository, SectionLoadRepository sectionLoadRepository, LineLoadUseCase lineLoadUseCase, StationService stationService) {
        this.sectionCommandRepository = sectionCommandRepository;
        this.sectionLoadRepository = sectionLoadRepository;
        this.lineLoadUseCase = lineLoadUseCase;
        this.stationService = stationService;
    }

    @Override
    public Long createSection(SectionCreateDto sectionCreateDto) {
        Line line = lineLoadUseCase.loadLine(sectionCreateDto.getLineId());

        Station upStation = stationService.findStation(sectionCreateDto.getUpStationId());
        Station newDownStation = stationService.findStation(sectionCreateDto.getDownStationId());

        List<Section> lineSections = sectionLoadRepository.loadLineSection(line.getId());

        if (lineSections.isEmpty()) {
            return createSection(sectionCreateDto, line, upStation, newDownStation);
        }

        Section lineDownSection = lineSections.get(lineSections.size() - 1);
        Station lineDownStation = lineDownSection.getDownStation();

        if (!lineDownStation.equals(upStation)) {
            throw new NotEqualUpStationAndDownStationException();
        }

        return createSection(sectionCreateDto, line, upStation, newDownStation);
    }

    private Long createSection(SectionCreateDto sectionCreateDto, Line line, Station upStation, Station downStation) {
        Section section = Section.withNoId(downStation, upStation, sectionCreateDto.getDistance(), line);
        return sectionCommandRepository.createSection(section);
    }

}
