/*
 * FTC Team 3123 - BioBUZZ
 * File: Drivetrain.java
 * Author: FTC Team 3123
 *
 * Description:
 * Drivetrain subsystem for a 4-motor mecanum drive. Encapsulates motor
 * initialization, wheel direction setup, drive math, and compact telemetry.
 *
 * Public methods:
 *  - Drivetrain(HardwareMap hardwareMap)
 *  - Drivetrain(HardwareMap hardwareMap, Telemetry telemetry)
 *  - void drive(double axial, double lateral, double yaw)
 *  - void stop()
 *  - void updateTelemetry()
 *  - void setTelemetry(Telemetry telemetry)
 */
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Locale;

/**
 * Drivetrain subsystem for a 4-motor mecanum drive.
 *
 * This class encapsulates the low-level motor objects and the math to convert
 * joystick inputs (axial/lateral/yaw) into individual wheel powers for a
 * mecanum drivetrain. It also provides compact telemetry helpers to display
 * motor powers and control inputs on the driver station.
 *
 * IMPORTANT: The motor names below must match the names configured on the
 * Driver Station / Robot Controller: front_left_drive, front_right_drive,
 * back_left_drive, back_right_drive.
 */
public class Drivetrain {

    // Motor objects for the four mecanum wheels. Marked final because the
    // hardware references are set once during construction and never changed.
    private final DcMotor frontLeftDrive;
    private final DcMotor frontRightDrive;
    private final DcMotor backLeftDrive;
    private final DcMotor backRightDrive;

    // Optional telemetry reference for driver-station output. May be null when
    // using the subsystem in non-telemetry contexts (e.g., autonomous tests).
    private Telemetry telemetry;

    // Last control inputs received via drive() — stored so telemetry can show
    // what the driver commanded even after the motor powers are applied.
    private double lastAxial, lastLateral, lastYaw;

    public Drivetrain(HardwareMap hardwareMap) {
        this(hardwareMap, null);
    }

    public Drivetrain(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

        // Direct-drive default: left side reversed so positive power drives forward.
        // If a wheel spins backward when you push the left stick forward, flip it here.
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    /**
     * Robot-centric POV drive.
     *
     * Converts the 3 control axes (axial, lateral, yaw) into individual motor
     * powers for a mecanum drive and normalizes them so no value exceeds 1.0.
     *
     * Explanation of math:
     * - axial moves the robot forward/backwards
     * - lateral strafes left/right
     * - yaw rotates the robot about its center
     * The wheel power equations combine these to produce the appropriate vector
     * for each wheel.
     *
     * Normalization step: if any raw wheel power is > 1 in magnitude we divide
     * all powers by the maximum magnitude so the relative proportions remain the
     * same while ensuring motor power stays within [-1, 1].
     *
     * @param axial   forward/backward, positive = forward
     * @param lateral strafe left/right, positive = right
     * @param yaw     rotate, positive = clockwise
     */
    public void drive(double axial, double lateral, double yaw) {
        // Save the most recent control inputs so telemetry can display them.
        lastAxial = axial;
        lastLateral = lateral;
        lastYaw = yaw;

        // Compute raw wheel powers (may be outside [-1,1]).
        double frontLeftPower = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower = axial - lateral + yaw;
        double backRightPower = axial + lateral - yaw;

        // Find the largest absolute value among the powers and ensure a minimum
        // of 1.0 so we only scale down when necessary.
        double max = Math.max(1.0, Math.abs(frontLeftPower));
        max = Math.max(max, Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        // Apply scaled powers to motors. Dividing by max ensures we never exceed
        // the allowed motor power range while preserving the commanded vector.
        frontLeftDrive.setPower(frontLeftPower / max);
        frontRightDrive.setPower(frontRightPower / max);
        backLeftDrive.setPower(backLeftPower / max);
        backRightDrive.setPower(backRightPower / max);
    }

    public void stop() {
        drive(0, 0, 0);
    }

    /**
     * Updates telemetry with compact drivetrain status.
     * Packs motor powers and control inputs on single lines to conserve screen space.
     */
    public void updateTelemetry() {
        if (telemetry == null) return;

        // Format motor powers with 2 decimals and pack on one line
        String motors = String.format(Locale.US, "FL:%.2f FR:%.2f BL:%.2f BR:%.2f",
                frontLeftDrive.getPower(),
                frontRightDrive.getPower(),
                backLeftDrive.getPower(),
                backRightDrive.getPower());
        telemetry.addData("Motors", motors);

        // Pack input commands on one line
        String inputs = String.format(Locale.US, "Axial:%.2f Lat:%.2f Yaw:%.2f",
                lastAxial, lastLateral, lastYaw);
        telemetry.addData("Input", inputs);
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
}
