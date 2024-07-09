package subway;

public enum ErrorCode {
    BAD_REQUEST(400, "잘못된 요청입니다."),
    MISSING_ID(400, "해당 id 로 data 를 찾을 수 없습니다.");
    private int statusCode;
    private String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

}
