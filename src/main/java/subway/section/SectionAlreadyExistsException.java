package subway.section;

public class SectionAlreadyExistsException extends RuntimeException {
    public SectionAlreadyExistsException(Long stationId) {
        super("downStation " + stationId + " already exists.");
    }
}
