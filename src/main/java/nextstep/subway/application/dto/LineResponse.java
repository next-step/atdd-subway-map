package nextstep.subway.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@RequiredArgsConstructor
@Getter
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;
}
