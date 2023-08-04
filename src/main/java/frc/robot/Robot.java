// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.XboxController;

public class Robot extends LoggedRobot {
  private TalonFX wristMotor = new TalonFX(16);
  private double wristTolerance = 1;
  private StatusSignal<Double> wristMotorRotations = wristMotor.getRotorPosition();
  private XboxController xboxController = new XboxController(0);
  public VoltageOut wristVoltage = new VoltageOut(0);
  public double goalAngleInDegrees;
  public boolean wristDisabled = true;

  public Robot() {
    Logger.getInstance().addDataReceiver(new NT4Publisher());

    Logger.getInstance().start();
  }

  @Override
    public void robotInit() {
    wristMotor.setControl(wristVoltage.withOutput(0));
    wristDisabled = true;
  }

  private double getWristAngleInDegrees() {
    StatusSignal<Double> wristMotorRotations = wristMotor.getRotorPosition();
    return wristMotorRotations.getValue() / 50.0 * 360.0;
  }

  @Override
  public void teleopPeriodic() {

    if (xboxController.getAButton()) {
      wristMotor.setControl(wristVoltage.withOutput(0));
      wristDisabled = true;
    } else if (xboxController.getBButton()) {
      wristMotor.setRotorPosition(0);
      goalAngleInDegrees = 0;
      wristDisabled = false;
    } else if (xboxController.getXButton()) {
      goalAngleInDegrees = 10;
      wristDisabled = false;
    } else if (xboxController.getYButton()) {
      goalAngleInDegrees = 50;
      wristDisabled = false;
    }

    if (!wristDisabled) {
      if (getWristAngleInDegrees() > goalAngleInDegrees - wristTolerance && getWristAngleInDegrees() < goalAngleInDegrees + wristTolerance) {
        wristMotor.setControl(wristVoltage.withOutput(0));
      } else if (getWristAngleInDegrees() > goalAngleInDegrees) {
        wristMotor.setControl(wristVoltage.withOutput(-1));
      } else if (getWristAngleInDegrees() < goalAngleInDegrees) {
        wristMotor.setControl(wristVoltage.withOutput(1));
      }
    } else {
      wristMotor.setControl(wristVoltage.withOutput(0));
    }
  }

  @Override
  public void robotPeriodic() {
    Logger.getInstance().recordOutput("Wrist/Angle", getWristAngleInDegrees());
    Logger.getInstance().recordOutput("Wrist/GoalAngle", goalAngleInDegrees);
    Logger.getInstance().recordOutput("Wrist/Disabled", wristDisabled);
    Logger.getInstance().recordOutput("Wrist/Voltage", wristMotor.getSupplyVoltage().getValue());
  }
}
