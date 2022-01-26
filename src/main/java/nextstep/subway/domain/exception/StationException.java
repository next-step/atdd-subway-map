package nextstep.subway.domain.exception;

import nextstep.subway.domain.Station;

public class StationException extends RuntimeException {

    private StationException(String message) {
        super(message);
    }

    public static class Duplicated extends StationException {
        public Duplicated(Station station) {
            super("Already exists station; name=" + station.getName());
        }
    }

    public static class NotFound extends StationException {
        public NotFound(Long id) {
            super("Not found station; id=" + id);
        }
    }
}
