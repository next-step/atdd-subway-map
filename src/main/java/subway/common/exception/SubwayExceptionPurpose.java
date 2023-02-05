package subway.common.exception;


public enum SubwayExceptionPurpose {
    CREATE("create"),
    READ("read"),
    UPDATE("UPDATE"),
    DELETE("delete"),
    ;

    SubwayExceptionPurpose(String label) {
        this.label = label;
    }

    private String label;

    public String label() {
        return this.label;
    }

}
