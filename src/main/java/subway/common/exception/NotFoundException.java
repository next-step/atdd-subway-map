package subway.common.exception;

public class NotFoundException extends RuntimeException
{
    private static final String MESSAGE = "번 엔티티를 찾을수 없습니다";

    public NotFoundException(long id)
    {
        super(id + MESSAGE);
    }
}
