package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.input.LineLoadUseCase;
import subway.application.service.input.SectionCommandUseCase;
import subway.application.service.output.SectionCommandRepository;
import subway.application.service.output.SectionLoadRepository;
import subway.domain.*;

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
        Long distance = sectionCreateDto.getDistance();

        List<Section> lineSections = sectionLoadRepository.loadLineSection(line.getId());
        Section section = Section.make(line, upStation, newDownStation, distance, lineSections);
        return sectionCommandRepository.createSection(section);

    }

}
