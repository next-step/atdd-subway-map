package subway.exception;

public class SameEndStationException extends BadRequestException{
    public SameEndStationException() {
        super("The up station and down station cannot be the same.");
    }
}
