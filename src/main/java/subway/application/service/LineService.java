package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.repository.LineRepositoryPort;
import subway.domain.LineCreateDomain;
import subway.domain.LineDomain;
import subway.domain.LineUpdateDomain;
import subway.domain.NotFoundLineException;

import java.util.List;

@Service
@Transactional
class LineService implements LineUseCase {

    private final LineRepositoryPort lineRepositoryPort;

    LineService(LineRepositoryPort lineRepositoryPort) {
        this.lineRepositoryPort = lineRepositoryPort;
    }

    @Override
    public Long createLine(LineCreateDomain lineCreateDomain) {
        return lineRepositoryPort.createLine(lineCreateDomain);
    }

    @Override
    public LineDomain loadLine(Long loadLineId) {
        return lineRepositoryPort.loadLine(loadLineId)
            .orElseThrow(() -> new NotFoundLineException(String.format("요청한 Line 을 찾지 못했습니다 requested lineId: %d", loadLineId)));
    }

    @Override
    public List<LineDomain> loadLines() {
        return lineRepositoryPort.loadLines();
    }

    @Override
    public void updateLine(LineUpdateDomain toDomain) {
        lineRepositoryPort.updateLine(toDomain);
    }

}
