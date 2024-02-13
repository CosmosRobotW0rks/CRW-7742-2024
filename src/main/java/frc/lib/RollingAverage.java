package frc.lib;

import java.util.ArrayList;
import java.util.Date;

public class RollingAverage {
    private class AverageEntry {
        public double value;
        public double time;
    }

    private ArrayList<AverageEntry> samples = new ArrayList<>();
    private double average_time;

    public RollingAverage(double average_time) {
        this.average_time = average_time;
    }

    public void AddSample(double value) {
        AverageEntry e = new AverageEntry();
        e.value = value;
        e.time = ((double) new Date().getTime() / 1000.0);
        samples.add(e);
    }

    public double GetAverage() {
        UpdateSamples();
        double average = 0;
        for (AverageEntry sample : samples)
            average += sample.value;

        return average / samples.size();
    }

    private void UpdateSamples() {
        double time = ((double) new Date().getTime() / 1000.0);
        ArrayList<AverageEntry> toRemove = new ArrayList<>();
        for (AverageEntry sample : samples) {
            if (time - sample.time > average_time)
                toRemove.add(sample);
        }

        for (AverageEntry sample : toRemove)
            samples.remove(sample);
    }
}
