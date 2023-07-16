package subway.line;

import org.apache.coyote.Response;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse makeLine(LineRequest request) {
        Line line = new Line();

        line.setName(request.getName());
        line.setColor(request.getColor());
        Station upStation = stationRepository.getReferenceById(request.getUpStationId());
        Station downStation = stationRepository.getReferenceById(request.getDownStationId());
        Line save = lineRepository.save(line);

        save.setUpStation(upStation);
        save.setDownStation(downStation);

        return new LineResponse(save, save.getUpStation(), save.getDownStation());
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll()
                .stream().map(v -> new LineResponse(v, v.getUpStation(), v.getDownStation()))
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = lineRepository.getReferenceById(id);
        if(line == null){
            throw new NoSuchElementException("없는 라인정보 입니다.");
        }
        return new LineResponse(line, line.getUpStation(), line.getDownStation());
    }
    @Transactional
    public LineResponse update(Long id,LineRequest request) {
        Line line = lineRepository.getReferenceById(id);
        if(!Strings.isBlank(request.getName())){
            line.setName(request.getName());
        }
        if(!Strings.isBlank(request.getColor())){
            line.setColor(request.getColor());
        }
        return new LineResponse(line, line.getUpStation(), line.getDownStation());
    }
    @Transactional
    public void delete(Long id) {
        Line line = lineRepository.getReferenceById(id);
        lineRepository.delete(line);
    }
}
