package nextstep.subway.domain.utils;

import nextstep.subway.domain.Section;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.common.consts.LineConsts.MIN_SECTION_COUNT;

public class LineUtils {
    public static void validSectionsSize(int size) {
        if (size < MIN_SECTION_COUNT){
            throw new IllegalArgumentException("삭제할 수 있는 구간이 존재하지 않습니다");
        }
    }



}
