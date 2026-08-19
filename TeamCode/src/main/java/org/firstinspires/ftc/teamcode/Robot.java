/*
 * FTC Team 3123 - BioBUZZ
 * File: Robot.java
 * Author: FTC Team 3123
 *
 * Description:
 * Master robot class. Owns and initializes subsystems so OpModes don't touch
 * the hardware map directly. Add new subsystems (Arm, Intake, etc.) under
 * the subsystems package as they are implemented.
 *
 * Public members and methods:
 *  - public final Drivetrain drivetrain
 *  - public Robot(HardwareMap hardwareMap)
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

/*
 * Master robot class. Owns and initializes every subsystem so OpModes
 * don't touch the hardware map directly - they just build a Robot and
 * call methods on its subsystems.
 *
 * Add new subsystems (Arm, Intake, etc.) here as they're built, under
 * the subsystems package.
 */
public class Robot {

    public final Drivetrain drivetrain;

    public Robot(HardwareMap hardwareMap) {
        drivetrain = new Drivetrain(hardwareMap);
    }
}
