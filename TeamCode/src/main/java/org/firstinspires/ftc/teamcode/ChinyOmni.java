package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "chinyOmni", group = "Linear OpMode")
public class ChinyOmni extends LinearOpMode {

  // pi

  // Declare OpMode members for each of the 4 motors.
  // test
  private ElapsedTime runtime = new ElapsedTime();
  private DcMotor leftFrontDrive = null;
  private DcMotorSimple leftBackDrive = null;
  private DcMotor rightFrontDrive = null;
  private DcMotorSimple rightBackDrive = null;

  private DcMotorSimple arm = null;
  private DcMotorSimple elevator = null;

  private Servo wrist = null;
  public double wristPos = 0.3;

  private Servo leftClaw = null;
  private Servo rightClaw = null;
  public double rightClawPos = 0;
  public double leftClawPos = 0.3;
  public float nothing = 0;

  @Override
  public void runOpMode() {

    leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFront");
    leftBackDrive = hardwareMap.get(DcMotorSimple.class, "leftBack");
    rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFront");
    rightBackDrive = hardwareMap.get(DcMotorSimple.class, "rightBack");
    arm = hardwareMap.get(DcMotorSimple.class, "arm");
    elevator = hardwareMap.get(DcMotorSimple.class, "elevator");
    wrist = hardwareMap.get(Servo.class, "wrist");
    leftClaw = hardwareMap.get(Servo.class, "leftClaw");
    rightClaw = hardwareMap.get(Servo.class, "rightClaw");

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

      // POV Mode uses left joystick to go forward & strafe, and right joystick to
      // rotate.
      double axial = -gamepad1.left_stick_y; // Note: pushing stick forward gives negative value
      double lateral = gamepad1.right_stick_x;
      double yaw = gamepad1.left_stick_x;
      double armPower = 1;
      double elevatorPower = 1;

      // Combine the joystick requests for each axis-motion to determine each wheel's
      // power.
      // Set up a variable for each drive wheel to save the power level for telemetry.
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
      // arm.setPower(armPower);
      // elevator.setPower(elevatorPower);

      // Move arm
      if (gamepad2.left_stick_y > 0) {
        arm.setPower(armPower);
      }
      if (gamepad2.left_stick_y < 0) {
        arm.setPower(-armPower);
      }
      if (gamepad2.left_stick_y == 0) {
        arm.setPower(0);
      }

      // Extend arm
      if (gamepad2.right_stick_y > 0) {
        elevator.setPower(-elevatorPower);
      }
      if (gamepad2.right_stick_y < 0) {
        elevator.setPower(elevatorPower);
      }
      if (gamepad2.right_stick_y == 0) {
        elevator.setPower(0);
      }

      //  Move wrist
      if (gamepad2.dpad_down) {
        // move down
        wristPos += 0.001;
      }
      if (gamepad2.dpad_up) {
        // move up
        wristPos -= 0.001;
      }
      if (gamepad2.y) {
        // wrist up
        wristPos = 0.05;
      } if (gamepad2.a) {
        // wrist down
        wristPos = 0.77;
      }
      if (gamepad2.x) {
        // wrist straight
        wristPos = 0.25;
      }
      wrist.setPosition(wristPos);
      // open full
      if (gamepad2.right_bumper) {
        rightClawPos = 0;
        leftClawPos = 0.3;
      }
      if (gamepad2.left_bumper) {
        rightClawPos = 0.6;
        leftClawPos = 0;
      }


      rightClaw.setPosition(rightClawPos);
      leftClaw.setPosition(leftClawPos);

      // Show the elapsed game time and wheel power.
      telemetry.addData("Servo Position", wristPos);
      telemetry.addData("Right Claw Position", rightClaw.getPosition());
      telemetry.addData("Left Claw Position", leftClaw.getPosition());
      telemetry.addData("Status", "Run Time: " + runtime.toString());
      telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
      telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
      telemetry.update();
    }
  }
}
