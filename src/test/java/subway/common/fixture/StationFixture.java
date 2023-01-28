package subway.common.fixture;


public enum StationFixture {
    강남역("강남역"),
    서울대입구역("서울대입구역"),
    범계역("범계역"),
    ;

    private final String name;

    StationFixture(final String name) {
        this.name = name;
    }

    public String 역_이름() {
        return name;
    }
}
