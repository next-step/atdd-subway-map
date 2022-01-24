package nextstep.subway.applicaion;

import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineVerificationService {
    private final LineRepository lineRepository;

    public LineVerificationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public boolean isExistStationByLineName(String lineName) {
        return lineRepository.findByName(lineName).isPresent();
    }
}
