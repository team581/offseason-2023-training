package frc.robot.wrist;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.util.scheduling.LifecycleSubsystem;
import frc.robot.util.scheduling.SubsystemPriority;

public class WristSubsystem extends LifecycleSubsystem{
  private double wristTolerance = 1;
  private double goalAngle = 0;
  // Whether the motor has had its sensor set to 0
  private boolean zeroed = false;
  // Whether the wrist is active - it should stop if this is false
  private boolean active = false;
  // Define a field for the motor, but leave it blank for now
  private TalonFX motor;
  private VoltageOut controlRequest = new VoltageOut(0);

  // When we do new WristSubsystem, we must pass in the motor
  public WristSubsystem(TalonFX motor) {
  // This lets us customize the order that subsystem periodic methods are called
  super(SubsystemPriority.WRIST);

  this.motor = motor;
  }

  @Override
  public void enabledPeriodic() {
    if (!active || !zeroed) {
      motor.disable();
    } else {
      if (getWristAngle() > goalAngle - wristTolerance) {
        motor.setControl(controlRequest.withOutput(-1));
      } else if (getWristAngle() < goalAngle + wristTolerance) {
        motor.setControl(controlRequest.withOutput(1));
      } else {
        motor.setControl(controlRequest.withOutput(0));
      }
    }



    // If the wrist is zeroed and active, go to the goal position
    // Otherwise, stop the wrist
  }
  private double getWristAngle() {
    StatusSignal<Double> wristMotorRotations = motor.getRotorPosition();
    return wristMotorRotations.getValue() / 50.0 * 360.0;
  }


  public Command getZeroCommand() {
    return runOnce(
      () -> {
        // Zero the wrist
        motor.setRotorPosition(0);
        // Set the wrist as zeroed
        zeroed=true;
      });
  }

  public Command getDisableCommand() {
    return runOnce(
      () -> {
        // Set the wrist as not active
        active=false;
        // In enabledPeriodic(), stop the motor if the wrist is not active
      });
  }

  public Command setPositionCommand(double angle) {
    return runOnce(
      () -> {
        goalAngle=angle;
        active=true;
        // Set the goal angle to the value provided + set the wrist as active
        // IN enabledPeriodic(), go to the goal angle
      });
  }
}
