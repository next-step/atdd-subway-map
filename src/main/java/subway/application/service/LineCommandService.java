package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.input.LineCommandUseCase;
import subway.application.service.output.LineCommandRepository;
import subway.domain.LineCreateDto;
import subway.domain.LineUpdateDto;

@Service
@Transactional
class LineCommandService implements LineCommandUseCase {

    private final LineCommandRepository lineCommandRepository;

    LineCommandService(LineCommandRepository lineCommandRepository) {
        this.lineCommandRepository = lineCommandRepository;
    }

    @Override
    public Long createLine(LineCreateDto lineCreateDto) {
        return lineCommandRepository.createLine(lineCreateDto);
    }

    @Override
    public void updateLine(LineUpdateDto toDomain) {
        lineCommandRepository.updateLine(toDomain);
    }

    @Override
    public void deleteLine(Long lineId) {
        lineCommandRepository.deleteLine(lineId);
    }

}
