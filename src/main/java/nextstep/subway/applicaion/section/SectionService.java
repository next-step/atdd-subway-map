package nextstep.subway.applicaion.section;

import nextstep.subway.applicaion.common.LineNotFoundException;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.line.domain.LineRepository;
import nextstep.subway.applicaion.section.dto.SectionRequest;
import nextstep.subway.applicaion.station.StationService;
import nextstep.subway.applicaion.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SectionService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    public SectionService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public void saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);

        Station upStation = stationService.getStationThrowExceptionIfNotExists(sectionRequest.getUpStationId());
        Station downStation = stationService.getStationThrowExceptionIfNotExists(sectionRequest.getDownStationId());

        line.getSections().checkUpStation(upStation);
        line.getSections().checkIsNewStation(downStation);

        line.getSections().addLast(line, upStation, downStation, sectionRequest.getDistance());
    }
}
