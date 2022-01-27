package nextstep.subway.domain.exception;

import nextstep.subway.domain.Section;

public class SectionException extends RuntimeException {

    private SectionException(String message) {
        super(message);
    }

    public static SectionException ofIllegalUpStation(Section section) {
        return new SectionException("Illegal Section; Requested upStation differs from Line's downStation");
    }

    public static SectionException ofIllegalDownStation(Section section) {
        return new SectionException("Illegal Section; Requested downStation already belongs to the Line");
    }
}
