package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(line -> LineResponse.of(line)).collect(Collectors.toList());
    }

    public LineResponse findLine(final Long lineId) {
        Optional<Line> line = lineRepository.findById(lineId);
        return line.map(LineResponse::of)
                .orElseThrow(() -> new IllegalArgumentException("Not found lineId"+lineId));
    }

    public void updateLine(final Long lineId, LineRequest lineRequest){
        Line findLine = lineRepository.getOne(lineId);
        findLine.update(lineRequest.toLine());
    }

    public void deleteLine(final Long lineId) {
        Optional<Line> line = lineRepository.findById(lineId);
        if(line.isPresent()){
            lineRepository.delete(line.get());
        }
    }
}
