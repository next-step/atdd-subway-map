package subway.exception;

public class InvalidSectionUpStationException extends BadRequestException {
    public InvalidSectionUpStationException(long id) {
        super("invalid upStation of section, station id: " + id);
    }
}
