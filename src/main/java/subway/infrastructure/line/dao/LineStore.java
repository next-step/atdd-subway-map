package subway.infrastructure.line.dao;

import org.springframework.stereotype.Component;
import subway.domain.line.entity.Line;
import subway.domain.line.entity.Section;
import subway.domain.station.entity.Station;
import subway.infrastructure.line.LineRepository;
import subway.infrastructure.line.SectionRepository;
import subway.infrastructure.station.StationRepository;
import subway.interfaces.line.dto.LineRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Component
public class LineStore {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public LineStore(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public Section createSection(LineRequest lineRequest) {
        List<Station> stations = stationRepository.findAllById(List.of(lineRequest.getUpStationId(), lineRequest.getDownStationId()));
        Station upStation = stations.stream().filter(station -> Objects.equals(station.getId(), lineRequest.getUpStationId())).findFirst().orElseThrow(() -> new EntityNotFoundException("station_id: " + lineRequest.getUpStationId()));
        Station downStation = stations.stream().filter(station -> Objects.equals(station.getId(), lineRequest.getDownStationId())).findFirst().orElseThrow(() -> new EntityNotFoundException("station_id: " + lineRequest.getUpStationId()));

        Section init = new Section(upStation, downStation, lineRequest.getDistance());
        return sectionRepository.save(init);
    }

    public Line store(Line init) {
        return lineRepository.save(init);
    }

    public void remove(Line line) {
        sectionRepository.deleteAll(line.getSections());
        lineRepository.delete(line);
    }
}
