package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        final String name = request.getName();
        final String color = request.getColor();

        if (lineRepository.findByName(name).isPresent()) {
            throw new RuntimeException(String.format("이미 존재하는 노선입니다. %s : %s", name, color));
        }

        Line line = lineRepository.save(new Line(name, color));
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(long id) {
        final Line foundLine = lineRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("해당하는 노선을 찾을 수 없습니다. id : %s", id)));
        return createLineResponse(foundLine);
    }

    @Transactional
    public LineResponse editLineById(long id, @RequestBody LineRequest lineRequest) {
        Line foundLine = lineRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("해당하는 노선을 찾을 수 없습니다. id : %s", id)));
        foundLine.updateLine(lineRequest.getName(), lineRequest.getColor());
        final Line savedLine = lineRepository.save(foundLine);
        return createLineResponse(savedLine);
    }


    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
