package nextstep.subway.section.application.manager;

import java.util.stream.Stream;

public interface SectionLineManager {
    boolean existsByLine(Long lineId);

    Stream<LineData> getAllLines();
}
