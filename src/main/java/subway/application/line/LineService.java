package subway.application.line;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.section.SectionRequest;
import subway.application.station.StationResponse;
import subway.domain.DomainException.LineException;
import subway.domain.DomainException.StationException;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.StationRepository;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;


    @Transactional
    public LineResponse createLine(LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId())
            .orElseThrow(LineException::NotFoundException);
        Station downStation = stationRepository.findById(request.getDownStationId())
            .orElseThrow(LineException::NotFoundException);
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
            .orElseThrow(LineException::NotFoundException);
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
            .orElseThrow(LineException::NotFoundException);
        Section section = new Section(line, request.getUpStationId(), request.getDownStationId(),
            request.getDistance());
        line.registSection(section);
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Station station = stationRepository.findById(stationId)
            .orElseThrow(StationException::NotFoundException);
        Line line = lineRepository.findById(id)
            .orElseThrow(LineException::NotFoundException);
        line.deleteSection(station.getId());
    }
}
