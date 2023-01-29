package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.out.LineRepositoryPort;
import subway.domain.LineDomain;
import subway.domain.NotFoundLineException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineLoadService implements LineLoadUseCase {

    private final LineRepositoryPort lineRepositoryPort;

    public LineLoadService(LineRepositoryPort lineRepositoryPort) {
        this.lineRepositoryPort = lineRepositoryPort;
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

}
