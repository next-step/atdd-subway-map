package nextstep.subway.applicaion.line;

import nextstep.subway.applicaion.dto.LineCreateResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.line.LineModifyService;
import nextstep.subway.applicaion.station.StationReadService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.ui.section.SectionModifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class LineCreationService {
  // 사용 도메인 서비스들
  private final LineModifyService lineModifyService;
  private final SectionModifyService sectionService;
  private final StationReadService stationReadService;

  public LineCreationService(LineModifyService lineModifyService, SectionModifyService sectionService, StationReadService stationReadService) {
    this.lineModifyService = lineModifyService;
    this.sectionService = sectionService;
    this.stationReadService = stationReadService;
  }

  @Transactional
  public LineCreateResponse saveLine(LineRequest lineRequest){
    // Not null Validation
    Objects.nonNull(lineRequest.getUpStationId());
    Objects.nonNull(lineRequest.getDownStationId());
    Objects.nonNull(lineRequest.getDistance());

    Line line = lineModifyService.saveLine(lineRequest.getName(), lineRequest.getColor());
    Station upStation = stationReadService.findSpecificStation(lineRequest.getUpStationId());
    Station downStation = stationReadService.findSpecificStation(lineRequest.getUpStationId());

    Section section = new Section(line, upStation, downStation, lineRequest.getDistance());
    line.addSection(section);
    return LineCreateResponse.of(line);
  }
}
