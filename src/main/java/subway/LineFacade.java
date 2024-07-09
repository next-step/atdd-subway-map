package subway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    public LineResponse createLine(LineCreateRequest lineCreateRequest) throws HttpException{
        Line line = lineService.createLine(lineCreateRequest);
        return makeLineResponse(line);
    }


    public LineResponse readLine(Long id) throws HttpException{
        Line line = lineService.readLine(id);
        return makeLineResponse(line);
    }


    public List<LineResponse> readLines() throws HttpException{
        List<Line> lines = lineService.readLines();
        List<LineResponse> linesResponse = new ArrayList<>();
        for (Line line: lines) {
            linesResponse.add(makeLineResponse(line));
        }
        return linesResponse;
    }

    public void updateLine(LineUpdateDTO lineUpdateDTO) throws HttpException {
        lineService.updateLine(lineUpdateDTO);
    }

    public void deleteLine(Long id) {
        lineService.deleteLine(id);
    }

    private LineResponse makeLineResponse(Line line) throws HttpException{
        LineResponse lineResponse = new LineResponse(line.getId(), line.getName(), line.getColor());
        StationResponse upStation = stationService.findStation(line.getUpStationId());
        StationResponse downStation = stationService.findStation(line.getDownStationId());
        return lineResponse
                .addStation(upStation)
                .addStation(downStation);
    }

}
