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
  TalonFX armMotor = new TalonFX(16);
  XboxController controller = new XboxController(0);

  public Robot() {
    Logger.getInstance().addDataReceiver(new NT4Publisher());

    Logger.getInstance().start();
  }

  @Override
  public void teleopPeriodic() {
    StatusSignal<Double> armRotor = armMotor.getRotorPosition();
    double motorRotations = armRotor.getValue();
    double Degrees = (motorRotations/50)/360;
    double trgt = 0;
    VoltageOut armVoltage = new VoltageOut(0);

    boolean aPressed = controller.getAButtonPressed();
    boolean xPressed = controller.getXButtonPressed();
    boolean bPressed = controller.getBButtonPressed();
    boolean yPressed = controller.getYButtonPressed();
    if (xPressed) {
      trgt = 10;
    }
    if (yPressed) {
      trgt = 50;
    }

    if (aPressed || bPressed) {
      armMotor.setControl(armVoltage.withOutput(0));
    } else if (xPressed||yPressed) {
      if (Degrees > trgt) {
        armMotor.setControl(armVoltage.withOutput(-0.2));
      } else if (Degrees < trgt) {
        armMotor.setControl(armVoltage.withOutput(0.2));
      } else if (Degrees == trgt) {
        armMotor.setControl(armVoltage.withOutput(0));
      }
    }
    if (bPressed) {
      armMotor.setRotorPosition(0);
    }
  }
}
