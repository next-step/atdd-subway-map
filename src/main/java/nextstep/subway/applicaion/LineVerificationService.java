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

    public boolean isExistByName(String name) {
        return lineRepository.existsByName(name);
    }

    public boolean isExistById(Long id) {
        return lineRepository.existsById(id);
    }
}
