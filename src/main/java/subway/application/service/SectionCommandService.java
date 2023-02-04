package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.input.LineLoadUseCase;
import subway.application.service.input.SectionCommandUseCase;
import subway.application.service.output.SectionCommandRepository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.SectionCreateDto;
import subway.domain.Station;

@Service
@Transactional
public class SectionCommandService implements SectionCommandUseCase {

    private final SectionCommandRepository sectionCommandRepository;
    private final LineLoadUseCase lineLoadUseCase;
    private final StationService stationService;

    public SectionCommandService(SectionCommandRepository sectionCommandRepository, LineLoadUseCase lineLoadUseCase, StationService stationService) {
        this.sectionCommandRepository = sectionCommandRepository;
        this.lineLoadUseCase = lineLoadUseCase;
        this.stationService = stationService;
    }

    @Override
    public Long createSection(SectionCreateDto sectionCreateDto) {
        Line line = lineLoadUseCase.loadLine(sectionCreateDto.getLineId());
        Station upStation = stationService.findStation(sectionCreateDto.getUpStationId());
        Station downStation = stationService.findStation(sectionCreateDto.getDownStationId());

        Section section = Section.withNoId(downStation, upStation, sectionCreateDto.getDistance(), line);
        return sectionCommandRepository.createSection(section);
    }

}
