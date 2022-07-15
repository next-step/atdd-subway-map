package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.infra.LineRepository;
import nextstep.subway.infra.StationRepository;
import nextstep.subway.ui.dto.SectionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public void createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = findLine(lineId);

        Station newUpStation = findStation(sectionRequest.getUpStationId());
        Station newDownStation = findStation(sectionRequest.getDownStationId());

        line.addSection(
                Section.builder()
                        .upStation(newUpStation)
                        .downStation(newDownStation)
                        .build());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLine(lineId);
        Station station = findStation(stationId);

        line.deleteStation(station);
    }

    public Line findLine(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("line is not found"));
    }

    public Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("station is not found"));
    }
}
