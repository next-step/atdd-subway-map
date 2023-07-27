package subway.line.code;

public enum LineValidateTypeCode {

    SAVE("이름"),
    UPDATE("아이디와 이름"),

    ;

    private final String description;

    LineValidateTypeCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
