package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long saveSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalStateException(String.format("지하철 노선을 찾을 수 없습니다. (id: %d)", lineId)));

        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new IllegalStateException(String.format("상행종점역을 찾을 수 없습니다. (upStationId: %d)", request.getUpStationId())));

        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new IllegalStateException(String.format("하행종점역을 찾을 수 없습니다. (downStationId: %d)", request.getDownStationId())));

        line.setDownStation(downStation);
        line.plusDistance(request.getDistance());

        Section section = sectionRepository.save(new Section(line, upStation, downStation, request.getDistance()));
        return section.getId();
    }
}
