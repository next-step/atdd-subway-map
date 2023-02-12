package subway.section;

import org.springframework.stereotype.Service;
import subway.line.LineQuery;
import subway.line.dto.LineResponse;
import subway.section.dto.SectionDto;
import subway.station.StationQuery;

import javax.transaction.Transactional;

@Service
public class SectionService {

    private LineQuery lineQuery;

    private StationQuery stationQuery;

    public SectionService(LineQuery lineQuery, StationQuery stationQuery) {
        this.lineQuery = lineQuery;
        this.stationQuery = stationQuery;
    }

    @Transactional
    public LineResponse addSection(Long lineId, SectionDto sectionDto) {
        var line = lineQuery.findById(lineId);
        var stations = stationQuery.getStations(sectionDto.getStationIds());
        return LineResponse.from(
                line.addSection(
                        stations.findById(sectionDto.getUpStationId()),
                        stations.findById(sectionDto.getDownStationId()),
                        sectionDto.getDistance()
                )
        );
    }

    public void deleteSection(Long lineId, Long stationId) {
        var line = lineQuery.findById(lineId);
        var station = stationQuery.getStation(stationId);
        line.deleteSection(station);
    }
}
