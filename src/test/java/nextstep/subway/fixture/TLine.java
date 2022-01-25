package nextstep.subway.fixture;

public enum TLine {

    신분당선("신분당선", "bg-red-600"),
    구분당선("구분당선", "bg-blue-600"),
    _2호선("2호선", "bg-green-600");

    public final String 이름;
    public final String 색;

    TLine(String 이름, String 색) {
        this.이름 = 이름;
        this.색 = 색;
    }
}
