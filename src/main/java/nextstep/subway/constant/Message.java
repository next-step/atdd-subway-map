package nextstep.subway.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {

    NOT_FOUND_STATION("해당 역을 찾을 수 없습니다."),

    ONLY_ADD_DOWNSTATION("하행선만 등록 가능합니다."),
    IS_EXIST_STATION("이미 등록된 역입니다."),

    ONLY_DELETE_DOWNSTAION("하행선만 삭제 가능합니다."),
    ONE_COUNT_SECTIONLIST("구간이 1개여서 삭제할 수 없습니다.");

    private String value;

}
