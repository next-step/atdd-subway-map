package subway.line;

import org.springframework.stereotype.Service;
import subway.line.dto.LineDto;
import subway.line.dto.LineResponse;
import subway.station.Station;
import subway.station.StationQuery;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationQuery stationQuery;

    public LineService(LineRepository lineRepository, StationQuery stationQuery) {
        this.lineRepository = lineRepository;
        this.stationQuery = stationQuery;
    }

    @Transactional
    public LineResponse createLine(LineDto lineDto) {
        var stations = queryStations(lineDto);

        var line = lineRepository.save(
                new Line(
                        lineDto.getName(),
                        lineDto.getColor(),
                        lineDto.getDistance(),
                        stations.get(0),
                        stations.get(1)
                )
        );

        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        return lineQuery.findAll()
                .stream().map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return LineResponse.from(lineRepository.findById(id).get()); // TODO null일경우 exception 처리
    }

    private List<Station> queryStations(LineDto lineDto) {
        return stationQuery.findStationByIdIn(
                List.of(lineDto.getUpStationId(), lineDto.getDownStationId())
        );
    }
}
