package hu.bme.aut.nightshaderemote.connectivity.commands;

import java.io.OutputStream;

/**
 * A command to set a flag on the server (on|off|toggle).
 *
 * @author Ákos Pap
 */
public class FlagCommand implements Command {
    /**
     * The possible flags.
     */
    public static enum CommandName {
        CONSTELLATION_LINES(("constellationLines")),
        CONSTELLATION_LABELS("constellationLabels"),
        CONSTELLATION_ART("constellationArt"),
        CONSTELLATION_BOUNDARIES("constellationBoundaries"),
        AZIMUTHAL_GRID("azimuthalGrid"),
        EQUATORIAL_GRID("equatorialGrid"),
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

    /**
     * The possible states.
     */
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

    /**
     * The prefix in the URL, that chooses the functionality.
     */
    public static String prefix = "flag";

    protected CommandName commandName;
    protected CommandState commandState;

    /**
     * Constructs a command to set a flag.
     * @param commandName The {@link FlagCommand.CommandName flag} to set.
     * @param commandState The {@link FlagCommand.CommandState state} to set the flag to.
     */
    public FlagCommand(CommandName commandName, CommandState commandState) {
        this.commandName = commandName;
        this.commandState = commandState;
    }

    /** {@inheritDoc} */
    @Override
    public String getPath() {
        return "/" + prefix + "/" + commandName.getPathPart() + "/" + commandState.getPathPart();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPost() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void writePostData(OutputStream out) {
        throw new UnsupportedOperationException("This Command doesn't do any output!");
    }
}
