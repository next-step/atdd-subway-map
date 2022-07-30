package nextstep.subway.applicaion.common;

import com.sun.jdi.request.DuplicateRequestException;

import java.util.NoSuchElementException;

public class DuplicatedDownStationException extends DuplicateRequestException {

    public DuplicatedDownStationException() {
        super("하행역이 이미 존재하는 역입니다.");
    }
}
