package subway.line.service;

import org.springframework.stereotype.Service;
import subway.line.packet.LineResponse;
import subway.station.domain.Station;
import subway.station.repository.StationRepository;
import subway.line.domain.Line;
import subway.line.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveStationLine(Long upStationId, Long downStationId, Line line){
        Station upStation = stationRepository.findById(upStationId).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(downStationId).orElseThrow(IllegalArgumentException::new);
        line.changeStations(upStationId, downStationId);
        Line savedLine = lineRepository.save(line);
        return LineResponse.fromEntity(savedLine, upStation, downStation);
    }

    public LineResponse getStationLine(Long id){
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow(IllegalArgumentException::new);
        return LineResponse.fromEntity(line, upStation, downStation);
    }

    public List<LineResponse> getStationLines(){
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(o -> {
            Station upStation = stationRepository.findById(o.getUpStationId()).orElseThrow(IllegalArgumentException::new);
            Station downStation = stationRepository.findById(o.getDownStationId()).orElseThrow(IllegalArgumentException::new);
            return LineResponse.fromEntity(o, upStation, downStation);
        }).collect(Collectors.toList());
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
