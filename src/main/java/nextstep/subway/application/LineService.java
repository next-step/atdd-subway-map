package nextstep.subway.application;

import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.domain.*;
import nextstep.subway.domain.exception.LineException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final SectionService sectionService;

    public LineService(LineRepository lineRepository, SectionService sectionService) {
        this.lineRepository = lineRepository;
        this.sectionService = sectionService;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = new Line(request.getName(), request.getColor());

        if (lineRepository.exists(Example.of(line))) {
            throw new LineException.Duplicated(line);
        }

        Line created = lineRepository.save(line);

        if (request.hasSectionInformation()) {
            sectionService.saveSection(created.getId(), new SectionRequest(request.getUpStationId(), request.getDownStationId(), request.getDistance()));
        }

        return LineResponse.fromLine(created);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::fromLine)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new LineException.NotFound(id));
        return LineResponse.fromLine(line);
    }

    public void updateLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new LineException.NotFound(id));
        line.changeName(lineRequest.getName());
        line.changeColor(lineRequest.getColor());
        lineRepository.save(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
