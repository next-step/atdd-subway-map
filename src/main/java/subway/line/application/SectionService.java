package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.request.SectionCreateRequest;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.station.application.StationService;
import subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final LineService lineService;
    private final StationService stationService;

    public SectionService(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    /**
     * 지하철 구간 정보를 등록합니다.
     *
     * @param lineId 등록할 지하철 구간의 노선 고유 번호
     * @param sectionCreateRequest 등록할 지하철 구간 정보
     * @return 등록된 지하철 구간 고유 번호
     */
    @Transactional
    public Long saveSection(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        Line findLine = lineService.findLineById(lineId);
        Station upStation = stationService.findStationById(sectionCreateRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionCreateRequest.getDownStationId());
        Section section = Section.createSection(findLine, upStation, downStation, sectionCreateRequest.getDistance());

        findLine.getSections().addSection(section);

        return section.getId();
    }

    /**
     * 지하철 구간 정보를 삭제합니다.
     *
     * @param lineId 삭제할 지하철 구간의 노선 고유 번호
     * @param stationId 삭제할 지하철 구간 고유 번호
     */
    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Line findLine = lineService.findLineById(lineId);
        Station findStation = stationService.findStationById(stationId);

        findLine.getSections().removeLastSection(findStation);
    }
}
