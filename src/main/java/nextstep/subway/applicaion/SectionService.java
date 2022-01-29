package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionAddRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectionService {
    private final LineService lineService;
    private final StationService stationService;

    public SectionService(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional
    public void addSection(final SectionAddRequest request) {
        Station upStation = stationService.getStation(request.getUpStationId());
        Station downStation = stationService.getStation(request.getDownStationId());

        Line line = lineService.getLine(request.getLineId());
        line.addSection(upStation.getId(), downStation.getId(), request.getDistance());
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Line line = lineService.getLine(lineId);

        line.deleteSection(stationId);
    }
}
