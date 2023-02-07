package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.exception.CustomException;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createStation(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> showStation(@PathVariable Long id, @RequestBody LineRequest lineRequest) throws CustomException {
        LineResponse line = lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().body(line);
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showStations() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> showStation(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLineById(id));
    }

    @DeleteMapping("/lines")
    public ResponseEntity<Void> deleteStation() {
        lineService.deleteLines();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<ErrorDto> handleNoSuchElementFoundException(CustomException exception) {
//        ErrorDto errorDto = new ErrorDto(exception.getErrorCode().getStatus(), exception.getErrorCode().getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
//    }


//    @ExceptionHandler(NoSuchElementFoundException.class)
//    public ResponseEntity<ErrorResponse> handleItemNotFoundException(NoSuchElementFoundException exception) {
//        ...
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//        ...
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception exception) {
//        ...
//    }
}
