package com.arrowpath;

public class arrowpathsettings {
    public enum Mode {
        SOLO, MULTI, ALL
    }

    private boolean showPath = true;
    private Mode currentMode = Mode.SOLO;

    public boolean isShowPathEnabled() {
        return showPath;
    }

    public void toggleShowPath() {
        showPath = !showPath;
    }

    public void setShowPath(boolean value) {
        this.showPath = value;
    }

    public Mode getMode() {
        return currentMode;
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
    }
}
