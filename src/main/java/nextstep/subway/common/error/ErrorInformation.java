package nextstep.subway.common.error;

public class ErrorInformation {

    private String errorMessage;

    private ErrorInformation(){}

    public ErrorInformation(String message){
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
