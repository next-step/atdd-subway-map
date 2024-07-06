package subway;

public class LineFacade implements LineService{

    private final LineServiceImpl lineService;
    private final StationService stationService;

    public LineFacade(LineServiceImpl lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }
}
