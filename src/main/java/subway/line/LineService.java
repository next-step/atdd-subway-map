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
    public LineResponse createSubwayLine(final LineRequest request) {
        final String lineName = request.getName();
        final String lineColor = request.getColor();
        final Station upStation = this.getStation(request.getUpStationId());
        final Station downStation = this.getStation(request.getDownStationId());
        final Integer lineDistance = request.getDistance();

        final Line newLine = new Line(lineName, lineColor, upStation, downStation, lineDistance);
        final Line savedLine = lineRepository.save(newLine);

        return LineResponse.convertToDto(savedLine);
    }

    public List<LineResponse> getSubwayLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineResponse.convertToDto(line))
                .collect(Collectors.toList());
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("Station not found. Station Id: " + stationId));
    }
}
