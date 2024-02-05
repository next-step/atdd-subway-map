package subway.domain;

public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public static class LineException extends DomainException {

        public LineException(String message) {
            super(message);
        }

        public static LineException NotFoundException() {
            return new LineException("지하철을 찾을 수 없습니다.");
        }

        public static LineException InvalidSectionException() {
            return new LineException("새로운 구간의 상행역은 기존 구간의 하행역과 일치해야 합니다.");
        }

        public static LineException NotRemoveException() {
            return new LineException("노선을 삭제할 수 없습니다.");
        }

        public static LineException AlreadyRegisteredStationException() {
            return new LineException("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.");
        }
    }

    public static class StationException extends DomainException {

        public StationException(String message) {
            super(message);
        }

        public static StationException NotFoundException() {
            return new StationException("지하철역을 찾을 수 없습니다.");
        }

    }
}

