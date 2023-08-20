package frc.robot.intake;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.util.scheduling.LifecycleSubsystem;
import frc.robot.util.scheduling.SubsystemPriority;

public class IntakeSubsystem extends LifecycleSubsystem{
  private TalonFX motor;
  private DutyCycleOut controlRequest = new DutyCycleOut(0);
  private IntakeState goalState = IntakeState.STOPPED;
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

      @Override
      public void enabledPeriodic() {
        if (goalState == IntakeState.OUTTAKE_CONE){
          motor.setControl(controlRequest.withOutput(0.75));
        }
        else if (goalState == IntakeState.INTAKE_CONE){
          motor.setControl(controlRequest.withOutput(-1));

        }
        else if (goalState == IntakeState.OUTTAKE_CUBE){
          motor.setControl(controlRequest.withOutput(0.6));

        }
        else if (goalState == IntakeState.INTAKE_CUBE){
          motor.setControl(controlRequest.withOutput(-0.5));

        }
        else if (goalState == IntakeState.STOPPED){
          motor.setControl(controlRequest.withOutput(0));
//stopped
        }
        else if (hasCone()){
          motor.setControl(controlRequest.withOutput(-0.1));
        }
        else if (hasCube()){
          motor.setControl(controlRequest.withOutput(0.15));
        }
      }

      public void setState(IntakeState myState){

        goalState = myState;
      }
      public boolean hasCone () {
        if (motor.getReverseLimit().getValue().value == 0){
          return false;
        }
        return true;

      }
      public boolean hasCube () {
        if (motor.getForwardLimit().getValue().value == 0){
          return false;
        }
        return true;

      }
      public Command setIntakeStateCommand(IntakeState state){

        if (state== IntakeState.INTAKE_CONE){
          return run(
        () -> {
          goalState = IntakeState.INTAKE_CONE;
        })
        .until(()->hasCone());
        }
        else if (state== IntakeState.INTAKE_CUBE){
          return run(
        () -> {
          goalState = IntakeState.INTAKE_CUBE;
        })
        .until(()->hasCube());
        }
        else if (state== IntakeState.OUTTAKE_CONE){
          return run(
        () -> {
          goalState = IntakeState.OUTTAKE_CONE;
        })
        .until(()->hasCone());
        }
        else if (state== IntakeState.OUTTAKE_CUBE){
          return run(
        () -> {
          goalState = IntakeState.OUTTAKE_CUBE;
        })
        .until(()->hasCube());
        }
        else {
          return run(
        () -> {
          goalState = IntakeState.STOPPED;
        });
        }
      }

  }
