package subway.common.exception;


import lombok.Getter;

@Getter
public class SubwayException extends RuntimeException {

    private SubwayExceptionPurpose purpose;
    private String location;
    private String message;

    public SubwayException(SubwayExceptionPurpose purpose, String location, String message) {
        this.purpose = purpose;
        this.location = location;
        this.message = message;
    }

}