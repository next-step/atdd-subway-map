package subway.line.delete;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.LineRepository;
import subway.section.SectionRepository;

@Transactional
@Service
public class LineDeleteService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineDeleteService(LineRepository lineRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public void deleteLine(Long lineId) {
        validateExistLine(lineId);
        sectionRepository.deleteAllByLineId(lineId);
        lineRepository.deleteById(lineId);
    }

    private void validateExistLine(Long lineId) {
        if (!lineRepository.existsById(lineId)) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다. lineId: " + lineId);
        }
    }
}
