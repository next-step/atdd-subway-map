package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.exception.NotFoundException;
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


    public LineResponse saveLine(LineRequest request) {
        final String name = request.getName();

        if (lineRepository.existsByName(name)) {
            throw new IllegalArgumentException(String.format("이미 존재하는 노선입니다. %s", request));
        }

        Line line = lineRepository.save(this.fromLine(request));

        return this.createLineResponse(line);
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
                () -> new NotFoundException(id));
        return createLineResponse(foundLine);
    }

    public LineResponse editLineById(long id, @RequestBody LineRequest lineRequest) {
        Line foundLine = lineRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id));
        foundLine.updateLine(lineRequest.getName(), lineRequest.getColor());
        final Line savedLine = lineRepository.save(foundLine);
        return createLineResponse(savedLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse
                .Builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .upStationId(line.getUpStationId())
                .downStationId(line.getDownStationId())
                .distance(line.getDistance())
                .createdDate(line.getCreatedDate())
                .modifiedDate(line.getModifiedDate())
                .build();
    }

    private Line fromLine(LineRequest lineRequest) {
        return new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()
        );
    }
}
