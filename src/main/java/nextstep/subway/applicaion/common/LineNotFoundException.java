package nextstep.subway.applicaion.common;

import java.util.NoSuchElementException;

public class LineNotFoundException extends NoSuchElementException {

    public LineNotFoundException() {
        super("해당 노선이 존재하지 않습니다.");
    }
}
