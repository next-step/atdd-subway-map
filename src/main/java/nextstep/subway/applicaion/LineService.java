package nextstep.subway.applicaion;

import java.util.Arrays;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Sections;
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
  private final SectionRepository sectionRepository;

  public LineResponse createLine(LineRequest lineRequest) {
    Station upStation = findStation(lineRequest.getUpStationId());
    Station downStation = findStation(lineRequest.getDownStationId());

    Line line = lineRepository.save(LineRequest.createLine(lineRequest));
    sectionRepository.save(getSection(upStation, downStation, line.getDistance(), line));

    return LineResponse.createResponse(line, Arrays.asList(upStation, downStation));
  }

  public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
    Line line = findLine(id);

    line.changeName(lineUpdateRequest.getName());
    line.changeColor(lineUpdateRequest.getColor());
  }

  public void deleteLine(Long id) {
    Line line = findLine(id);
    sectionRepository.deleteByLine(line);
    lineRepository.deleteById(id);
  }

  public void createSection(Long lineId, SectionRequest sectionRequest) {
    Line line = findLine(lineId);
    Sections sections = new Sections(sectionRepository.findByLineOrderByIdAsc(line));
    sections.validateAddSection(sectionRequest.getUpStationId(), sectionRequest.getDownStationId());

    Station upStation =findStation(sectionRequest.getUpStationId());
    Station downStation =findStation(sectionRequest.getDownStationId());
    sectionRepository.save(getSection(upStation, downStation, sectionRequest.getDistance(), line));

    line.addDistance(sectionRequest.getDistance());
  }

  private Section getSection(Station upStation, Station downStation, int distance, Line line) {
    return Section.builder()
        .upStation(upStation)
        .downStation(downStation)
        .distance(distance)
        .line(line)
        .build();
  }

  private Line findLine(Long id) {
    return lineRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LINE_NOT_FOUND));
  }

  public void deleteSection(long lineId, long stationId) {
    Line line = findLine(lineId);
    Sections sections = new Sections(sectionRepository.findByLineOrderByIdAsc(line));
    sections.validateDeleteSection(stationId);
    Station station = findStation(stationId);
    sectionRepository.deleteByDownStation(station);
  }

  private Station findStation(Long id) {
    return stationRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.STATION_NOT_FOUND));
  }
}
