package subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.Station;
import subway.StationRepository;
import subway.line.application.dto.LineRequest;
import subway.line.application.dto.LineResponse;
import subway.line.application.dto.SectionRequest;
import subway.line.domain.Line;
import subway.line.domain.LineQueryService;
import subway.line.domain.LineRepository;
import subway.line.domain.Section;

@Service
@Transactional(readOnly = true)
public class DefaultLineQueryService implements LineQueryService {
    public static final String LINE_NOT_FOUND_MESSAGE = "노선을 찾을 수 없습니다.";

    private final LineRepository lineRepository;

    public DefaultLineQueryService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(LineResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public LineResponse findLineById(Long id) {
        Line line = findLineOrElseThrow(id);
        return LineResponse.from(line);
    }

    private Line findLineOrElseThrow(Long lineId) {
        return lineRepository.findById(lineId)
            .orElseThrow(() -> new IllegalArgumentException(LINE_NOT_FOUND_MESSAGE));
    }
}