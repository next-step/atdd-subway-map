package subway.line;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        return null;
    }

    public List<LineResponse> findAllLines() {
        return null;
    }
}
