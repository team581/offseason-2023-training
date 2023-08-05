// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;

public class Robot extends LoggedRobot {
    public Robot() {
        Logger.getInstance().addDataReceiver(new NT4Publisher());

        Logger.getInstance().start();
    }

    @Override
    public void teleopPeriodic() {
        // do something
        private TalonFX armMotor = new TalonFX(17);
        private XboxController controller = new XboxController(0);
        private VoltageOut armVoltage = new VoltageOut(0);

        boolean aPressed = controller.getAButtonPressed();
        boolean bPressed = controller.getBButtonPressed();
        boolean xPressed = controller.getXButtonPressed();
        boolean yPressed = controller.getYButtonPressed();
        StatusSignal<Double> armRotor = armMotor.getRotorPosition();
        double armRotations = armRotor.getValue();
        double armAngle = armRotations*45/256;

            if (aPressed==true){
                armMotor.setControl(armVoltage.withOutput(0.0));
            }
            if(bPressed==true){
                while (armAngle!=0){
                    if(armAngle > 0){
                    armMotor.setControl(armVoltage.withOutput(-0.2));
                    }
                        else if (armAngle < 0){
                            armMotor.setControl(armVoltage.withOutput(0.2));
                        }
                            else{
                                armMotor.setControl(armVoltage.withOutput(0.0));
                            }
                }


            }
            if(xPressed == true){
                while (armAngle!=10){
                    if(armAngle > 10){
                    armMotor.setControl(armVoltage.withOutput(-0.2));
                    }
                        else if (armAngle < 10){
                            armMotor.setControl(armVoltage.withOutput(0.2));
                        }
                            else{
                                armMotor.setControl(armVoltage.withOutput(0.0));
                            }
                }

            }
            if(yPressed == true){
                while (armAngle!=50){
                    if(armAngle > 50){
                    armMotor.setControl(armVoltage.withOutput(-0.2));
                }
                        else if (armAngle < 50){
                            armMotor.setControl(armVoltage.withOutput(0.2));
                        }
                            else{
                                armMotor.setControl(armVoltage.withOutput(0.0));
                            }
                }

            }
                }
            }
