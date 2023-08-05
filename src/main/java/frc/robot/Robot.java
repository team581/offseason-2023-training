// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.XboxController;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;

public class Robot extends LoggedRobot {
  XboxController controller = new XboxController(0);
  TalonFX armMotor = new TalonFX(17);
  VoltageOut armVoltage = new VoltageOut(0);


  public Robot() {
    Logger.getInstance().addDataReceiver(new NT4Publisher());

    Logger.getInstance().start();
  }

  @Override
  public void teleopPeriodic() {
  StatusSignal<Double> armRotor = armMotor.getRotorPosition();
  double armRotations = armRotor.getValue();
  boolean aPressed = controller.getAButtonPressed();
  boolean bPressed = controller.getBButtonPressed();
  boolean xPressed = controller.getXButtonPressed();
  boolean yPressed = controller.getYButtonPressed();
    Logger.getInstance().recordOutput("Arm/Position", armRotations);
    double armDegrees = armRotations / 360;
    if (aPressed) {
      armMotor.setControl(armVoltage.withOutput(0.0));
    }
    if (bPressed) {
      armRotations = 0;
    }
    if (xPressed) {

    }
    if (yPressed) {}
  }
}
