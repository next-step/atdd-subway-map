package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineDomainService;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectionService {

    private LineDomainService lineDomainService;
    private StationService stationService;
    private LineRepository lineRepository;

    public SectionService(LineDomainService lineDomainService, StationService stationService, LineRepository lineRepository) {
        this.lineDomainService = lineDomainService;
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public SectionResponse addSection(Long lineId, SectionRequest request) {
        Line line = lineDomainService.getLineEntity(lineId);
        Station upStation = stationService.getStationEntity(request.getUpStationId());
        Station downStation = stationService.getStationEntity(request.getDownStationId());

        Section section = line.addSection(upStation, downStation, request.getDistance());
        lineRepository.save(line);
        return SectionResponse.of(section);
    }

}
