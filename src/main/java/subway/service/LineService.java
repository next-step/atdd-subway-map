package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.ui.dto.LineResponse;
import subway.ui.dto.LineSaveRequest;
import subway.ui.dto.LineUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철 노선이 존재하지 않습니다."));

        return createLineResponse(line);
    }

    @Transactional
    public LineResponse save(LineSaveRequest lineSaveRequest) {
        Line line = lineRepository.save(lineSaveRequest.toEntity());
        return createLineResponse(line);
    }

    @Transactional
    public Long update(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철 노선이 존재하지 않습니다."));

        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        return id;
    }

    @Transactional
    public Long deleteById(Long id) {
        lineRepository.deleteById(id);
        return id;
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line);
    }
}
