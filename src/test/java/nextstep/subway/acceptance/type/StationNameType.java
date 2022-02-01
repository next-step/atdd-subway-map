package nextstep.subway.acceptance.type;

public enum StationNameType {
    강남역("강남역"),
    역삼역("역삼역"),
    양재역("양재역"),
    선릉역("선릉역");

    private final String stationName;

    StationNameType(String stationName) {
        this.stationName = stationName;
    }

    public String stationName() {
        return stationName;
    }
}
