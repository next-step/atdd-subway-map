package nextstep.subway.applicaion.common;

import com.sun.jdi.request.DuplicateRequestException;

public class UnappropriateUpStationException extends IllegalArgumentException {

    public UnappropriateUpStationException() {
        super("상행역이 해당 역의 하행역이 아닙니다.");
    }
}
