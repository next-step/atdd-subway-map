package nextstep.subway.common.error;

public class ErrorResponse {

    private final String code;
    private final String desc;

    private ErrorResponse() {
        this.code = null;
        this.desc = null;
    }

    public ErrorResponse(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
