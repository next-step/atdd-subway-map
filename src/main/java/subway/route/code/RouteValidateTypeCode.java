package subway.route.code;

public enum RouteValidateTypeCode {

    SAVE("이름"),
    UPDATE("아이디와 이름"),

    ;

    private final String description;

    RouteValidateTypeCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
