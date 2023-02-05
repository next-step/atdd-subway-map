package subway.common;

public enum errorMsgEnum {
    ERROR_NO_FOUND_LINE ("LINE","do not found line by id"),
    ERROR_UPSTATION_INVAILD_LINE ("LINE", "upStation's not equal (last down station)"),
    ERROR_DOWNSTATION_INVAILD_LINE ("LINE", "downStation's already exists"),
    ERROR_DELETE_SECTION_COUNT_LINE ("LINE", "line have only one section"),
    ERROR_DELETE_SECTION_NO_LAST_SECTION_LINE ("LINE", "section's not last section"),
    ERROR_NO_FOUND_SECTION ("SECTION", "do not found section by id");

    String code = "";
    String msg = "";

    errorMsgEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
