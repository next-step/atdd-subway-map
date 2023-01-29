package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.out.LineRepositoryPort;
import subway.application.service.in.LineCommandUseCase;
import subway.domain.LineCreateDomain;
import subway.domain.LineUpdateDomain;

@Service
@Transactional
class LineCommandService implements LineCommandUseCase {

    private final LineRepositoryPort lineRepositoryPort;

    LineCommandService(LineRepositoryPort lineRepositoryPort) {
        this.lineRepositoryPort = lineRepositoryPort;
    }

    @Override
    public Long createLine(LineCreateDomain lineCreateDomain) {
        return lineRepositoryPort.createLine(lineCreateDomain);
    }

    @Override
    public void updateLine(LineUpdateDomain toDomain) {
        lineRepositoryPort.updateLine(toDomain);
    }

    @Override
    public void deleteLine(Long lineId) {
        lineRepositoryPort.deleteLine(lineId);
    }

}
