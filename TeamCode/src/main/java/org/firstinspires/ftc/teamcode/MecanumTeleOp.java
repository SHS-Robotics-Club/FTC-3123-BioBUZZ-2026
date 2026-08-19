/*
 * FTC Team 3123 - BioBUZZ
 * File: MecanumTeleOp.java
 * Author: FTC Team 3123
 *
 * Description:
 * TeleOp LinearOpMode for a 4-motor mecanum drivetrain. Reads driver gamepad
 * inputs, sends commands to the Robot.drivetrain subsystem, and displays
 * compact telemetry (runtime and drivetrain status).
 *
 * Public methods:
 *  - void runOpMode()
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * Starter TeleOp for a 4-motor mecanum drivetrain. Robot-centric POV control:
 * left stick = drive/strafe, right stick X = rotate.
 *
 * Before driving: push the left stick forward. Any wheel that spins the
 * wrong way needs its setDirection() flipped in Drivetrain.
 */
@TeleOp(name = "BioBUZZ: Mecanum TeleOp", group = "BioBUZZ")
public class MecanumTeleOp extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Create a Robot helper that initializes subsystems (motors, sensors).
        Robot robot = new Robot(hardwareMap);

        // Give the drivetrain a reference to the OpMode telemetry so it can
        // post compact motor/input status lines. This is optional; the
        // Drivetrain constructor supports a null telemetry reference.
        robot.drivetrain.setTelemetry(telemetry);

        // Show an initialization status to the driver station.
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the play button and reset the runtime counter.
        waitForStart();
        runtime.reset();

        // Main TeleOp loop runs until the driver stops the opmode.
        while (opModeIsActive()) {
            // Read gamepad sticks. Note: gamepad Y is typically -1 when pushed
            // forward, so we negate left_stick_y to make 'axial' positive when
            // the driver pushes forward.
            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            // Send the desired motion to the drivetrain subsystem.
            robot.drivetrain.drive(axial, lateral, yaw);

            // Update runtime and drivetrain telemetry together. updateTelemetry()
            // packs multiple values onto single lines to save driver-station
            // screen space.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            robot.drivetrain.updateTelemetry();
            telemetry.update();
        }
    }
}
