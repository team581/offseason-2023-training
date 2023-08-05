package frc.robot.wrist;

import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.util.scheduling.LifecycleSubsystem;
import frc.robot.util.scheduling.SubsystemPriority;

public class WristSubsystem extends LifecycleSubsystem {
  private double wristTolerance = 1;
  private double goalAngle = 0;
  private boolean zeroed = false;
  private boolean active = false;
  private TalonFX motor;
  private VoltageOut controlRequest = new VoltageOut(0);

  public WristSubsystem(TalonFX motor) {
    super(SubsystemPriority.WRIST);

    this.motor = motor;
  }

  @Override
  public void enabledPeriodic() {
    if(active && zeroed) {
      if (getWristAngle() > goalAngle - wristTolerance) {
        motor.setControl(controlRequest.withOutput(-1));
      } else if (getWristAngle() < goalAngle + wristTolerance) {
        motor.setControl(controlRequest.withOutput(1));
      } else {
        motor.setControl(controlRequest.withOutput(0));
      }
    } else {
      motor.setControl(controlRequest.withOutput(0));
    }
  }

  private double getWristAngle() {
    StatusSignal<Double> wristMotorRotations = motor.getRotorPosition();
    return wristMotorRotations.getValue() / 50.0 * 360.0;
  }

  public Command getZeroCommand() {
    return runOnce(
      () -> {
      motor.setRotorPosition(0);
      zeroed = true;
    });
  }

  public Command gettDisabledCommand() {
    return runOnce(
      () -> {
        active = false;
      });
  }

  public Command setPositionCommand(double angle) {
    return runOnce(() -> {
      goalAngle = angle;
      active = true;
    });
  }
}