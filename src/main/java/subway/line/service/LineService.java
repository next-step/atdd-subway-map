package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.exception.LineNotFoundException;
import subway.line.model.Line;
import subway.line.repository.LineRepository;
import subway.station.dto.StationResponse;
import subway.station.exception.StationNotFoundException;
import subway.station.model.Station;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse createLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId())
                .orElseThrow(StationNotFoundException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
                .orElseThrow(StationNotFoundException::new);
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation));
        return createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> showLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse showLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        return createLineResponse(line);
    }

    @Transactional
    public void modifyLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        line.modifyLine(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(createStationResponse(line.getUpStation()), createStationResponse(line.getDownStation()))
        );
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

}
