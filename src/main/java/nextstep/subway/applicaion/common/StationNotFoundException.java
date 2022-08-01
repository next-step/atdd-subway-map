package nextstep.subway.applicaion.common;

import java.util.NoSuchElementException;

public class StationNotFoundException extends NoSuchElementException {

    public StationNotFoundException() {
        super("해당 역이 존재하지 않습니다.");
    }
}
