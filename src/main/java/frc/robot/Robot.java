// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.LoggedRobot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends LoggedRobot {
  private TalonSRX driveRightAMotor = new TalonSRX(14);
  private TalonSRX driveRightBMotor = new TalonSRX(15);
  private TalonSRX driveLeftAMotor = new TalonSRX(2);
  private TalonSRX driveLeftBMotor = new TalonSRX(8);
  private XboxController xbox = new XboxController(0);

  @Override
  public void teleopPeriodic() {
    double leftY = xbox.getLeftY();
    double leftY = xbox.getLeftY();
    double rightX = xbox.getRightX();
 // idk
    double leftSpeed = leftY + rightx;
    double rightSpeed = leftY - rightx;

    driveRightAMotor.set(ControlMode.PercentOutput, rightSpeed);
    driveRightBMotor.set(ControlMode.PercentOutput, rightSpeed);
    driveLeftBMotor.set(ControlMode.PercentOutput, leftSpeed);
    driveLeftAMotor.set(ControlMode.PercentOutput, leftSpeed);


  }
  @Override
  public void robotInit() {
    driveLeftAMotor.setInverted(true);
    driveLeftBMotor.setInverted(true);
  }
}
