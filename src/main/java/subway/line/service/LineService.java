package subway.line.service;

import org.springframework.stereotype.Service;
import subway.station.domain.Station;
import subway.station.repository.StationRepository;
import subway.line.domain.Line;
import subway.line.repository.LineRepository;

import java.util.List;

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

    public List<Line> getStationLines(){
        return lineRepository.findAll();
    }

    public void updateStationLine(Long id, String changedName, String changedColor){
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.changeLine(changedName, changedColor);
        lineRepository.save(line);
    }

    public void deleteStationLine(Long id){
        lineRepository.deleteById(id);
    }
}
