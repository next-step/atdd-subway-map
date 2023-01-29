package subway.line.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import subway.line.business.model.Line;
import subway.line.repository.LineRepository;
import subway.line.repository.entity.LineEntity;

@Service
@AllArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    public Line create(Line line) {
        return lineRepository.save(new LineEntity(line)).toLine();
    }

}
