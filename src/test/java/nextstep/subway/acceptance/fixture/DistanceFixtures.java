package nextstep.subway.acceptance.fixture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DistanceFixtures {
    TEN(10),
    FIVE(5);

    private final int value;

}
