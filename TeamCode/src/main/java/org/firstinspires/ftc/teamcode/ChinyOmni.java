package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.ChudnovskyPi.calculatePi;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.math.BigDecimal;

@TeleOp(name = "chinyOmni", group = "Linear OpMode")
public class ChinyOmni extends LinearOpMode {

  // pi
  public BigDecimal pi = calculatePi(1000);

  // Declare OpMode members for each of the 4 motors.
  private ElapsedTime runtime = new ElapsedTime();
  private DcMotor leftFrontDrive = null;
  private DcMotorSimple leftBackDrive = null;
  private DcMotor rightFrontDrive = null;
  private DcMotorSimple rightBackDrive = null;

  // private DcMotor arm1 = null;
  // private DcMotor arm2 = null;

  @Override
  public void runOpMode() {

    leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFront");
    leftBackDrive = hardwareMap.get(DcMotorSimple.class, "left_back_drive");
    rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFront");
    rightBackDrive = hardwareMap.get(DcMotorSimple.class, "right_back_drive");
    // arm1 = hardwareMap.get(DcMotor.class, "arm1");
    // arm2 = hardwareMap.get(DcMotor.class, "arm2");

    leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
    leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
    rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);

    // Wait for the game to start (driver presses START)
    telemetry.addData("Status", "Initialized");
    telemetry.update();

    waitForStart();
    runtime.reset();
    // axial = front and back
    // lateral = crab
    // yaw = turning
    // run until the end of the match (driver presses STOP)
    while (opModeIsActive()) {
      double max;

      // BUG: there wont mover properly
      // POV Mode uses left joystick to go forward & strafe, and right joystick to
      // rotate.
      double axial = -gamepad1.left_stick_y; // Note: pushing stick forward gives negative value
      double lateral = gamepad1.right_stick_x;
      double yaw = gamepad1.left_stick_x;

      // Combine the joystick requests for each axis-motion to determine each wheel's
      // power.
      // Set up a variable for each drive wheel to save the power level for telemetry.
      // BUG: these wont move properly
      double leftFrontPower = axial + lateral + yaw;
      double rightFrontPower = axial - lateral - yaw;
      double leftBackPower = axial - lateral + yaw;
      double rightBackPower = axial + lateral - yaw;

      // Normalize the values so no wheel power exceeds 100%
      // This ensures that the robot maintains the desired motion.
      max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
      max = Math.max(max, Math.abs(leftBackPower));
      max = Math.max(max, Math.abs(rightBackPower));

      if (max > 1.0) {
        leftFrontPower /= max;
        rightFrontPower /= max;
        leftBackPower /= max;
        rightBackPower /= max;
      }

      /*
       * leftFrontPower = gamepad1.x ? 1.0 : 0.0; // X gamepad
       * leftBackPower = gamepad1.a ? 1.0 : 0.0; // A gamepad
       * rightFrontPower = gamepad1.y ? 1.0 : 0.0; // Y gamepad
       * rightBackPower = gamepad1.b ? 1.0 : 0.0; // B gamepad
       */

      // Send calculated power to wheels
      // this is just a test
      leftFrontDrive.setPower(leftFrontPower);
      rightFrontDrive.setPower(rightFrontPower);
      leftBackDrive.setPower(leftBackPower);
      rightBackDrive.setPower(rightBackPower);

      // TODO: move arm
      /*
       * if (gamepad1.y) {
       * telemetry.addData("Angle", arm.getCurrentPosition());
       * arm.setTargetPosition(arm.getCurrentPosition() + 560/4);
       * arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       * arm.setPower(0.5);
       * while (arm.isBusy()) {
       * // Optionally add telemetry or other feedback
       * telemetry.addData("Position", arm.getCurrentPosition());
       * }
       * arm.setPower(0);
       * }
       */

      // TODO: Move claw

      // Show the elapsed game time and wheel power.
      telemetry.addData("Status", "Run Time: " + runtime.toString());
      telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
      telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
      telemetry.addData("pi:", pi.toString());
      telemetry.update();
    }
  }
}
