package subway.service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.LineRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.LinesResponse;
import subway.domain.EndStations;
import subway.domain.Line;
import subway.domain.Station;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse createdLineBy(LineRequest request) {
        return LineResponse.from(lineRepository.save(new Line.Builder()
            .name(request.getName())
            .color(request.getColor())
            .distance(request.getDistance())
            .stations(EndStations.of(distinctStationsGetByIdFrom(request)))
            .build()));
    }

    public LineResponse lineResponseFoundById(Long id) {
        return LineResponse.from(lineFoundById(id));
    }

    public LinesResponse allLines() {
        return LinesResponse.from(lineRepository.findAll());
    }

    @Transactional
    public LineResponse updatedLineBy(Long id, LineRequest request) {
        Line line = lineFoundById(id);

        line.updateNewInfo(request.getName(), request.getColor(), EndStations.of(renewableStationsFrom(request)), request.getDistance());

        return LineResponse.from(lineRepository.save(line));
    }

    private Set<Station> distinctStationsGetByIdFrom(LineRequest request) {
        return streamOfEndStationIdFrom(request)
            .filter(Objects::nonNull)
            .map(this::stationFoundById)
            .collect(Collectors.toSet());
    }

    private Line lineFoundById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new LineNotFoundException("일치하는 지하철 노선 정보가 존재하지 않습니다: " + id));
    }

    private Set<Station> renewableStationsFrom(LineRequest request) {
        return distinctStationsGetByIdFrom(request);
    }

    private Stream<Long> streamOfEndStationIdFrom(LineRequest request) {
        return Stream.of(request.getUpStationId(), request.getDownStationId());
    }

    private Station stationFoundById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new StationNotFoundException("일치하는 지하철 역 정보가 존재하지 않습니다: " + id));
    }

    public void deleteLineBy(Long id) {
        lineRepository.delete(lineFoundById(id));
    }
}
