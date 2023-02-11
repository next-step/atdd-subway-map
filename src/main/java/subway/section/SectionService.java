package subway.section;

import org.springframework.stereotype.Service;
import subway.line.LineQuery;
import subway.line.dto.LineResponse;
import subway.section.dto.SectionDto;
import subway.station.Station;
import subway.station.StationQuery;
import subway.station.Stations;

import java.util.List;

@Service
public class SectionService {

    private LineQuery lineQuery;

    private StationQuery stationQuery;

    public SectionService(LineQuery lineQuery, StationQuery stationQuery) {
        this.lineQuery = lineQuery;
        this.stationQuery = stationQuery;
    }

    public LineResponse addSection(Long lineId, SectionDto sectionDto) {
        var stations = stationQuery.getStations(sectionDto.getStationIds());
        var line = lineQuery.findById(lineId);

        var updatedLine = line.addSection(
                stations.findById(sectionDto.getUpStationId()),
                stations.findById(sectionDto.getDownStationId()),
                sectionDto.getDistance()
        );

        return LineResponse.from(updatedLine);
    }
}
