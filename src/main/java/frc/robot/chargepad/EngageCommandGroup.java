package frc.robot.chargepad;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class EngageCommandGroup extends SequentialCommandGroup{
    public EngageCommandGroup(){
        addCommands(new DockCommand(), new BalanceCommand());
    }
}
