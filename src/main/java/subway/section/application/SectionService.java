package subway.section.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.LineService;
import subway.line.domain.Line;
import subway.section.application.dto.request.SectionCreateRequest;
import subway.section.domain.Section;
import subway.station.application.StationService;
import subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final StationService stationService;
    private final LineService lineService;

    public SectionService(final StationService stationService,
                          final LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional
    public Long saveSection(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        Line findLine = lineService.findLineById(lineId);
        Station upStation = stationService.findStationById(sectionCreateRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionCreateRequest.getDownStationId());
        Section section = Section.createSection(findLine, upStation, downStation, sectionCreateRequest.getDistance());

        findLine.getSections().addSection(section);

        return section.getId();
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Line findLine = lineService.findLineById(lineId);
        Station findStation = stationService.findStationById(stationId);

        findLine.getSections().remove(findStation);
    }
}
