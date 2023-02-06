package subway.line.domain;

public class FakeStationValidator implements StationValidator {
    @Override
    public boolean existsStations(Long... ids) {
        return true;
    }
}
