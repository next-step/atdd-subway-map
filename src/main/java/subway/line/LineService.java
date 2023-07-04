package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Station;
import subway.StationNotFoundException;
import subway.StationRepository;
import subway.StationResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final LineStationRepository lineStationRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, LineStationRepository lineStationRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.lineStationRepository = lineStationRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
      return new LineResponse(
          line.getId(),
          line.getName(),
          line.getColor(),
          mapToStations(line)
      );
    }

  private static List<StationResponse> mapToStations(final Line line) {
      return line.getLineStations()
          .stream()
          .map(LineStation::getStation)
          .map(StationResponse::new)
          .collect(Collectors.toList());
  }

  public LineResponse saveLine(LineRequest lineRequest) {
      final Line line = lineRepository.save(new Line(lineRequest.getName(),
          lineRequest.getColor(),
          lineRequest.getDistance()));

      createLineStationIfNotNull(lineRequest.getUpStationId(), line);
      createLineStationIfNotNull(lineRequest.getDownStationId(), line);

      return createLineResponse(line);
    }

  private void createLineStationIfNotNull(final Long stationId, final Line line) {
    if (stationId != null) {
      stationRepository.findById(stationId)
          .ifPresent(station -> lineStationRepository.save(new LineStation(line, station)));
    }
  }

  @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        return createLineResponse(lineRepository.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
