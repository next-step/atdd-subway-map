package nextstep.subway.exception;

public enum LogicError {
    DUPLICATED_NAME_LINE("Line name is duplicated"),
    DUPLICATED_NAME_STATION("Station name is duplicated"),
    NOT_EXIST_LINE("Does not exist line"),
    NOT_EXIST_STATION("Does not exist station");

    private final String errMsg;

    LogicError(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }
}