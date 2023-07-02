package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.StationRepository;
import subway.ui.LineCreateRequest;
import subway.ui.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final LineMapper lineMapper;

    public LineService(LineRepository lineRepository, LineMapper lineMapper) {
        this.lineRepository = lineRepository;
        this.lineMapper = lineMapper;
    }

    @Transactional
    public LineResponse createLine(LineCreateRequest request) {
        Line line = lineRepository.save(Line.create(request));
        return new LineResponse(line.getId(), line.getName());
    }

    public List<LineResponse> showLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(lineMapper::toLineResponse).collect(Collectors.toList());
    }
}
