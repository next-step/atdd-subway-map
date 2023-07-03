package subway.line.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.controller.dto.LineRequest;
import subway.line.controller.dto.LineResponse;
import subway.line.domain.Line;
import subway.line.infra.LineRepository;
import subway.station.controller.dto.StationResponse;
import subway.station.domain.Station;
import subway.station.domain.StationList;
import subway.station.service.StationService;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationService stationService;
    private final LineRepository lineRepository;

    public LineService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Line savedLine = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));

        List<Long> stationIds = Arrays.asList(lineRequest.getUpStationId(), lineRequest.getDownStationId());
        StationList stations = stationService.findStationsByIdList(stationIds);
        stations.updateLine(savedLine);

        return createLineResponse(savedLine, stations);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return createLineResponseList(lines);
    }

    private List<LineResponse> createLineResponseList(List<Line> lines) {
        return lines
                .stream()
                .map(line -> {
                    List<StationResponse> stationResponses = getStationResponse(line.getStations());
                    return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
                }).collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line, StationList stationList) {
        List<StationResponse> stationResponses = getStationResponse(stationList);
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    private List<StationResponse> getStationResponse(StationList stationList) {
        List<Station> stations = stationList.getStations();
        return getStationResponse(stations);
    }

    private List<StationResponse> getStationResponse(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
