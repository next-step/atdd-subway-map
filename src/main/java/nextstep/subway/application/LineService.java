package nextstep.subway.application;

import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.domain.*;
import nextstep.subway.domain.exception.LineException;
import nextstep.subway.domain.exception.StationException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        SectionRequest sectionRequest = new SectionRequest(request.getUpStationId(), request.getDownStationId(), request.getDistance());
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new StationException.NotFound(sectionRequest.getUpStationId()));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new StationException.NotFound(sectionRequest.getDownStationId()));
        Line line = new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());

        if (lineRepository.exists(Example.of(line))) {
            throw new LineException.Duplicated(line);
        }

        Line created = lineRepository.save(line);
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
