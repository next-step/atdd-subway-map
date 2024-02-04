package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import javax.persistence.EntityNotFoundException;
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
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(lineRequest.toEntity());
        Station upStation = stationRepository.findById(lineRequest.getUpStationId())
                .orElseThrow(() -> new EntityNotFoundException("해당 역을 상행종점역으로 등록할 수 없습니다."));
        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
                .orElseThrow(() -> new EntityNotFoundException("해당 역을 하행종점역으로 등록할 수 없습니다."));

        return new LineResponse(line, List.of(upStation, downStation));
    }

    public List<LineResponse> findAllLines() {
        List<Station> stations = stationRepository.findAll();
        return lineRepository.findAll().stream()
                .map(line -> new LineResponse(
                                line,
                                stations.stream()
                                        .filter(station -> line.stationIds().contains(station.getId()))
                                        .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        Line line = lineRepository.getReferenceById(id);
        List<Station> stations = stationRepository.findAll().stream()
                .filter(station -> line.stationIds().contains(station.getId()))
                .collect(Collectors.toList());
        return new LineResponse(line, stations);
    }

    @Transactional
    public void updateLine(Long id, UpdateLineRequest updateLineRequest) {
        Line line = lineRepository.getReferenceById(id);
        line.update(updateLineRequest.getName(), updateLineRequest.getColor());
    }

    @Transactional
    public void deleteStationById(Long id) {
        lineRepository.deleteById(id);
    }
}
