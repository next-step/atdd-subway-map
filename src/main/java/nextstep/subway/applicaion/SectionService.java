package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionAddRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectionService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionService(final StationRepository stationRepository, final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public void addSection(final SectionAddRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(IllegalArgumentException::new);

        Line line = lineRepository.findById(request.getLineId()).orElseThrow(IllegalArgumentException::new);
        line.addSection(upStation.getId(), downStation.getId(), request.getDistance());
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);

        line.deleteSection(stationId);
    }
}
