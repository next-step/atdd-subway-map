package subway.section;

import org.springframework.stereotype.Service;
import subway.line.Line;
import subway.line.service.LineDataService;
import subway.station.Station;

import javax.transaction.Transactional;

@Transactional
@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    private final LineDataService lineDataService;

    public SectionService(SectionRepository sectionRepository, LineDataService lineDataService) {
        this.sectionRepository = sectionRepository;
        this.lineDataService = lineDataService;
    }

    public void saveSection(Long lineId, SectionCreateRequest request) {
        Station upStation = lineDataService.findStation(request.getUpStationId());
        Station downStation = lineDataService.findStation(request.getDownStationId());

        Line line = lineDataService.findLine(lineId);

        Section section = Section.verifyAndGenerate(request.getDistance(), upStation, downStation, line);
        Section savedSection = sectionRepository.save(section);

        line.generateSection(savedSection);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineDataService.findLine(lineId);

        if (line.getSections().size() <= 1) {
            throw new SectionException("구간이 1개인 노선의 구간은 삭제할 수 없습니다.");
        }

        Section deleteSection = line.getSections().stream().filter(s -> s.getDownStation().getId().equals(stationId)).findFirst().get();

        if (!deleteSection.getDownStation().getId().equals(line.getDownStation().getId())) {
            throw new SectionException("노선의 하행종점역만 제거할 수 있습니다.");
        }

        sectionRepository.delete(deleteSection);

        line.deleteSection(deleteSection);
    }
}
