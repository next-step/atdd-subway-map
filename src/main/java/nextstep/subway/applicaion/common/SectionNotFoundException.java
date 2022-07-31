package nextstep.subway.applicaion.common;

import java.util.NoSuchElementException;

public class SectionNotFoundException extends NoSuchElementException {

    public SectionNotFoundException() {
        super("해당 구간이 존재하지 않습니다.");
    }
}
