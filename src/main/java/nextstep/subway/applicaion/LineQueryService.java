package nextstep.subway.applicaion;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LineQueryService {

  private final LineRepository lineRepository;
  private final SectionRepository sectionRepository;

  public List<LineResponse> getAllLine() {
    List<LineResponse> lineResponses = new ArrayList<>();

    List<Line> lines = lineRepository.findAll();
    for (Line line : lines) {
      List<Station> stations = getSectionInStations(sectionRepository.findByLineOrderByIdAsc(line));
      lineResponses.add(LineResponse.createResponse(line, stations));
    }
    return lineResponses;
  }

  public LineResponse getLine(Long id) {
    Line line = findLine(id);
    List<Section> sections = sectionRepository.findByLineOrderByIdAsc(line);
    List<Station> stations = getSectionInStations(sections);
    return LineResponse.createResponse(line, stations);
  }

  private Line findLine(Long id) {
    return lineRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LINE_NOT_FOUND));
  }

  private List<Station> getSectionInStations(List<Section> sections) {
    return sections.stream()
        .map(Section::getSectionInStation)
        .flatMap(List::stream)
        .distinct()
        .collect(toList());
  }
}
