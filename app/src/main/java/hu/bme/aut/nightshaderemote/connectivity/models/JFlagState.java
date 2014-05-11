package hu.bme.aut.nightshaderemote.connectivity.models;

import java.io.Serializable;

/**
* @author Ákos Pap
*/
public class JFlagState implements Serializable {

    private boolean constellationLines;
    private boolean constellationLabels;
    private boolean constellationArt;
    private boolean constellationBoundaries;
    private boolean azimuthalGrid;
    private boolean equatorialGrid;
    private boolean ground;
    private boolean cardinalPoints;
    private boolean atmosphere;
    private boolean bodyLabels;
    private boolean nebulaLabels;
    private boolean mount;

    public boolean isConstellationLines() {
        return constellationLines;
    }

    public void setConstellationLines(boolean constellationLines) {
        this.constellationLines = constellationLines;
    }

    public boolean isConstellationLabels() {
        return constellationLabels;
    }

    public void setConstellationLabels(boolean constellationLabels) {
        this.constellationLabels = constellationLabels;
    }

    public boolean isConstellationArt() {
        return constellationArt;
    }

    public void setConstellationArt(boolean constellationArt) {
        this.constellationArt = constellationArt;
    }

    public boolean isConstellationBoundaries() {
        return constellationBoundaries;
    }

    public void setConstellationBoundaries(boolean constellationBoundaries) {
        this.constellationBoundaries = constellationBoundaries;
    }

    public boolean isAzimuthalGrid() {
        return azimuthalGrid;
    }

    public void setAzimuthalGrid(boolean azimuthalGrid) {
        this.azimuthalGrid = azimuthalGrid;
    }

    public boolean isEquatorialGrid() {
        return equatorialGrid;
    }

    public void setEquatorialGrid(boolean equatorialGrid) {
        this.equatorialGrid = equatorialGrid;
    }

    public boolean isGround() {
        return ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public boolean isCardinalPoints() {
        return cardinalPoints;
    }

    public void setCardinalPoints(boolean cardinalPoints) {
        this.cardinalPoints = cardinalPoints;
    }

    public boolean isAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(boolean atmosphere) {
        this.atmosphere = atmosphere;
    }

    public boolean isBodyLabels() {
        return bodyLabels;
    }

    public void setBodyLabels(boolean bodyLabels) {
        this.bodyLabels = bodyLabels;
    }

    public boolean isNebulaLabels() {
        return nebulaLabels;
    }

    public void setNebulaLabels(boolean nebulaLabels) {
        this.nebulaLabels = nebulaLabels;
    }

    public boolean isMount() {
        return mount;
    }

    public void setMount(boolean mount) {
        this.mount = mount;
    }
}
