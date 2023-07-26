// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.LoggedRobot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends LoggedRobot {
  private TalonSRX frontLeft = new TalonSRX(2);
  private TalonSRX rearLeft = new TalonSRX(8);
  private TalonSRX rearRight = new TalonSRX(14);
  private TalonSRX frontRight = new TalonSRX(15);
  private XboxController xbox = new XboxController(0);

  @Override
  public void robotInit() {
    rearRight.setInverted(true);
    frontRight.setInverted(true);
    frontRight.enableCurrentLimit(true);
    frontLeft.enableCurrentLimit(true);
    rearRight.enableCurrentLimit(true);
    rearLeft.enableCurrentLimit(true);
    frontRight.configPeakCurrentLimit(51);
    frontLeft.configPeakCurrentLimit(51);
    rearRight.configPeakCurrentLimit(51);
    rearLeft.configPeakCurrentLimit(51);
  }

  @Override
  public void teleopPeriodic() {
    double rightX = xbox.getRightX();
    double leftY = -xbox.getLeftY();
    double leftSpeed = leftY + rightX;
    double rightSpeed = leftY - rightX;
    frontLeft.set(ControlMode.PercentOutput, leftSpeed);
    rearLeft.set(ControlMode.PercentOutput, leftSpeed);
    frontRight.set(ControlMode.PercentOutput, rightSpeed);
    rearRight.set(ControlMode.PercentOutput, rightSpeed);
  }
}
