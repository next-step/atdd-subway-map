package nextstep.subway.line.application;

import nextstep.subway.error.NotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineSectionService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineSectionService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse addSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(lineId));
        Section section = createSection(line, request);
        line.getSections().addSection(section);
        return LineResponse.of(lineRepository.save(line));
    }

    private Section createSection(Line line, SectionRequest request) {
        Station upStation = stationService.findByStation(request.getUpStationId());
        Station downStation = stationService.findByStation(request.getDownStationId());

        return Section.Builder.aSection()
                .upStation(upStation)
                .downStation(downStation)
                .line(line)
                .distance(new Distance(request.getDistance()))
                .build();
    }

    public LineResponse removeSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(lineId));
        Station lastStation = stationService.findByStation(stationId);
        line.getSections().removeSection(lastStation);
        return LineResponse.of(lineRepository.save(line));
    }
}
