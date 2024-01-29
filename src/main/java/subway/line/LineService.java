package subway.line;

import javax.sound.sampled.Line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(Line.of(lineRequest));
        return createLineResponse(line);
    }
}
