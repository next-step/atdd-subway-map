package subway;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class LineFacade implements LineService{

    private final LineServiceImpl lineService;
    private final StationService stationService;

    public LineFacade(LineServiceImpl lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Override
    public void createLine(LineCreateRequest lineCreateRequest) {
        lineService.createLine(lineCreateRequest);
    }


    public LineResponse readLine(Long id) {
        return null;
    }


    public List<LineResponse> readLines() {
        return null;
    }

    @Override
    public void updateLine(LineUpdateDTO lineUpdateDTO) {
        lineService.updateLine(lineUpdateDTO);
    }

    @Override
    public void deleteLine(Long id) {
        lineService.deleteLine(id);
    }
}
