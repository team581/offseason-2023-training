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
  private TalonFX motor;
  private IntakeState goalState = IntakeState.STOPPED;
  private DutyCycleOut controlRequest = new DutyCycleOut(0);

  public IntakeSubsystem(TalonFX motor) {
    super(SubsystemPriority.INTAKE);

    this.motor = motor;

    TalonFXConfiguration motorConfig = new TalonFXConfiguration();

    motorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    motorConfig.CurrentLimits.SupplyCurrentLimit = 15;
    motorConfig.CurrentLimits.SupplyCurrentThreshold = 25;
    motorConfig.CurrentLimits.SupplyTimeThreshold = 0.2;
    motorConfig.HardwareLimitSwitch.ForwardLimitEnable = false;
    motorConfig.HardwareLimitSwitch.ReverseLimitEnable = false;

    motor.getConfigurator().apply(motorConfig);
  }

  public void setState(IntakeState state) {
    goalState = state;
  }

  public boolean intakedCube() {
    if (motor.getForwardLimit().getValue().value == 1) {
      return true;
    } else {
      return false;
    }
  }

  public boolean intakedCone() {
    if (motor.getReverseLimit().getValue().value == 1) {
      return true;
    } else {
      return false;
    }
  }

  public Command setIntakeStateCommand(IntakeState state) {
  if (state == IntakeState.INTAKE_CONE) {
      return run(() -> {
                setState(IntakeState.INTAKE_CONE);
          })
          .until(() -> intakedCone());
    } else if (state == IntakeState.INTAKE_CUBE) {
      return run(() -> {
        setState(IntakeState.INTAKE_CUBE);
    })
    .until(() -> intakedCube());
    } else if (state == IntakeState.OUTTAKE_CONE) {
      return run(() -> {
        setState(IntakeState.OUTTAKE_CONE);
      })
      .until(() -> !intakedCone());
    } else if (state == IntakeState.OUTTAKE_CUBE) {
      return run(() -> {
        setState(IntakeState.OUTTAKE_CUBE);
      })
      .until(() -> !intakedCube());
    } else {
      return run(() -> {
        setState(IntakeState.STOPPED);
      });
    }

  }

  @Override
  public void enabledPeriodic() {
    if (goalState == IntakeState.STOPPED) {
      motor.disable();
    } else if (goalState == IntakeState.INTAKE_CUBE) {
      motor.setControl(controlRequest.withOutput(0.75));
    } else if (goalState == IntakeState.OUTTAKE_CUBE) {
      motor.setControl(controlRequest.withOutput(-0.5));
    } else if (goalState == IntakeState.INTAKE_CONE) {
      motor.setControl(controlRequest.withOutput(-1));
    } else if (goalState == IntakeState.OUTTAKE_CONE) {
      motor.setControl(controlRequest.withOutput(0.6));
    } else if (intakedCube() == true) {
      motor.setControl(controlRequest.withOutput(0.15));
    } else if (intakedCone() == true) {
      motor.setControl(controlRequest.withOutput(-0.1));
    }
  }
}
