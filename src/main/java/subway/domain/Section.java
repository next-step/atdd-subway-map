package subway.domain;

public class Section {

    private Long id;
    private Station downStation;
    private Station upStation;
    private Long distance;
    private Line line;

    public Section(Long id, Station downStation, Station upStation, Long distance, Line line) {
        this.id = id;
        this.downStation = downStation;
        this.upStation = upStation;
        this.distance = distance;
        this.line = line;
    }

    public static Section withNoId(Station downStation, Station upStation, Long distance, Line line) {
        return new Section(null, downStation, upStation, distance, line);
    }

    public static Section withId(Long id, Station downStation, Station upStation, Long distance, Line line) {
        return new Section(id, downStation, upStation, distance, line);
    }

    public Long getId() {
        return id;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Long getDistance() {
        return distance;
    }

    public Line getLine() {
        return line;
    }

}
