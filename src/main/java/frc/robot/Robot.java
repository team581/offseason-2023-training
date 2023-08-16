// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.util.scheduling.LifecycleSubsystemManager;
import frc.robot.wrist.WristSubsystem;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;

public class Robot extends LoggedRobot {
  private WristSubsystem wrist = new WristSubsystem(new TalonFX(16));
  private CommandXboxController controller = new CommandXboxController(0);

  public Robot() {
    Logger.getInstance().addDataReceiver(new NT4Publisher());
    Logger.getInstance().start();

    controller.a().onTrue(wrist.getDisabledCommand());
    controller.b().onTrue(wrist.getZeroCommand());
    controller.x().onTrue(wrist.setPositionCommand(10));
    controller.y().onTrue(wrist.setPositionCommand(50));
    controller.rightTrigger().onTrue(wrist.getPositionSequenceCommand());

    LifecycleSubsystemManager.getInstance().ready();

    SmartDashboard.putData(CommandScheduler.getInstance());
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }
}
