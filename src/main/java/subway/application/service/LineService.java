package subway.application.service;

import org.springframework.stereotype.Service;
import subway.application.repository.LineRepository;
import subway.domain.LineCreateDomain;

@Service
class LineService implements LineUseCase {

    private LineRepository lineRepository;

    @Override
    public void createLine(LineCreateDomain lineCreateDomain) {
        lineRepository.createLine(lineCreateDomain);
    }

}
