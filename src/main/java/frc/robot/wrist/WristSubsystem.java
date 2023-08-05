package frc.robot.wrist;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;

import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.StatusSignal;

import frc.robot.util.scheduling.LifecycleSubsystem;
import frc.robot.util.scheduling.SubsystemPriority;

public class WristSubsystem extends LifecycleSubsystem{

  private double wristTolerance = 3;
  private double goalAngle = 0;

  private boolean zeroed = false;

  private boolean active = false ;

  private TalonFX wristMotor;
  private VoltageOut controlRequest = new VoltageOut(0);

  public WristSubsystem(TalonFX motor) {
    super(SubsystemPriority.WRIST);
    this.wristMotor = motor;
  }


  @Override
  public void enabledPeriodic() {
    if (zeroed == true && active == true) {
      if (getWristAngle() > goalAngle - wristTolerance) {
        wristMotor.setControl(controlRequest.withOutput(-1));
      } else if (getWristAngle() < goalAngle + wristTolerance) {
        wristMotor.setControl(controlRequest.withOutput(1));
      } else {
        wristMotor.setControl(controlRequest.withOutput(0));
      }
    } else {
      wristMotor.disable();
    }
  }

  private double getWristAngle() {
    StatusSignal<Double> wristMotorRotations = wristMotor.getRotorPosition();
    return wristMotorRotations.getValue() / 50.0 * 360.0;
  }

  public Command getZeroCommand() {
    return runOnce(
      () -> {
        wristMotor.setRotorPosition(0);
        zeroed = true;
      });
  }

  public Command getDisableCommand() {
    return runOnce(
      () -> {
        active = false;
      });
    }

    public Command setPositionCommand(double angle) {
      return runOnce(
      () -> {
        active = true;
        goalAngle = angle;
      });
  }
}
