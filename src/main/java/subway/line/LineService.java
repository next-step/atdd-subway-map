package subway.line;

import org.springframework.stereotype.Service;
import subway.line.dto.LineDto;
import subway.line.dto.LineResponse;
import subway.line.dto.UpdateLineDto;
import subway.station.StationQuery;
import subway.station.Stations;

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
        var line = saveLine(lineDto);
        var stations = getStations(lineDto);
        return LineResponse.from(
                line.addSection(
                        stations.findById(lineDto.getUpStationId()),
                        stations.findById(lineDto.getDownStationId()),
                        lineDto.getDistance()
                )
        );
    }

    public List<LineResponse> findAllLines() {
        return lineQuery.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long lineId) {
        return LineResponse.from(lineQuery.findById(lineId));
    }

    public LineResponse updateLine(Long lineId, UpdateLineDto updateLineDto) {
        var line = lineQuery.findById(lineId);
        var updatedLine = line.update(updateLineDto.getName(), updateLineDto.getColor());
        return LineResponse.from(lineRepository.save(updatedLine));
    }

    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    private Stations getStations(LineDto lineDto) {
        return stationQuery.getStations(lineDto.getStationIds());
    }

    private Line saveLine(LineDto lineDto) {
        return lineRepository.save(
                new Line(
                        lineDto.getName(),
                        lineDto.getColor()
                )
        );
    }
}
