package subway.section;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineService;
import subway.station.Station;
import subway.station.StationService;

@Service
public class SectionService {

  private final SectionRepository sectionRepository;
  private final StationService stationService;
  private final LineService lineService;

  public SectionService(StationService stationService, @Lazy LineService lineService, SectionRepository sectionRepository) {
    this.stationService = stationService;
    this.lineService = lineService;
    this.sectionRepository = sectionRepository;
  }

  @Transactional
  public SectionResponse createSection(Long lineId, SectionCreateRequest request) {
    Station upStation = stationService.findById(request.getUpStationId()).toEntity();
    Station downStation = stationService.findById(request.getDownStationId()).toEntity();
    Line line = lineService.showLine(lineId).get().toEntity();

    Section created = sectionRepository.save(new Section(upStation, downStation, request.getDistance()));

    lineService.save(line.addSection(created));

    return SectionResponse.of(created);
  }

  public void removeSection(Long lineId, Long sectionId) {
    Line line = lineService.showLine(lineId).get().toEntity();
    Section section = sectionRepository.findById(sectionId).get();

    System.out.println("remove!!");
    System.out.println(line);
    System.out.println(section);
    line.removeSection(section);

    System.out.println("delete by id");
    sectionRepository.deleteById(sectionId);
    System.out.println("succeed remove");
  }
}
