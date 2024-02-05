package subway.application.line;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.application.line.section.SectionRequest;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.application.station.StationResponse;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;


    @Transactional
    public LineResponse createLine(LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId())
            .orElseThrow(() -> new IllegalArgumentException("지하철을 찾을 수 없습니다."));
        Station downStation = stationRepository.findById(request.getDownStationId())
            .orElseThrow(() -> new IllegalArgumentException("지하철을 찾을 수 없습니다."));
        Line createdLine = new Line(request.getName(), request.getColor(), request.getDistance(),
            upStation, downStation);
        lineRepository.save(createdLine);
        return new LineResponse(
            createdLine.getId(),
            createdLine.getName(),
            createdLine.getColor(),
            List.of(
                new StationResponse(upStation.getId(), upStation.getName()),
                new StationResponse(downStation.getId(), downStation.getName())
            )
        );
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        Map<Long, Station> stationMap = stationRepository.findAll().stream()
            .collect(Collectors.toMap(Station::getId, station -> station));
        return lineRepository.findAll().stream().map(line -> {
            Set<Station> lineStations = getLineStations(stationMap, line);
            return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                lineStations.stream()
                    .distinct()
                    .map(station -> new StationResponse(station.getId(), station.getName()))
                    .collect(Collectors.toList())
            );
        }).collect(Collectors.toList());
    }

    private Set<Station> getLineStations(Map<Long, Station> stationMap, Line line) {
        return line.getSections().stream().flatMap(section -> {
            Station upStation = stationMap.get(section.getUpStationId());
            Station downStation = stationMap.get(section.getDownStationId());
            return Stream.of(upStation, downStation);
        }).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("노선을 찾을 수 없습니다."));
        List<StationResponse> stations = stationRepository.findAllById(line.getStationIds())
            .stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());

        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stations
        );
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void registSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new RuntimeException("노선이 존재하지 않습니다."));
        Section section = new Section(line, request.getUpStationId(), request.getDownStationId(),
            request.getDistance());
        line.registSection(section);
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new IllegalArgumentException("지하철역을 찾을 수 없습니다"));
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다"));
        line.deleteSection(station.getId());
    }
}
