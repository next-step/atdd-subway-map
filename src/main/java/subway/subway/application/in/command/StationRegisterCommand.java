package subway.subway.application.in.command;

public class StationRegisterCommand {
    private final String name;

    public StationRegisterCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
