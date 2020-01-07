package com.spikes2212.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinedPIDLoop implements PIDLoop {

    private List<PIDLoop> pidLoops;

    public JoinedPIDLoop(PIDLoop... pidLoops) {
        this.pidLoops = new ArrayList<>();
        this.pidLoops.addAll(Arrays.asList(pidLoops));
    }

    @Override
    public void enable() {
        for (PIDLoop pidLoop : pidLoops) {
            pidLoop.enable();
        }
    }

    @Override
    public void disable() {
        for (PIDLoop pidLoop : pidLoops) {
            pidLoop.disable();
        }
    }

    @Override
    public void update() {
        for (PIDLoop pidLoop : pidLoops) {
            pidLoop.update();
        }
    }

    @Override
    public boolean onTarget() {
        for (PIDLoop pidLoop : pidLoops) {
            if (!pidLoop.onTarget())
                return false;
        }
        return true;
    }
}
