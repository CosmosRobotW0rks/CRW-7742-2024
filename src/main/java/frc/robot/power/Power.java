package frc.robot.power;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;

public class Power {
    public PowerDistribution pdp = new PowerDistribution(0, ModuleType.kCTRE);

    public double GetCurrent(int ch){
        return pdp.getCurrent(ch);
    }
}
