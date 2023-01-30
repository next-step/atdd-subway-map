package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.ui.dto.LineSectionRequest;
import subway.ui.dto.LineSectionResponse;

@Service
@Transactional(readOnly = true)
public class LineSectionService {

    private final StationService stationService;
    private final LineService lineService;

    public LineSectionService(final StationService stationService, final LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional
    public void addSection(final Long lineId, final LineSectionRequest lineSectionRequest) {

        final Station upStation = stationService.findById(lineSectionRequest.getUpStationId());
        final Station downStation = stationService.findById(lineSectionRequest.getDownStationId());
        final Line line = lineService.findById(lineId);
        line.addSection(upStation, downStation, lineSectionRequest.getDistance());
    }

    @Transactional
    public void removeSection(final Long lineId, final Long stationId) {

        final Station downStation = stationService.findById(stationId);
        final Line line = lineService.findById(lineId);
        line.removeSection(downStation);
    }

    public LineSectionResponse findSectionByline(final Long id) {

        return LineSectionResponse.createResponse(lineService.findById(id));
    }
}
