package subway.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LineFixture {
    public Long id;
    public String name;
    public String color;
    public StationFixture upStation;
    public StationFixture downStation;
    public Long distance;

    public static LineFixture 신분당선(StationFixture up, StationFixture down) {
        final String name = "신분당선";
        final String color = "bg-red-600";
        final Long distance = 10L;
        LineFixture line = new LineFixture();
        line.name = name;
        line.color = color;
        line.upStation = up;
        line.downStation = down;
        line.distance = distance;
        return line;
    }

    public static LineFixture 분당선(StationFixture up, StationFixture down) {
        final String name = "분당선";
        final String color = "bg-greed-600";
        final Long distance = 10L;

        LineFixture line = new LineFixture();
        line.name = name;
        line.color = color;
        line.upStation = up;
        line.downStation = down;
        line.distance = distance;
        return line;
    }

}