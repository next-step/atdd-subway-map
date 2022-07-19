package nextstep.subway.acceptance.fake;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Line;

import static org.hamcrest.Matchers.contains;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FakeLine {
    public static Line mock(String name, String color) {
        return new Line(name, color);
    }
    public static Line 신분당선 =  new Line("신분당선", "red");
    public static Line 분당선 = new Line("분당선", "green");
    public static Line 경춘선 =  new Line("경춘선", "blue");
    public static Line 우이신설 = new Line("우이신설", "blue");
    public static Line 경의중앙선 = new Line("경의중앙선", "blue");

}


