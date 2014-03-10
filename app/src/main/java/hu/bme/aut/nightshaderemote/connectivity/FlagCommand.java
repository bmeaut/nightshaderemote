package hu.bme.aut.nightshaderemote.connectivity;

/**
 * Created by akos on 2014.03.09..
 */
public class FlagCommand implements Command {
    public static enum CommandName {
        CONSTELLATION_LINES(("constellationLines")),
        CONSTELLATION_LABELS("constellationLabels"),
        CONSTELLATION_ART("constellationArt"),
        AZIMUTHAL_GRID("azimuthalGrid"),
        EQUTORIAL_GRID("equatorialGrid"),
        GROUND("ground"),
        CARDINAL_POINTS("cardinalPoints"),
        ATMOSPHERE("atmosphere"),
        BODY_LABELS("bodyLabels"),
        NEBULA_LABELS("nebulaLabels"),
        MOUNT("mount");

        private String uriPart;

        CommandName(String uriPart) {
            this.uriPart = uriPart;
        }

        public String getPathPart() {
            return this.uriPart;
        }
    }

    public static enum CommandState {
        ON("on"),
        OFF("off"),
        TOGGLE("toggle");

        private String uriPart;

        CommandState(String uriPart) {
            this.uriPart = uriPart;
        }

        public String getPathPart() {
            return this.uriPart;
        }
    }

    protected CommandName commandName;
    protected CommandState commandState;

    public FlagCommand(CommandName commandName, CommandState commandState) {
        this.commandName = commandName;
        this.commandState = commandState;
    }

    @Override
    public String getPath() {
        return "/" + commandName.getPathPart() + "/" + commandState.getPathPart();
    }
}
