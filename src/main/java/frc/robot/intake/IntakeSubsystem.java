// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake;

import javax.sound.sampled.Control;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DutyCycle;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.util.scheduling.LifecycleSubsystem;
import frc.robot.util.scheduling.SubsystemPriority;


public class IntakeSubsystem extends LifecycleSubsystem {
  private IntakeState currentState = IntakeState.STOPPED;
  private DutyCycleOut controlRequest = new DutyCycleOut(0);
  private TalonFX motor;

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
    currentState = state;
  }

  @Override
  public void enabledPeriodic(){
    if (currentState == IntakeState.INTAKE_CUBE){
      motor.setControl(controlRequest.withOutput(0.75));
    }
    else if (currentState == IntakeState.OUTTAKE_CUBE){
      motor.setControl(controlRequest.withOutput(-0.5));
    }
    else if (currentState == IntakeState.INTAKE_CONE){
      motor.setControl(controlRequest.withOutput(-1));
    }

  }
}


