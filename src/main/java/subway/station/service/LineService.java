package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.line.Line;
import subway.station.domain.line.LineRepository;
import subway.station.domain.station.Station;
import subway.station.domain.station.StationRepository;
import subway.station.global.error.exception.ErrorCode;
import subway.station.global.error.exception.InvalidValueException;
import subway.station.service.dto.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public LineSaveResponse save(LineSaveRequest lineSaveRequest) {
        Line line = saveLine(lineSaveRequest);
        addSection(line.getId(), lineSaveRequest.getUpStationId(), lineSaveRequest.getDownStationId(), lineSaveRequest.getDistance());
        return toDtoForSaveResponse(line);
    }

    public List<LineFindAllResponse> findAll() {
        List<Line> lines = lineRepository.findAll();

        return toDtoForFindAllResponse(lines);
    }

    public LineFindByLineResponse findById(Long id) {
        Line line = findLineById(id);

        return toDtoForFindByResponse(line);
    }

    @Transactional
    public LineUpdateResponse update(Long id, String name, String color) {
        Line line = findLineById(id);

        updateLine(line, name, color);

        return toDtoForUpdateResponse(line);
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long id, Long upStationId, Long downStationId, Long distance) {
        Line line = findLineById(id);
        Station upStation = findStationById(upStationId);
        Station downStation = findStationById(downStationId);
        line.addSection(upStation, downStation, distance);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = findStationById(stationId);
        line.deleteSection(station);
    }

    private void updateLine(Line line, String name, String color) {
        if (!name.isEmpty()) {
            line.changeName(name);
        }
        if (!color.isEmpty()) {
            line.changeColor(color);
        }
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new InvalidValueException(ErrorCode.NOT_EXISTS_STATION));
    }

    private Line saveLine(LineSaveRequest lineSaveRequest) {
        return lineRepository.save(
                Line.builder()
                        .name(lineSaveRequest.getName())
                        .color(lineSaveRequest.getColor())
                        .build());
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new InvalidValueException(ErrorCode.STATION_NOT_EXISTS_IN_LINE));
    }

    private List<LineFindAllResponse> toDtoForFindAllResponse(List<Line> lines) {
        return lines.stream()
                .map(this::toDtoFoFindAllResponse)
                .collect(Collectors.toList());
    }

    private LineSaveResponse toDtoForSaveResponse(Line line) {
        return LineSaveResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(line.getStations())
                .build();
    }

    private LineFindAllResponse toDtoFoFindAllResponse(Line line) {
        return LineFindAllResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(line.getStations())
                .build();
    }

    private LineFindByLineResponse toDtoForFindByResponse(Line line) {
        return LineFindByLineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(line.getStations())
                .build();
    }

    private LineUpdateResponse toDtoForUpdateResponse(Line line) {
        return LineUpdateResponse.builder()
                .name(line.getName())
                .color(line.getColor())
                .build();
    }
}
