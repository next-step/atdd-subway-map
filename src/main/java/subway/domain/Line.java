package subway.domain;

public class Line {

    private Long id;
    private String name;
    private String color;
    private Station upStation;
    private Station downStation;
    private Long distance;

    public Line(Long id, String name, String color, Station upStation, Station downStation, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Line withoutId(String name, String color, Station upStation, Station downStation, Long distance) {
        return new Line(null, name, color, upStation, downStation, distance);
    }

    public static Line withId(Long id, String name, String color, Station upStation, Station downStation, Long distance) {
        return new Line(id, name, color, upStation, downStation, distance);
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }

}
