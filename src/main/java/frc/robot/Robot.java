// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import org.littletonrobotics.junction.LoggedRobot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Robot extends LoggedRobot {
  private TalonSRX driveFrontRight = new TalonSRX(15);
  private TalonSRX driveRearRight = new TalonSRX(14);
  private TalonSRX driveFrontLeft = new TalonSRX(2);
  private TalonSRX driveRearLeft = new TalonSRX(8);
  private XboxController xbox = new XboxController(0);

   @Override
   public void teleopPeriodic() {
    double rightY = xbox.getRightY();
    double leftY = -xbox.getLeftY();
    double rightX = xbox.getRightX();
    double leftSpeed = xbox.getRightX() + xbox.getLeftY();
    double rightSpeed = xbox.getLeftY() - xbox.getRightX();

    driveFrontRight.set(ControlMode.PercentOutput, rightSpeed);
    driveFrontLeft.set(ControlMode.PercentOutput, leftSpeed);
    driveRearLeft.set(ControlMode.PercentOutput, leftSpeed);
    driveRearRight.set(ControlMode.PercentOutput, rightSpeed);
}
}
