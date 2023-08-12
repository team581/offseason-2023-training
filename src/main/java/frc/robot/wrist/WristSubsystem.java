// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.wrist;

import com.ctre.phoenix6.StatusSignal;
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
    if (active && zeroed) {
      if (getWristAngle() > goalAngle - wristTolerance) {
        motor.setControl(controlRequest.withOutput(-1));
      } else if (getWristAngle() < goalAngle + wristTolerance) {
        motor.setControl(controlRequest.withOutput(1));
      } else {
        motor.disable();
      }
    } else {
      motor.disable();
    }
  }

  public Command getZeroCommand() {
    return runOnce(
        () -> {
          motor.setRotorPosition(0);
          zeroed = true;
        });
  }

  public Command getDisabledCommand() {
    return runOnce(
        () -> {
          active = false;
        });
  }

  public Command setPositionCommand(double angle) {
    return run(() -> {
          goalAngle = angle;
          active = true;
        })
        .until(() -> atAngle(angle));
  }

  public Command getPositionSequenceCommand() {
    return setPositionCommand(10).andThen(setPositionCommand(50)).andThen(setPositionCommand(10));
  }

  private boolean atAngle(double angle) {
    return Math.abs(getWristAngle() - angle) < wristTolerance;
  }

  private double getWristAngle() {
    StatusSignal<Double> wristMotorRotations = motor.getRotorPosition();
    return wristMotorRotations.getValue() / 50.0 * 360.0;
  }
}
