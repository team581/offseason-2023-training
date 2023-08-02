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

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends LoggedRobot {

  private TalonFX armMotor = new TalonFX(16);
  XboxController controller = new XboxController(0);
  VoltageOut armVoltage = new VoltageOut(0);
  StatusSignal<Double> armRotor = armMotor.getRotorPosition();
  double xGoalRotations = Rotation2d.fromDegrees(10).getRotations();
  double yGoalRotations = Rotation2d.fromDegrees(50).getRotations();


  public Robot() {
    Logger.getInstance().addDataReceiver(new NT4Publisher());

    Logger.getInstance().start();
  }

  public void setWristAngle(double degrees, double currentRotation) {
    if (Rotation2d.fromDegrees(degrees).getRotations()<currentRotation) {
      armMotor.setControl(armVoltage.withOutput(0.2));
    } else if (Rotation2d.fromDegrees(degrees).getRotations()>currentRotation) {
      armMotor.setControl(armVoltage.withOutput(-0.2));
    } else if (Rotation2d.fromDegrees(degrees).getRotations()== currentRotation) {
      armMotor.setControl(armVoltage.withOutput(0));
    }
  }
  @Override
  public void teleopPeriodic() {
    boolean aPressed = controller.getAButtonPressed();
    boolean xPressed = controller.getXButtonPressed();
    boolean yPressed = controller.getYButtonPressed();
    boolean bPressed = controller.getBButtonPressed();
    double motorRotations = armRotor.getValue();
    double armRotations = motorRotations /50;
    if(aPressed){
      armMotor.disable();
    }
    if (bPressed) {
      armMotor.setRotorPosition(0);
    }
    if (xPressed) {
      setWristAngle(10, armRotations);
    }
    if(yPressed) {
      setWristAngle(50, armRotations);
    }
  }
}
