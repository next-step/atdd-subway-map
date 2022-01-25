package nextstep.subway.model;

import static nextstep.subway.model.Station.*;

public enum Line {

    신분당선("bg-red-600", 정자역, 판교역, 1),
    이호선("bg-green-600", 강남역, 역삼역, 2),
    ;

    private final String color;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    Line(String color, Station upStation, Station downStation, int distance) {
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public String getColor() {
        return color;
    }

    public String getUpStation() {
        return upStation.name();
    }

    public String getDownStation() {
        return downStation.name();
    }

    public int getDistance() {
        return distance;
    }
}
