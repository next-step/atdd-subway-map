package subway.line.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.controller.dto.LineRequest;
import subway.line.controller.dto.LineResponse;
import subway.line.domain.Line;
import subway.line.domain.LineStationConnection;
import subway.line.infra.LineRepository;
import subway.line.infra.LineStationRepository;
import subway.station.controller.dto.StationResponse;
import subway.station.domain.Station;
import subway.station.domain.StationList;
import subway.station.service.StationService;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationService stationService;
    private final LineRepository lineRepository;
    private final LineStationRepository lineStationRepository;

    public LineService(StationService stationService, LineRepository lineRepository,
            LineStationRepository lineStationRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
        this.lineStationRepository = lineStationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Line savedLine = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));

        List<Long> stationIds = Arrays.asList(lineRequest.getUpStationId(), lineRequest.getDownStationId());
        StationList stations = stationService.findStationsByIdList(stationIds);
        List<LineStationConnection> connections = stations.connectLineWithStation(savedLine);
        lineStationRepository.saveAll(connections);

        return createLineResponse(savedLine, stations.getStations());
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return createLineResponseList(lines);
    }

    public LineResponse findById(Long lineId) {
        Line line = lineRepository.findById(lineId).orElseThrow();
        List<Station> stations = getStations(line);
        return createLineResponse(line, stations);
    }

    private List<LineResponse> createLineResponseList(List<Line> lines) {
        return lines
                .stream()
                .map(line -> {
                    List<Station> stations = getStations(line);
                    return new LineResponse(line.getId(), line.getName(), line.getColor(), getStationResponse(stations));
                }).collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line, List<Station> stationList) {
        List<StationResponse> stationResponses = getStationResponse(stationList);
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    private List<StationResponse> getStationResponse(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    private List<Station> getStations(Line line) {
        List<LineStationConnection> connections = lineStationRepository.findAllByLine(line);
        return connections.stream().map(LineStationConnection::getStation).collect(Collectors.toList());
    }
}
