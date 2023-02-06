package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.presentation.LineResponse;
import subway.station.application.StationQuery;
import subway.station.application.dto.Stations;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
        Stations stations = stationQuery.findAllByIdIn(List.of(lineDto.getUpStationId(), lineDto.getDownStationId()));

        var line = lineRepository.save(new Line(
                lineDto.getName(),
                lineDto.getColor(),
                lineDto.getDistance(),
                stations.getById(lineDto.getUpStationId()),
                stations.getById(lineDto.getDownStationId())
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

    @Transactional
    public void deleteLineById(long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional
    public LineResponse addSection(long lineId, SectionDto sectionDto) {
        Stations stations = stationQuery.findAllByIdIn(List.of(sectionDto.getUpStationId(), sectionDto.getDownStationId()));

        Line line = lineQuery.findById(lineId);
        Line updatedLine = line.addSection(
                stations.getById(sectionDto.getUpStationId()),
                stations.getById(sectionDto.getDownStationId()),
                sectionDto.getDistance()
        );

        return LineResponse.from(updatedLine);
    }

    @Transactional
    public void deleteSection(Long lineId, long stationId) {
        Station deleteStation = stationQuery.findById(stationId);
        Line line = lineQuery.findById(lineId);
        line.deleteSection(deleteStation);
    }
}
