package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Stations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(Line.of(request));
        return LineResponse.of(line);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public Optional<LineResponse> getLine(long lineId) {
        Optional<Line> lineOptional = lineRepository.findById(lineId);
        if (lineOptional.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(LineResponse.of(lineOptional.get()));
    }

    public void updateLine(long lineId, LineRequest lineRequest) {
        lineRepository.save(Line.of(lineId, lineRequest));
    }

    public void deleteLine(long lineId) {
        lineRepository.deleteById(lineId);
    }
}
