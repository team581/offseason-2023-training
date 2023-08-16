// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.util.scheduling.LifecycleSubsystem;
import frc.robot.util.scheduling.SubsystemPriority;

public class IntakeSubsystem extends LifecycleSubsystem {
  private IntakeState intakeState;
  private TalonFX motor;
  private DutyCycleOut controlRequest = new DutyCycleOut(0);

  public IntakeSubsystem(TalonFX motor) {

    super(SubsystemPriority.INTAKE);
    this.motor = motor;
    TalonFXConfiguration motorConfig = new TalonFXConfiguration();
    motorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    motorConfig.CurrentLimits.SupplyCurrentLimit = 15;
    motorConfig.CurrentLimits.SupplyCurrentThreshold = 25;
    motorConfig.CurrentLimits.SupplyTimeThreshold = 12;

    motorConfig.HardwareLimitSwitch.ForwardLimitEnable = false;
    motorConfig.HardwareLimitSwitch.ReverseLimitEnable = false;

    motor.getConfigurator().apply(motorConfig);
  }

  @Override
  public void enabledPeriodic() {
    if (intakeState == IntakeState.OUTTAKE_CUBE) {
      motor.setControl(controlRequest.withOutput(-0.5));
    } else if (intakeState == IntakeState.OUTTAKE_CONE) {
      motor.setControl(controlRequest.withOutput(0.6));
    } else if (holdingCone()) {
      motor.setControl(controlRequest.withOutput(0.15));
    } else if (holdingCube()) {
      motor.setControl(controlRequest.withOutput(-0.1));
    } else if (intakeState == IntakeState.STOPPED) {
      motor.disable();
    } else if (intakeState == IntakeState.INTAKE_CUBE) {
      motor.setControl(controlRequest.withOutput(0.75));
    } else if (intakeState == IntakeState.INTAKE_CONE) {
      motor.setControl(controlRequest.withOutput(-1));
    }
  }

  public void setState(IntakeState intakeState) {}

  public boolean holdingCube() {
    return motor.getForwardLimit().getValue().value == 1;
  }

  public boolean holdingCone() {
    return motor.getReverseLimit().getValue().value == 1;
  }

  public Command setStateCommand(IntakeState intakeState) {
    Command command = run(() -> setState(intakeState));

    if (intakeState == IntakeState.INTAKE_CONE) {
      command = command.until(() -> holdingCone());
    } else if (intakeState == IntakeState.INTAKE_CUBE) {
      command = command.until(() -> holdingCube());
    } else if (intakeState == IntakeState.OUTTAKE_CUBE) {
      command = command.until(() -> holdingCube() == false);
    } else if (intakeState == IntakeState.OUTTAKE_CONE) {
      command = command.until(() -> holdingCone() == false);
    }
    return command;
  }
}
