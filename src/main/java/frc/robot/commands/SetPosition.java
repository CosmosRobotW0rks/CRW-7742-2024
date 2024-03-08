package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.SquareUp.Position;
import frc.robot.note_manipulation.SetMode;

public class SetPosition extends Command {
    SquareUp square_up;
    Position position;
    public SetPosition(SquareUp square_up, Position position){
        this.square_up = square_up;
        this.position = position;
    }

    @Override
    public void initialize() {
        square_up.SetPosition(position);
        System.out.println("A");
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
