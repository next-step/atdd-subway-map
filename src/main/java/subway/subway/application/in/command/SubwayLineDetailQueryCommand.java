package subway.subway.application.in.command;

import subway.subway.domain.SubwayLine;

public class SubwayLineDetailQueryCommand {
    private final SubwayLine.Id id;

    public SubwayLineDetailQueryCommand(SubwayLine.Id id) {
        this.id = id;
    }

    public SubwayLine.Id getId() {
        return id;
    }
}
