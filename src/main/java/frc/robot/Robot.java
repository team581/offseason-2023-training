// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot;

import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.console.RIOConsoleSource;

import com.ctre.phoenix6.hardware.TalonFX;  //The motor type we're using.
import edu.wpi.first.wpilibj.XboxController;//The controller type we're using.

public class Robot extends LoggedRobot {
 
 
    private TalonFX intakeMotor = new TalonFX(17, "rio");
  private XboxController xbox = new XboxController(0);

  @Override                                 
  public void robotInit() { 
    intakeMotor.set;
  }

  @Override
  public void teleopPeriodic() {  
   double speed = -xbox.getLeftY();
   boolean hasCone = intakeMotor.getReverseLimit().getValue().value == 1;
   
    if (hasCone){
        intakeMotor.set(Math.min(speed, 0.1));
    } else {
        intakeMotor.set(Math.max(speed, -0.1));
    }
    System.out.println(intakeMotor.getReverseLimit().getValue().value);


      intakeMotor.set(xbox.getLeftY());   
  }                                    
}



