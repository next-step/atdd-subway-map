package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final StationService stationService;
    private final LineService lineService;
    private final SectionRepository sectionRepository;

    public SectionService(StationService stationService, LineService lineService,
                       SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void saveSection(Long lineId, SectionRequest request) {
        Station downStation = stationService.findById(request.getDownStationId());
        Station upStation = stationService.findById(request.getUpStationId());

        Section section = sectionRepository.save(new Section(
                        downStation,
                        upStation,
                        request.getDistance()
                )
        );
        Line line = lineService.getLineById(lineId);
        line.addSection(section);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineService.getLineById(lineId);
        Section section = line.deleteSection(stationId);
        sectionRepository.delete(section);
    }
}
