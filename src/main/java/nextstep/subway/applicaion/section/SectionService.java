package nextstep.subway.applicaion.section;
import nextstep.subway.applicaion.common.LineNotFoundException;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.line.domain.LineRepository;
import nextstep.subway.applicaion.section.dto.SectionRequest;
import nextstep.subway.applicaion.station.StationService;
import nextstep.subway.applicaion.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

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
        Line line = lineRepository.findById(lineId).orElse(null);

        if (line == null) {
            throw new LineNotFoundException();
        }

        checkUpStation(line, sectionRequest.getUpStationId());
        checkDownStation(sectionRequest.getDownStationId());

        line.addStation(sectionRequest.getDownStationId(), sectionRequest.getDistance());
        lineRepository.save(line);
    }

    private void checkUpStation(Line line, Long upStationId) {
        if (!Objects.equals(line.getDownStationId(), upStationId)) {
            throw new IllegalArgumentException("상행역이 해당 역의 하행역이 아닙니다.");
        }
    }

    private void checkDownStation(Long downStationId) {
        Station station = stationService.getStationById(downStationId);

        if (station != null) {
            throw new IllegalArgumentException("하행역이 이미 존재하는 역입니다.");
        }
    }
}
