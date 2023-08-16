// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.wrist;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.util.scheduling.LifecycleSubsystem;
import frc.robot.util.scheduling.SubsystemPriority;

public class WristSubsystem extends LifecycleSubsystem {
  private double wristTolerance = 1;
  private double goalAngle = 0;
  private boolean zeroed = false;
  private boolean active = false;
  private TalonFX motor;
  private PositionDutyCycle controlRequest = new PositionDutyCycle(0);

  public WristSubsystem(TalonFX motor) {
    super(SubsystemPriority.WRIST);

    this.motor = motor;

    TalonFXConfiguration motorConfig = new TalonFXConfiguration();

    // Set neutral mode to brake (instead of coast)
    motorConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    // Set current limits
    motorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    motorConfig.CurrentLimits.SupplyCurrentLimit = 25;
    // Set the gearing of the motor
    motorConfig.Feedback.SensorToMechanismRatio = 25.0 * 2.0;
    // Set our PID constants
    motorConfig.Slot0.kP = 4;
    motorConfig.Slot0.kI = 0;
    motorConfig.Slot0.kD = 0;

    motor.getConfigurator().apply(motorConfig);
  }

  @Override
  public void enabledPeriodic() {
    if (active && zeroed) {
      motor.setControl(controlRequest.withPosition(goalAngle));
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
          setPosition(angle);
          active = true;
        })
        .until(() -> atAngle(angle));
  }

  public void setPosition(double angle) {
    goalAngle = angle;
  }

  public Command getPositionSequenceCommand() {
    return setPositionCommand(10).andThen(setPositionCommand(50)).andThen(setPositionCommand(10));
  }

  private boolean atAngle(double angle) {
    return Math.abs(getWristAngle() - angle) < wristTolerance;
  }

  private double getWristAngle() {
    StatusSignal<Double> wristRotations = motor.getPosition();
    return wristRotations.getValue() / 360.0;
  }
}
