package nextstep.subway.fixture;

public enum TLine {

    신분당선("신분당선", "bg-red-600", 1L, 2L, 10),
    구분당선("구분당선", "bg-blue-600", 2L, 3L, 10),
    _2호선("2호선", "bg-green-600", 4L, 5L, 10);

    public final String 이름;
    public final String 색;
    public final Long 상행역;
    public final Long 하행역;
    public final int 거리;

    TLine(String 이름, String 색, Long 상행역, Long 하행역, int 거리) {
        this.이름 = 이름;
        this.색 = 색;
        this.상행역 = 상행역;
        this.하행역 = 하행역;
        this.거리 = 거리;
    }
}
