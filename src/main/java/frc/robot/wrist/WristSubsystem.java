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
    // if the wrist is zeroed and active, go to the goal position. Otherwiser, stop the wrist.
    if (zeroed == true && active == true) {
      // go to goal angle
      if (getWristAngle() > goalAngle - wristTolerance) {
        motor.setControl(controlRequest.withOutput(-1));
      } else if (getWristAngle() < goalAngle + wristTolerance) {
        motor.setControl(controlRequest.withOutput(1));
      } else {
        motor.disable();
      }
    } else {
      // disable motor
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

  private double getWristAngle() {
    StatusSignal<Double> wristMotorRotations = motor.getRotorPosition();
    return wristMotorRotations.getValue() / 50.0 * 360.0;
  }
}
