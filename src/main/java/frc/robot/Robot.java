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
            frontRight.configPeakCurrentLimit(50);
            frontLeft.configPeakCurrentLimit(50);
            rearRight.configPeakCurrentLimit(50);
            rearLeft.configPeakCurrentLimit(50);
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