package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineModifyRequest;
import subway.line.dto.LineSaveRequest;
import subway.line.dto.LineResponse;
import subway.line.domain.Line;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineCommandRepository;
import subway.line.repository.LineQueryRepository;
import subway.station.domain.Station;
import subway.station.repository.StationQueryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineCommandRepository lineCommand;
    private final LineQueryRepository lineQuery;
    private final StationQueryRepository stationQuery;

    @Transactional
    public LineResponse saveLine(LineSaveRequest saveRequest) {
        Optional<Station> downStation = stationQuery.findById(saveRequest.getDownStationId());
        Optional<Station> upStation = stationQuery.findById(saveRequest.getUpStationId());
        Line line = lineCommand.save(
                Line.builder()
                        .name(saveRequest.getName())
                        .color(saveRequest.getColor())
                        .distance(saveRequest.getDistance())
                        .downStation(downStation.get())
                        .upStation(upStation.get())
                        .build()
        );
        return createLineResponse(line);
    }

    @Transactional
    public void modifyLine(Long id, LineModifyRequest modifyRequest) {
        Line line = lineQuery.findById(id).orElseThrow(LineNotFoundException::new);
        line.modify(modifyRequest);
    }

    public List<LineResponse> findAllLines() {
        return lineQuery.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        return LineResponse.fromDomain(
                lineQuery.findById(id).orElseThrow(LineNotFoundException::new)
        );
    }

    public void removeLineById(Long id) {
        lineCommand.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return LineResponse.fromDomain(line);
    }

}