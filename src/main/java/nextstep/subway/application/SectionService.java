package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.domain.Section.validateDeleteSectionRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class SectionService {
    private final LineService lineService;
    private final StationService stationService;

    public void deleteBy(Long lineId, Long stationId) {
        Line line = lineService.findLineBy(lineId);
        Station station = stationService.findBy(stationId);

        validateDeleteSectionRequest(line, station);

        line.removeLastSection();
    }
}
