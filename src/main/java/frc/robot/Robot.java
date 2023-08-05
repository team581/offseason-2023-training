// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.util.scheduling.LifecycleSubsystemManager;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;

public class Robot extends LoggedRobot {
  private XboxController xboxController = new XboxController(0);
  private double wristTolerance = 1;
  public double goalAngle = 0;
  public boolean wristDisabled = true;
  private TalonFX wristMotor = new TalonFX(16);
  public VoltageOut wristVoltage = new VoltageOut(0);

  public Robot() {
    Logger.getInstance().addDataReceiver(new NT4Publisher());
    Logger.getInstance().start();
    LifecycleSubsystemManager.getInstance().ready();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void teleopPeriodic() {
    if (xboxController.getAButton()) {
      wristDisabled = true;
    } else if (xboxController.getBButton()) {
      wristMotor.setRotorPosition(0);
      goalAngle = 0;
      wristDisabled = false;
    } else if (xboxController.getXButton()) {
      goalAngle = 10;
      wristDisabled = false;
    } else if (xboxController.getYButton()) {
      goalAngle = 50;
      wristDisabled = false;
    }

    if (wristDisabled) {
      wristMotor.disable();
    } else {
      if (getWristAngle() > goalAngle - wristTolerance) {
        wristMotor.setControl(wristVoltage.withOutput(-1));
      } else if (getWristAngle() < goalAngle + wristTolerance) {
        wristMotor.setControl(wristVoltage.withOutput(1));
      } else {
        wristMotor.setControl(wristVoltage.withOutput(0));
      }
    }
  }

  private double getWristAngle() {
    StatusSignal<Double> wristMotorRotations = wristMotor.getRotorPosition();
    return wristMotorRotations.getValue() / 50.0 * 360.0;
  }
}
