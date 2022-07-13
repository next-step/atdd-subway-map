package nextstep.subway.applicaion;

import static java.util.stream.Collectors.toList;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public LineResponse createLine(LineRequest lineRequest) {
    Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(
        () -> new EntityNotFoundException("station not found"));

    Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(
        () -> new EntityNotFoundException("station not found"));

    Line line = lineRepository.save(LineRequest.createLine(lineRequest, upStation, downStation));

    return LineResponse.createResponse(line);
  }

  public List<LineResponse> getAllLine() {
    return lineRepository.findAll().stream().map(LineResponse::createResponse).collect(toList());
  }

  public LineResponse getLine(Long id) {
    Line line = lineRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("line not found")
    );
    return LineResponse.createResponse(line);
  }

  @Transactional
  public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
    Line line = lineRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("line not found")
    );

    line.changeName(lineUpdateRequest.getName());
    line.changeColor(lineUpdateRequest.getColor());
  }

  @Transactional
  public void deleteLine(Long id) {
    lineRepository.deleteById(id);
  }
}
