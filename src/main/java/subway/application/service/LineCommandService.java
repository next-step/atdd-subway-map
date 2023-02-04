package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.input.LineCommandUseCase;
import subway.application.service.input.LineLoadUseCase;
import subway.application.service.input.SectionCommandUseCase;
import subway.application.service.output.LineCommandRepository;
import subway.domain.Line;
import subway.domain.LineCreateDto;
import subway.domain.LineUpdateDto;
import subway.domain.SectionCreateDto;

@Service
@Transactional
class LineCommandService implements LineCommandUseCase {

    private final LineCommandRepository lineCommandRepository;
    private final LineLoadUseCase lineLoadUseCase;
    private final SectionCommandUseCase sectionCommandUseCase;

    LineCommandService(LineCommandRepository lineCommandRepository, LineLoadUseCase lineLoadUseCase, SectionCommandUseCase sectionCommandUseCase) {
        this.lineCommandRepository = lineCommandRepository;
        this.lineLoadUseCase = lineLoadUseCase;
        this.sectionCommandUseCase = sectionCommandUseCase;
    }

    @Override
    public Long createLine(LineCreateDto lineCreateDto) {
        Line line = Line.withoutId(lineCreateDto.getName(), lineCreateDto.getColor());
        Long lineId = lineCommandRepository.createLine(line);
        sectionCommandUseCase.createSection(new SectionCreateDto(lineId, lineCreateDto.getUpStationId(), lineCreateDto.getDownStationId(), lineCreateDto.getDistance()));
        return lineId;
    }

    @Override
    public void updateLine(LineUpdateDto lineUpdateDto) {
        Line line = lineLoadUseCase.loadLine(lineUpdateDto.getLineId());
        line.updateLine(lineUpdateDto.getName(), lineUpdateDto.getColor());
        lineCommandRepository.updateLine(line);
    }

    @Override
    public void deleteLine(Long lineId) {
        lineCommandRepository.deleteLine(lineId);
    }

}
