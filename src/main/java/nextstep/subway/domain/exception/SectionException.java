package nextstep.subway.domain.exception;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

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

    public static SectionException ofIllegalDownStation(Station station) {
        return new SectionException("Illegal Station; Requested downStation is not Line's downStation");
    }
    public static SectionException ofSectionSize() {
        return new SectionException("Violation Line policy; Have to left one section");
    }
}
