// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.intake.IntakeState;
import frc.robot.intake.IntakeSubsystem;
import frc.robot.util.scheduling.LifecycleSubsystemManager;
import frc.robot.wrist.WristSubsystem;


public class Robot extends LoggedRobot {
  private WristSubsystem wrist = new WristSubsystem(new TalonFX(16));
  private CommandXboxController controller = new CommandXboxController(0);
  private IntakeSubsystem intake = new IntakeSubsystem(new TalonFX(17));

  public Robot() {
    Logger.getInstance().addDataReceiver(new NT4Publisher());
    Logger.getInstance().start();
    LifecycleSubsystemManager.getInstance().ready();

    controller.a().onTrue(wrist.getDisabledCommand().alongWith(intake.setIntakeStateCommand(IntakeState.STOPPED)).alongWith(wrist.setPositionCommand(10)));
    controller.b().onTrue(wrist.getZeroCommand());
    controller.x().onTrue(wrist.setPositionCommand(10));
    controller.y().onTrue(wrist.setPositionCommand(50));
    controller.rightTrigger().onTrue(wrist.setPositionCommand(40).andThen(intake.setIntakeStateCommand(IntakeState.OUTTAKE_CUBE)).andThen(wrist.setPositionCommand(10))) ;
    controller.rightBumper().onTrue(wrist.setPositionCommand(40).andThen(intake.setIntakeStateCommand(IntakeState.INTAKE_CUBE)).andThen(wrist.setPositionCommand(10))) ;
    controller.leftTrigger().onTrue(wrist.setPositionCommand(40).andThen(intake.setIntakeStateCommand(IntakeState.OUTTAKE_CONE)).andThen(wrist.setPositionCommand(10))) ;
    controller.leftBumper().onTrue(wrist.setPositionCommand(40).andThen(intake.setIntakeStateCommand(IntakeState.INTAKE_CONE)).andThen(wrist.setPositionCommand(10))) ;


  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }
}
