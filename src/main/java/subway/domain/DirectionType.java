package subway.domain;

public enum DirectionType {
    UP("상행"),
    DOWN("하행");

    private final String name;

    DirectionType(String name) {
        this.name = name;
    }
}
