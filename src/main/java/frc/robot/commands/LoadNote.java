package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.apriltag.NotePOIs;
import frc.robot.drive.WaypointDriveCommand;
import frc.robot.note_manipulation.SetMode;
import frc.robot.note_manipulation.WaitForMode;
import frc.robot.note_manipulation.WaitNotBusy;
import frc.robot.note_manipulation.NoteSystemController.Mode;

public class LoadNote extends SequentialCommandGroup {
        public LoadNote(RobotContainer c, NotePOIs n) {
                Command before_note = new WaypointDriveCommand(c,
                                n.LoadPre, false, 2, 0.3, 2.5).withTimeout(4);

                Command set_load_mode = new SetMode(c.note_c, Mode.LOAD);

                Command wait_for_load_mode = new WaitNotBusy(c.note_c);

                Command move_over_note = new WaypointDriveCommand(c,
                                n.LoadPost, false, 0.5, 0.2, 3).withTimeout(4);

                Command wait_for_note = new WaitForMode(c.note_c, Mode.WAIT);

                Command wait_for_note_entry = new WaitNotBusy(c.note_c);
                Command wait_for_not_busy = new WaitNotBusy(c.note_c);

                addCommands(before_note, set_load_mode, wait_for_load_mode,
                                move_over_note.raceWith(wait_for_note.andThen(wait_for_note_entry)), wait_for_not_busy);
        }

        public LoadNote(RobotContainer c, NotePOIs n, Pose2d Pre) {
                Command move_pre = new WaypointDriveCommand(c,
                                Pre, false, 6, 1, 4).withTimeout(4);
                Command before_note = new WaypointDriveCommand(c,
                                n.LoadPre, false, 6, 0.6, 4).withTimeout(4);

                Command set_load_mode = new SetMode(c.note_c, Mode.LOAD);

                Command wait_for_load_mode = new WaitNotBusy(c.note_c);

                Command move_over_note = new WaypointDriveCommand(c,
                                n.LoadPost, false, 0.85, 0.2, 3).withTimeout(3);

                Command wait_for_note = new WaitForMode(c.note_c, Mode.WAIT);

                Command wait_for_note_entry = new WaitNotBusy(c.note_c);
                Command wait_for_not_busy = new WaitNotBusy(c.note_c);
                Command set_shoot_mode = new SetMode(c.note_c, Mode.WAIT);

                Command move_back_pre = new WaypointDriveCommand(c,
                                Pre, false, 8, 1, 4).withTimeout(4);

                addCommands(move_pre, before_note, set_load_mode, wait_for_load_mode,
                                move_over_note.raceWith(wait_for_note.andThen(wait_for_note_entry)), wait_for_not_busy, set_shoot_mode,
                                move_back_pre);
        }
}
