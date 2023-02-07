package subway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDto {
    private int status;
    private  String message;
//    public ErrorDto(int status, String message) {
//        this.status = status;
//        this.message = message;
//    }
//
//    public int getStatus() {
//        return this.status;
//    }
//
//    public String getMessage() {
//        return this.message;
//    }
}
