package subway;

public enum ErrorCode {
    BAD_REQUEST(400, "잘못된 요청입니다.");
    private int statusCode;
    private String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

}
