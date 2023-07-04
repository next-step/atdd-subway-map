package subway.service;

import org.springframework.stereotype.Service;
import subway.Station;
import subway.StationRepository;
import subway.domain.Line;
import subway.repository.LineRepository;

@Service
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Line saveStationLine(Long upStationId, Long downStationId, Line line){
        Station upStation = stationRepository.findById(upStationId).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(downStationId).orElseThrow(IllegalArgumentException::new);
        line.changeStations(upStation, downStation);
        return lineRepository.save(line);
    }

    public Line getStationLine(Long id){
        return lineRepository.findByIdFetchEager(id).orElseThrow(IllegalArgumentException::new);
    }
}
