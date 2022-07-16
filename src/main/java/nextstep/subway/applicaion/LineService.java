package nextstep.subway.applicaion;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.common.ErrorMessage;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class LineService {

  private final LineRepository lineRepository;
  private final StationRepository stationRepository;

  public LineResponse createLine(LineRequest lineRequest) {
    Station upStation = findStation(lineRequest.getUpStationId());
    Station downStation = findStation(lineRequest.getDownStationId());

    Line line = lineRepository.save(LineRequest.createLine(lineRequest, upStation, downStation));

    return LineResponse.createResponse(line);
  }

  private Station findStation(Long id) {
    return stationRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.STATION_NOT_FOUND));
  }

  public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
    Line line = findLine(id);

    line.changeName(lineUpdateRequest.getName());
    line.changeColor(lineUpdateRequest.getColor());
  }

  private Line findLine(Long id) {
    return lineRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LINE_NOT_FOUND));
  }

  public void deleteLine(Long id) {
    lineRepository.deleteById(id);
  }
}
