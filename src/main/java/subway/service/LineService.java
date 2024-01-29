package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.LineUpdateRequest;
import subway.domain.Line;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {
        Line line = lineRepository.save(new Line(
                lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                lineCreateRequest.getUpStationId(),
                lineCreateRequest.getDownStationId(),
                lineCreateRequest.getDistance()
        ));
        return LineResponse.of(line, findStationsBy(line.stationIds()));
    }

    public List<LineResponse> findLines() {
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stations(lines);
        return LineResponse.listOf(lines, stations);
    }

    private List<Station> stations(List<Line> lines) {
        List<Long> stationIds = lines.stream()
                .map(Line::stationIds)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return findStationsBy(stationIds);
    }

    private List<Station> findStationsBy(List<Long> ids) {
        return stationRepository.findAll().stream()
                .filter(station -> ids.contains(station.getId()))
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = findBy(id);
        List<Station> stations = findStationsBy(line.stationIds());
        return LineResponse.of(line, stations);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = findBy(id);
        line.update(request.getName(), request.getColor());
    }

    private Line findBy(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
