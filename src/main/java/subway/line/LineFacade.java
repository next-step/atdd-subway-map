package subway.line;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.station.StationResponse;
import subway.station.StationService;

import java.util.ArrayList;
import java.util.List;
@Component
public class LineFacade {

    private final LineService lineService;
    private final StationService stationService;

    public LineFacade(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse createLine(LineCreateRequest lineCreateRequest) {
        Line line = lineService.createLine(lineCreateRequest);
        return makeLineResponse(line);
    }


    public LineResponse readLine(Long id) {
        Line line = lineService.readLine(id);
        return makeLineResponse(line);
    }


    public List<LineResponse> readLines() {
        List<Line> lines = lineService.readLines();
        List<LineResponse> linesResponse = new ArrayList<>();
        for (Line line: lines) {
            linesResponse.add(makeLineResponse(line));
        }
        return linesResponse;
    }

    public void updateLine(LineUpdateDTO lineUpdateDTO) {
        lineService.updateLine(lineUpdateDTO);
    }

    public void deleteLine(Long id) {
        lineService.deleteLine(id);
    }

    private LineResponse makeLineResponse(Line line) {
        LineResponse lineResponse = new LineResponse(line.getId(), line.getName(), line.getColor());
        StationResponse upStation = stationService.findStation(line.getUpStationId());
        StationResponse downStation = stationService.findStation(line.getDownStationId());
        return lineResponse
                .addStation(upStation)
                .addStation(downStation);
    }

}
