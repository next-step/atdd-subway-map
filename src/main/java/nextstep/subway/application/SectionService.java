package nextstep.subway.application;

import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.application.dto.SectionResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.exception.LineException;
import nextstep.subway.domain.exception.StationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectionService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SectionResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new LineException.NotFound(lineId));
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new StationException.NotFound(sectionRequest.getUpStationId()));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new StationException.NotFound(sectionRequest.getDownStationId()));

        Section section = new Section(line, upStation, downStation, sectionRequest.getDistance());
        line.extend(section);
        return new SectionResponse();
    }
}
