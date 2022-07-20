package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
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

    Line line = lineRepository.save(LineRequest.createLine(lineRequest));
    line.addSections(Section.createSection(upStation, downStation, lineRequest.getDistance(), line));

    return LineResponse.createResponse(line);
  }

  public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
    Line line = findLine(id);
    line.changeName(lineUpdateRequest.getName());
    line.changeColor(lineUpdateRequest.getColor());
  }

  public void deleteLine(Long id) {
    lineRepository.deleteById(id);
  }

  public void createSection(Long lineId, SectionRequest sectionRequest) {
    Line line = findLine(lineId);
    line.getSections().validateAddSection(sectionRequest.getUpStationId(), sectionRequest.getDownStationId());

    Station upStation =findStation(sectionRequest.getUpStationId());
    Station downStation =findStation(sectionRequest.getDownStationId());
    line.addSections(Section.createSection(upStation, downStation, sectionRequest.getDistance(), line));
  }

  private Station findStation(Long id) {
    return stationRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.STATION_NOT_FOUND));
  }

  public void deleteSection(long lineId, long stationId) {
    Line line = findLine(lineId);
    line.getSections().validateDeleteSection(stationId);
    line.deleteLastSection();
  }

  private Line findLine(Long id) {
    return lineRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.LINE_NOT_FOUND));
  }
}
