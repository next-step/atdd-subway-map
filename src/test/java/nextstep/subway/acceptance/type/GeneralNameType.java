package nextstep.subway.acceptance.type;

public enum GeneralNameType {
    NAME("name"),
    COLOR("color"),
    ID("id"),
    LINE_PATH_PREFIX("/lines"),
    SECTION_PATH_PREFIX("/sections");

    private final String type;

    GeneralNameType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
