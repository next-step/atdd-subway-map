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

    private final String NOT_FOUND_MESSAGE = "존재하지 않는 노선 입니다.";

    private final LineRepository lineRepository;

    @Transactional
    public LineResponse saveLine(LineCreateRequest createRequest,
                                 Station upStation,
                                 Station downStation) {
        Line request = LineCreateRequest.to(createRequest, upStation, downStation);
        Line line = lineRepository.save(request);
        return LineResponse.from(line);
    }

    @Transactional
    public void updateLine(Long id, LineModifyRequest request) {
        Line line = this.findLineById(id);
        line.updateLine(request.getName(), request.getColor());
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::from)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MESSAGE));
        lineRepository.deleteById(id);
    }
}
