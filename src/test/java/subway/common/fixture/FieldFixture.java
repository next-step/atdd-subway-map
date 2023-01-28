package subway.common.fixture;


public enum FieldFixture {
    역_이름("name"),
    ;

    private final String value;

    FieldFixture(String value) {
        this.value = value;
    }

    public String 필드명() {
        return value;
    }
}
