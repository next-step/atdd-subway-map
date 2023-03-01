package subway.line;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class LineException {

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public static class DuplicatedStationAddToSectionFailException extends RuntimeException {
    public DuplicatedStationAddToSectionFailException() {
      super("이미 등록된 역을 새로운 하행역으로 설정하실 수 없습니다.");
    }
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public static class InvalidUpstationAppendInSection extends RuntimeException {
    public InvalidUpstationAppendInSection() {
      super("하행역에만 새로운 구간 추가가 가능합니다.");
    }
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public static class ZeroSectionException extends RuntimeException {
    public ZeroSectionException() {
      super("노선의 구간은 한 개 이상 남아야 합니다.");
    }
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public static class MiddleSectionRemoveFailException extends RuntimeException {
    public MiddleSectionRemoveFailException() {
      super("노선의 중간 구간들은 지울 수 없습니다.");
    }
  }

}
