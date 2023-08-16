// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.util.scheduling.LifecycleSubsystem;
import frc.robot.util.scheduling.SubsystemPriority;

public class IntakeSubsystem extends LifecycleSubsystem {
  private DutyCycleOut controlRequest = new DutyCycleOut(0);
  private TalonFX motor;
  public IntakeState state;
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
  public void setState(IntakeState intakeState){
    state = intakeState;
  }

  @Override
  public void enabledPeriodic() {

    if(state == IntakeState.INTAKE_CUBE){
      motor.setControl(controlRequest.withOutput(0.75));
    } else if(state == IntakeState.INTAKE_CONE){
      motor.setControl(controlRequest.withOutput(-1));
    } else if(state == IntakeState.OUTTAKE_CONE){
      motor.setControl(controlRequest.withOutput(0.6));
    } else if(state == IntakeState.OUTTAKE_CUBE){
      motor.setControl(controlRequest.withOutput(-0.5));
    } else {
      motor.setControl(controlRequest.withOutput(0));
    }
  }
  // private boolean getForwardLimit(){
  //   // motor.getForwardLimit().getValue().value}
}
