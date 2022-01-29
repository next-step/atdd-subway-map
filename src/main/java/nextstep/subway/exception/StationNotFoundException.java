package nextstep.subway.exception;

public class StationNotFoundException extends BadRequestException{
    public static final String MESSAGE = "존재하지 않는 역입니다. stationId: ";

    public StationNotFoundException(Long id) {
        super(MESSAGE + id);
    }
}
