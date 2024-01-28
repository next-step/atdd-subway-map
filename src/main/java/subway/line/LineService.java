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
        final Station upStation = this.findStationById(request.getUpStationId());
        final Station downStation = this.findStationById(request.getDownStationId());
        final Integer lineDistance = request.getDistance();

        final Line newLine = new Line(lineName, lineColor, upStation, downStation, lineDistance);
        final Line savedLine = lineRepository.save(newLine);

        return LineResponse.convertToDto(savedLine);
    }

    public LineResponse getSubwayLine(final Long lindId) {
        final Line line = this.findLineById(lindId);

        return LineResponse.convertToDto(line);
    }

    public List<LineResponse> getSubwayLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineResponse.convertToDto(line))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateSubwayLine(final Long id, final LineUpdateRequest request) {
        final Line line = this.findLineById(id);

        final String name = request.getName();
        final String color = request.getColor();
        line.updateDetails(name, color);
    }

    private Station findStationById(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("Station not found. Station Id: " + stationId));
    }

    private Line findLineById(final Long lindId) {
        return lineRepository.findById(lindId)
                .orElseThrow(() -> new EntityNotFoundException("Line not found. Line Id: " + lindId));
    }
}
