package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Line.Line;
import nextstep.subway.domain.Line.LineRepository;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public SectionResponse save(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지하철 역입니다."));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지하철 역입니다."));
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노션입니다."));
        line.addSection(upStation, downStation, sectionRequest.getDistance());
        return new SectionResponse(upStation.getId(), downStation.getId(), sectionRequest.getDistance());
    }

    @Transactional
    public void delete(Long lineId, Long sectionDownStationId) {
        Station downStation = stationRepository.findById(sectionDownStationId).orElseThrow(() -> new EntityNotFoundException("station.not.found"));
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException("line.not.found"));
        line.deleteSection(downStation);
    }

}
