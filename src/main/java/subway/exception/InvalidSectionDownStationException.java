package subway.exception;

public class InvalidSectionDownStationException extends BadRequestException {
    public InvalidSectionDownStationException(long id) {
        super("invalid downStation of section, station id: " + id);
    }
}
