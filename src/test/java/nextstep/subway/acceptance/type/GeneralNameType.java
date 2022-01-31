package nextstep.subway.acceptance.type;

public enum GeneralNameType {
    NAME("name"),
    COLOR("color"),
    ID("id");

    private final String type;

    GeneralNameType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
