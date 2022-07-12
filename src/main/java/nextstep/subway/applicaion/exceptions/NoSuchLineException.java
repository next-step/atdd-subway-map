package nextstep.subway.applicaion.exceptions;

import java.util.NoSuchElementException;

public class NoSuchLineException extends NoSuchElementException {
    public NoSuchLineException(String s) {
        super(s);
    }
}
