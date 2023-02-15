package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.line.Line;
import subway.station.domain.line.LineRepository;
import subway.station.domain.station.Station;
import subway.station.domain.station.StationRepository;
import subway.station.global.error.exception.ErrorCode;
import subway.station.global.error.exception.InvalidValueException;
import subway.station.service.dto.LineRequest;
import subway.station.service.dto.LineResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse save(LineRequest lineRequest) {
        Line line = saveLine(lineRequest);
        addSection(line.getId(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());
        return LineResponse.from(line);
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAllLinesWithSectionsAndStations();
        return LineResponse.from(lines);
    }

    public LineResponse findById(Long id) {
        Line line = findLineById(id);
        return LineResponse.from(line);
    }

    @Transactional
    public LineResponse update(Long id, String name, String color) {
        Line line = findLineById(id);
        line.update(name, color);
        return LineResponse.fromForUpdate(line);
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = findStationById(stationId);
        line.deleteSection(station);
    }

    public void addSection(Long id, Long upStationId, Long downStationId, Long distance) {
        Line line = findLineById(id);
        line.addSection(findStationById(upStationId), findStationById(downStationId), distance);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new InvalidValueException(ErrorCode.NOT_EXISTS_STATION));
    }

    private Line saveLine(LineRequest lineRequest) {
        return lineRepository.save(
                Line.builder()
                        .name(lineRequest.getName())
                        .color(lineRequest.getColor())
                        .build());
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findLineWithSectionsAndStationsById(lineId);
    }
}
