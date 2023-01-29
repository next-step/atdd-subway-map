package subway.line;

import org.springframework.stereotype.Service;
import subway.station.StationQuery;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;
    private LineQuery lineQuery;
    private StationQuery stationQuery;

    public LineService(LineRepository lineRepository, LineQuery lineQuery, StationQuery stationQuery) {
        this.lineRepository = lineRepository;
        this.lineQuery = lineQuery;
        this.stationQuery = stationQuery;
    }

    @Transactional
    public LineResponse createLine(LineDto lineDto) {
        var upStation = stationQuery.findById(lineDto.getUpStationId());
        var downStation = stationQuery.findById(lineDto.getDownStationId());

        var line = lineRepository.save(new Line(
                lineDto.getName(),
                lineDto.getColor(),
                lineDto.getDistance(),
                upStation,
                downStation
        ));

        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        var lines = lineQuery.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(long lineId) {
        Line line = lineQuery.findById(lineId);
        return LineResponse.from(line);
    }

    @Transactional
    public LineResponse updateLineById(long lineId, UpdateLineDto updateLineDto) {
        Line line = lineQuery.findById(lineId);
        var updatedLine = line.update(updateLineDto.getName(), updateLineDto.getColor());
        return LineResponse.from(lineRepository.save(updatedLine));
    }
}
