package nextstep.subway.line.exception;
import org.springframework.dao.DataIntegrityViolationException;


public class DuplicateLineException extends DataIntegrityViolationException{
    public DuplicateLineException(String msg) {
        super(msg);
    }
}
