package nextstep.subway.acceptance.fixture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LineFixtures {
    이호선("2호선"),
    신분당선("신분당선"),
    분당선("분당선");

    private final String value;

}
