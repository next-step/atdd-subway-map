package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exceptions.ExistingLineException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.findFirstByName(request.getName()) != null) {
            throw new ExistingLineException();
        }
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                    .stream()
                    .map(LineResponse::of)
                    .collect(Collectors.toList());
    }

    public LineResponse findLine(long id) {
        return lineRepository.findById(id)
                    .map(LineResponse::of)
                    .orElseThrow(NullPointerException::new);
    }
    @Transactional
    public LineResponse updateLine(LineRequest request, long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(NullPointerException::new);
        line.update(request.toLine());
        return LineResponse.of(line);
    }

    public void removeLine(long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(NullPointerException::new);

        lineRepository.delete(line);
    }

}
