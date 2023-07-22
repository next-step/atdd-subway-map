package subway.global.exception;

public class TargetNotFound extends RuntimeException{

    public TargetNotFound() {
        super("해당 엔티티가 존재하지 않습니다");
    }
}
