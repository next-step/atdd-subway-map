package subway.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.CannotDeleteStationFromALineWithOnlyTwoStationsException;
import subway.exception.DownStationOfNewSectionMustNotExistingLineStationException;
import subway.exception.ToBeDeletedStationMustBeLastException;
import subway.exception.UpStationOfNewSectionMustBeDownStationOfExistingLineException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> noSuchElement() {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UpStationOfNewSectionMustBeDownStationOfExistingLineException.class)
    public ResponseEntity<String> upStationOfNewSectionMustBeDownStationOfExistingLineException() {
        return ResponseEntity.badRequest()
                .body("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.");
    }

    @ExceptionHandler(DownStationOfNewSectionMustNotExistingLineStationException.class)
    public ResponseEntity<String> downStationOfNewSectionMustNotExistingLineStationException() {
        return ResponseEntity.badRequest()
                .body("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.");
    }

    @ExceptionHandler(ToBeDeletedStationMustBeLastException.class)
    public ResponseEntity<String> toBeDeletedStationMustBeLastException() {
        return ResponseEntity.badRequest()
                .body("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.");
    }

    @ExceptionHandler(CannotDeleteStationFromALineWithOnlyTwoStationsException.class)
    public ResponseEntity<String> cannotDeleteStationFromALineWithOnlyTwoStationsException() {
        return ResponseEntity.badRequest()
                .body("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.");
    }

}
