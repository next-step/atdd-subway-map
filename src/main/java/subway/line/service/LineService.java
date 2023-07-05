package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.constants.LineConstant;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineModifyRequest;
import subway.line.dto.LineResponse;
import subway.line.model.Line;
import subway.line.model.LineStation;
import subway.line.repository.LineRepository;
import subway.line.repository.LineStationRepository;
import subway.station.model.Station;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    private final LineStationRepository lineStationRepository;

    @Transactional
    public LineResponse saveLine(LineCreateRequest createRequest, Station upStation, Station downStation) {
        Line request = Line.from(createRequest, upStation, downStation);
        Line line = lineRepository.save(request);
        LineStation upLineStation = lineStationRepository.save(generateLineStation(line, upStation));
        LineStation downLineStation = lineStationRepository.save(generateLineStation(line, downStation));
        line.addLineStation(upLineStation);
        line.addLineStation(downLineStation);
        return LineResponse.from(line);
    }

    private LineStation generateLineStation (Line line, Station station) {
        return LineStation.builder()
                .station(station)
                .line(line)
                .build();
    }

    @Transactional
    public void updateLine(Long id, LineModifyRequest request) {
        Line line = this.findLineById(id);
        line.updateLine(request.getName(), request.getColor());
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::from)
                .orElseThrow(() -> new IllegalArgumentException(LineConstant.NOT_FOUND));
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(LineConstant.NOT_FOUND));
    }

    @Transactional
    public void deleteLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(LineConstant.NOT_FOUND));
        lineStationRepository.deleteByLine(line);
        lineRepository.deleteById(id);
    }
}
