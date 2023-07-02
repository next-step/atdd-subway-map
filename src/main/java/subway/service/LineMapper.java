package subway.service;

import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.ui.LineResponse;
import subway.ui.StationResponse;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Component
public class LineMapper {

    private final StationRepository stationRepository;

    public LineMapper(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public LineResponse toLineResponse(Line line) {
        StationResponse upStation = getStationResponse(line.getUpStationId());
        StationResponse downStation = getStationResponse(line.getDownStationId());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(upStation, downStation));
    }

    private StationResponse getStationResponse(Long id) {
        Station station = getStation(id);
        return new StationResponse(station.getId(), station.getName());
    }

    private Station getStation(Long id) {
        return stationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
