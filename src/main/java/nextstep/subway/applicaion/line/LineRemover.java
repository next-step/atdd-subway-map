package nextstep.subway.applicaion.line;

import nextstep.subway.domain.line.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineRemover {

    private final LineRepository lineRepository;

    public LineRemover(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public void remove(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
