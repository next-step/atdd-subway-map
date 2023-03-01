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

  /*
  순환 참조는 어떻게 해결하는게 좋을까요?
   */
  public SectionService(StationService stationService, @Lazy LineService lineService, SectionRepository sectionRepository) {
    this.stationService = stationService;
    this.lineService = lineService;
    this.sectionRepository = sectionRepository;
  }

  /*
    Service Layer에서 DTO를 반환한다면, 다른 서비스에서 해당 Entity를 같이 사용해야 할 때 어떻게 쓰는게 좋을까?
    DTO.toEntity()는 JPA가 영속성컨텍스트에 보관하는 해당 객체가 아니기 때문에, line.addSection()만으로 바로 저장되지 않음
   */
  @Transactional
  public SectionResponse createSection(Long lineId, SectionCreateRequest request) {
    Station upStation = stationService.findById(request.getUpStationId()).toEntity();
    Station downStation = stationService.findById(request.getDownStationId()).toEntity();
    Line line = lineService.findById(lineId);

    Section created = sectionRepository.save(new Section(upStation, downStation, request.getDistance()));

    lineService.save(line.addSection(created));

    return SectionResponse.of(created);
  }

  public void removeSection(Long lineId, Long sectionId) {
    Line line = lineService.findById(lineId);
    Section section = sectionRepository.findById(sectionId).orElseThrow(IllegalArgumentException::new);

    line.removeSection(section);
    sectionRepository.deleteById(sectionId);
  }
}
