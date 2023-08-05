// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Wrist;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.util.scheduling.LifecycleSubsystem;
import frc.robot.util.scheduling.SubsystemPriority;

public class WristSubsystem extends LifecycleSubsystem {

  private double wristTolerance = 1;
  private double goalAngle = 0;
  // Wether the motor has had its sensor set to 0

  private boolean zeroed = false;
  // Wether the wrist is active - it should stop if this is false
  private boolean active = false;
  // Define a field for the motor, but leave it blank for now;
  private TalonFX motor;
  private VoltageOut controlRequest = new VoltageOut(0);

  public WristSubsystem(TalonFX motor) {
    super(SubsystemPriority.WRIST);
    this.motor = motor;
  }

  @Override
  public void enabledPeriodic() {
    // If the wrist is zeroed and active, go to the goal positon
    // Otherwise, stop the wrist

    if (zeroed == true && active == true) {
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

  public Command getDisabledCommand() {
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
