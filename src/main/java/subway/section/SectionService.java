package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineService;
import subway.station.Station;
import subway.station.StationService;

@Service
public class SectionService {

  private final StationService stationService;
  private final LineService lineService;
  private final SectionRepository sectionRepository;

  public SectionService(StationService stationService, LineService lineService, SectionRepository sectionRepository) {
    this.stationService = stationService;
    this.lineService = lineService;
    this.sectionRepository = sectionRepository;
  }

  @Transactional
  public SectionResponse createSection(Long lineId, SectionCreateRequest request) {
    Station upStation = stationService.findById(request.getUpStationId()).toEntity();
    Station downStation = stationService.findById(request.getUpStationId()).toEntity();
    Line line = lineService.showLine(lineId).get().toEntity();

    Section created = sectionRepository.save(new Section(upStation, downStation, request.getDistance()));
    line.addSection(created);

    return SectionResponse.of(created);
  }

  public SectionResponse removeSection() {
    return null;
//    return SectionResponse.of(sectionRepository.delete());
  }
}
