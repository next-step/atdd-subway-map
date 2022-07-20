package nextstep.subway.acceptance.fixture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ColorFixtures {
    GREEN("green"),
    RED("red"),
    YELLOW("yellow");

    private final String value;

}
