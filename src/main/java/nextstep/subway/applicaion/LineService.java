package nextstep.subway.applicaion;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.applicaion.dto.LineCreate;
import nextstep.subway.applicaion.dto.LineCreate.Request;
import nextstep.subway.applicaion.dto.LineCreate.Response;
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
  public Response createLine(Request request) {
    Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(
        () -> new EntityNotFoundException("station not found"));

    Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(
        () -> new EntityNotFoundException("station not found"));

    Line line = lineRepository.save(Request.createLine(request, upStation, downStation));

    return Response.createResponse(line);
  }

  public List<Response> getAllLine() {
    return lineRepository.findAll().stream().map(Response::createResponse).collect(toList());
  }

  public Response getLine(Long id) {
    Line line = lineRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("line not found")
    );
    return Response.createResponse(line);
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
