package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Station;
import subway.StationNotFoundException;
import subway.StationRepository;
import subway.StationResponse;
import subway.section.Section;
import subway.section.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
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
      return line.getSections()
          .stream()
          .map(Section::getStations)
          .flatMap(List::stream)
          .distinct()
          .map(StationResponse::new)
          .collect(Collectors.toList());
  }

  public LineResponse saveLine(LineRequest lineRequest) {
      final Line line = lineRepository.save(new Line(lineRequest.getName(),
          lineRequest.getColor(),
          lineRequest.getDistance()));

      if (lineRequest.getUpStationId() != null && lineRequest.getDownStationId() != null) {
          Section section = new Section(line, getStationById(lineRequest.getUpStationId()), getStationById(lineRequest.getDownStationId()), lineRequest.getDistance());
          line.addSection(sectionRepository.save(section));
      }

      return createLineResponse(line);
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

    private Station getStationById(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));
    }
}
