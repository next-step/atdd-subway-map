package nextstep.subway.domain.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.infra.LineRepository;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;

    public Line findLine(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("line is not found"));
    }

    public Line save(final Line line) {
        return lineRepository.save(line);
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public void delete(final Line line) {
        lineRepository.delete(line);
    }
}
