package nextstep.subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.exception.EntityNotFoundException;
import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.line.domain.dto.SectionResponse;
import nextstep.subway.line.domain.model.Line;
import nextstep.subway.line.domain.model.Section;
import nextstep.subway.line.domain.repository.LineRepository;
import nextstep.subway.station.domain.model.Station;
import nextstep.subway.station.domain.repository.StationRepository;

@Transactional
@Service
public class LineSectionService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineSectionService(LineRepository lineRepository,
                              StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SectionResponse addSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findByIdWithSections(lineId)
                                  .orElseThrow(EntityNotFoundException::new);
        Station upStation = stationRepository.findById(request.getUpStationId())
                                             .orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(request.getDownStationId())
                                               .orElseThrow(EntityNotFoundException::new);

        Section section = line.createSection(upStation, downStation, request.getDistance());
        return SectionResponse.from(section);
    }
}
