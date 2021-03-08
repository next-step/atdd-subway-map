package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LineMapper {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineMapper(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public Line toLine(LineRequest lineRequest) {
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());
        return new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
    }

    public Section toSection(Long id, SectionRequest sectionRequest) {
        Line line = getLine(id);
        Station upStation = getStation(sectionRequest.getUpStationId());
        Station downStation = getStation(sectionRequest.getDownStationId());
        return new Section(line, upStation, downStation, sectionRequest.getDistance());
    }

    private Station getStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(id));
    }
}
