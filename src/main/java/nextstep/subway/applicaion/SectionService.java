package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.exception.NotFoundException;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

@Service
public class SectionService {


    //todo url 질문
    // section과 line controller service 분리?

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Section createSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId).orElseThrow(NotFoundException::new);
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(NotFoundException::new);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(NotFoundException::new);
        Section section = Section.of(upStation, downStation, request.getDistance());
        line.addSection(section);
        return section;
    }
}
