package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.line.Line;
import subway.station.domain.line.LineRepository;
import subway.station.domain.section.Section;
import subway.station.domain.station.Station;
import subway.station.domain.station.StationRepository;
import subway.station.global.error.exception.ErrorCode;
import subway.station.global.error.exception.InvalidValueException;
import subway.station.service.dto.LineResponse;
import subway.station.service.dto.LineRequest;

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
    public LineResponse save(LineRequest lineRequest) {
        Line line = saveLine(lineRequest);
        addSection(line.getId(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());
        return from(line);
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAllLinesWithSectionsAndStations();
        return from(lines);
    }

    public LineResponse findById(Long id) {
        Line line = findLineById(id);
        return from(line);
    }

    @Transactional
    public LineResponse update(Long id, String name, String color) {
        Line line = findLineById(id);
        updateLine(line, name, color);
        return fromForUpdate(line);
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long id, Long upStationId, Long downStationId, Long distance) {
        Line line = findLineById(id);

        Section section = Section.builder()
                .upStation(findStationById(upStationId))
                .downStation(findStationById(downStationId))
                .distance(distance)
                .build();
        line.addSection(section);
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

    private List<LineResponse> from(List<Line> lines) {
        return lines.stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    private LineResponse from(Line line) {
        List<Station> stations = convertToStations(line.getSections());
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stations)
                .build();
    }

    private LineResponse fromForUpdate(Line line) {
        return LineResponse.builder()
                .name(line.getName())
                .color(line.getColor())
                .build();
    }

    private List<Station> convertToStations(List<Section> orderedSections) {
        List<Station> stations = orderedSections.stream().map(Section::getUpStation).collect(Collectors.toList());
        stations.add(orderedSections.get(orderedSections.size() - 1).getDownStation());
        return stations;
    }
}
