package subway.subway.application.in.command;

import subway.subway.domain.SubwayLine;

public class SubwayLineUpdateCommand {

    private SubwayLine.Id id;
    private UpdateContents updateContents;

    public SubwayLineUpdateCommand(SubwayLine.Id id, UpdateContents updateContents) {
        this.id = id;
        this.updateContents = updateContents;
    }

    private SubwayLineUpdateCommand() {
    }

    public SubwayLine.Id getId() {
        return id;
    }

    public String getName() {
        return updateContents.getName();
    }

    public String getColor() {
        return updateContents.getColor();
    }

    public static class UpdateContents {
        private String name;
        private String color;

        public UpdateContents(String name, String color) {
            this.name = name;
            this.color = color;
        }

        private UpdateContents() {
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }
    }

}
