package subway.common;

public class NotFoundSubwayLineException extends NotFoundException {

    public NotFoundSubwayLineException(Long id) {
        super(String.format("not found station line : %d", id));
    }
}
