package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.service.LineService;
import nextstep.subway.domain.service.SectionService;
import nextstep.subway.domain.service.StationService;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.ui.dto.SectionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectionApiService {

    private final LineService lineService;
    private final SectionService sectionService;
    private final StationService stationService;

    @Transactional
    public void createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineService.findLine(lineId);

        Station newUpStation = stationService.findStation(sectionRequest.getUpStationId());
        Station newDownStation = stationService.findStation(sectionRequest.getDownStationId());

        line.addSection(
                Section.builder()
                        .upStation(newUpStation)
                        .downStation(newDownStation)
                        .build());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineService.findLine(lineId);
        Station station = stationService.findStation(stationId);

        line.deleteStation(station);
    }
}
