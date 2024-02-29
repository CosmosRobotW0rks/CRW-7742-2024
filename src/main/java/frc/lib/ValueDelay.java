package frc.lib;

import java.util.ArrayList;
import java.util.Date;

public class ValueDelay {
    private class Entry {
        public double value;
        public double time;
    }

    private ArrayList<Entry> samples = new ArrayList<>();
    private double delay_time;

    public ValueDelay(double delay_time) {
        this.delay_time = delay_time;
    }

    public void AddSample(double value) {
        Entry e = new Entry();
        e.value = value;
        e.time = ((double) new Date().getTime() / 1000.0);
        samples.add(e);
    }

    public double GetValue() {
        UpdateSamples();

        if (samples.size() > 0)
            return samples.get(0).value;
        else
            return 0;
    }

    private void UpdateSamples() {
        double time = ((double) new Date().getTime() / 1000.0);
        ArrayList<Entry> toRemove = new ArrayList<>();
        for (Entry sample : samples) {
            if (time - sample.time > delay_time)
                toRemove.add(sample);
        }

        for (Entry sample : toRemove)
            samples.remove(sample);
    }
}
