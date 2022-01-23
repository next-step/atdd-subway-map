package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineWithStationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 노선입니다.");
        }

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            line.getCreatedDate(),
            line.getModifiedDate()
        );
    }

    @Transactional(readOnly = true)
    public List<LineWithStationResponse> getLines() {
        List<Line> lines = lineRepository.findAll();

        return lines
            .stream()
            .map(line -> new LineWithStationResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate(),
                new ArrayList()
            ))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineWithStationResponse getLine(Long id) {
        Line line = getLineById(id);

        return new LineWithStationResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            line.getCreatedDate(),
            line.getModifiedDate(),
            new ArrayList()
        );
    }

    public void editLine(Long id, LineRequest lineRequest) {
        Line line = getLineById(id);

        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Line getLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("일치하는 라인이 없습니다."));
    }
}
