package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Station;
import subway.StationRepository;
import subway.StationResponse;

@Service
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse createLine(LineRequest request) {
        Line createdLine = new Line(request.getName(), request.getColor(), request.getDistance());
        lineRepository.save(createdLine);
        // 상행선, 하행선 생성
        Station upStation = new Station(request.getUpStationId(),
            newStationName(request.getUpStationId()),
            createdLine.getId());
        stationRepository.save(upStation);
        Station downStation = new Station(request.getDownStationId(),
            newStationName(request.getDownStationId()),
            createdLine.getId());
        stationRepository.save(downStation);
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

    private String newStationName(Long id) {
        return "지하철역" + id;
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream().map(line -> new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stationRepository.findAllByLineId(line.getId()).stream()
                .map(station -> new StationResponse(
                    station.getId(),
                    station.getName()
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<StationResponse> stations = stationRepository.findAllByLineId(id).stream()
            .map(station -> new StationResponse(
                station.getId(),
                station.getName()
            )).collect(Collectors.toList());
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
}
