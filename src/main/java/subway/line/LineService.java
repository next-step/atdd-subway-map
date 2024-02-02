package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.NotFoundException;
import subway.station.Station;
import subway.station.StationRepository;

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
        Station upStation = getStationById(lineCreateRequest.getUpStationId());
        Station downStation = getStationById(lineCreateRequest.getDownStationId());

        Line line = lineRepository.save(
                Line.builder()
                        .name(lineCreateRequest.getName())
                        .color(lineCreateRequest.getColor())
                        .upStation(upStation)
                        .downStation(downStation)
                        .distance(lineCreateRequest.getDistance())
                        .build());

        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .upStation(upStation)
                .downStation(downStation)
                .build();
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line findLine = getLineById(id);
        findLine.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        Line findLine = getLineById(id);
        lineRepository.deleteById(findLine.getId());
    }

    public List<LineResponse> findAllLine() {
        return lineRepository.findAll().stream()
                .map(line -> LineResponse.builder().id(line.getId())
                        .name(line.getName())
                        .color(line.getColor())
                        .upStation(line.getUpStation())
                        .downStation(line.getDownStation()).build())
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line findLine = getLineById(id);

        return LineResponse.builder()
                .id(findLine.getId())
                .color(findLine.getColor())
                .name(findLine.getName())
                .upStation(findLine.getUpStation())
                .downStation(findLine.getDownStation())
                .build();
    }

    private Line getLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("지하철 노선이 존재하지 않습니다."));
    }

    private Station getStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("지하철역이 존재하지 않습니다."));
    }
}
