// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.LoggedRobot;

public class Robot extends LoggedRobot {
  private TalonSRX driveRightAMotor = new TalonSRX(15);
  private TalonSRX driveRightBMotor = new TalonSRX(8);
  private TalonSRX driveLeftAMotor = new TalonSRX(14);
  private TalonSRX driveLeftBMotor = new TalonSRX(2);
  private XboxController xbox = new XboxController(0);

  @Override
  public void teleopPeriodic() {
    double leftY = -xbox.getLeftY();
    double rightY = -xbox.getRightY();

    driveLeftAMotor.set(ControlMode.PercentOutput, leftY);
    driveRightAMotor.set(ControlMode.PercentOutput, rightY);
  }
}
