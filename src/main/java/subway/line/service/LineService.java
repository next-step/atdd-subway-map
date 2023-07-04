package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineModifyRequest;
import subway.line.dto.LineResponse;
import subway.line.model.Line;
import subway.line.repository.LineRepository;
import subway.station.model.Station;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    public static final String NOT_FOUND_LINE = "노선이 존재하지 않습니다.";
    private final LineRepository lineRepository;

    @Transactional
    public LineResponse saveLine(LineCreateRequest request, Station upStation, Station downStation) {
        Line line = Line.from(request, upStation, downStation);
        lineRepository.save(line);
        return LineResponse.from(line);
    }

    @Transactional
    public void updateLine(Long id, LineModifyRequest request) {
        Line line = this.findLineById(id);
        line.setName(request.getName());
        line.setColor(request.getColor());
    }

    public List<LineResponse> findAllLines() {
        List<LineResponse> collect = lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
        return collect;
    }

    public LineResponse findLineResponseById(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::from)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_LINE));
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_LINE));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
