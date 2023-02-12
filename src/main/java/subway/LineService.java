package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(Line.from(lineRequest));
        return LineResponse.from(line);

    }

    public LineResponse findById(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::from)
                .orElseThrow(() -> new NoSuchElementException("no line for id"));
    }
}
