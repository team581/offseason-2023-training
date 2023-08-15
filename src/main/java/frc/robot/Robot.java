package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXStatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import frc.advantage.AdvantageLibrary.Logger;

public class Robot extends TimedRobot {
    private TalonFX armMotor = new TalonFX(17);
    private TalonFXConfiguration armMotorConfig = new TalonFXConfiguration();
    private XboxController controller = new XboxController(0);

    private double setpointAngle = 0.0;
    private final double SETPOINT_1_ANGLE = 0.0;
    private final double SETPOINT_2_ANGLE = 10.0;
    private final double SETPOINT_3_ANGLE = 50.0;

    private final double ANGLE_TOLERANCE = 2.0;

    private final int GEARBOX_RATIO = 25;
    private final int ARM_PIVOT_RATIO = 2;

    @Override
    public void robotInit() {
        armMotorConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.IntegratedSensor;
        armMotor.configAllSettings(armMotorConfig);

        armMotorConfig.statusFramePeriod = TalonFXStatusFrame.Status_2_Feedback0;
        armMotorConfig.statusFramePeriodMs = 5;
        armMotor.configAllSettings(armMotorConfig);
    }

    @Override
    public void teleopPeriodic() {
        boolean aPressed = controller.getAButtonPressed();
        boolean bPressed = controller.getBButtonPressed();
        boolean xPressed = controller.getXButtonPressed();
        boolean yPressed = controller.getYButtonPressed();
        boolean backButtonPressed = controller.getBackButtonPressed();

        double armRotations = armMotor.getSelectedSensorPosition() / GEARBOX_RATIO / ARM_PIVOT_RATIO;

        double armAngle = armRotations * 360.0;

        Logger.getInstance().recordOutput("Arm/Position", armAngle);

        if (aPressed) {
            armMotor.set(ControlMode.PercentOutput, 0.0);
        }

        if (bPressed) {
            armMotor.setSelectedSensorPosition(0);
        }

        if (xPressed) {
            setpointAngle = SETPOINT_2_ANGLE;

            bangBangControl(setpointAngle, armRotations);
        }

        if (yPressed) {
            setpointAngle = SETPOINT_3_ANGLE;

            bangBangControl(setpointAngle, armRotations);
        }

        if (backButtonPressed) {
            armMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }

    private void bangBangControl(double setpointRotations, double currentRotations) {
        double voltage = 0.2;

        if (currentRotations > (setpointRotations + ANGLE_TOLERANCE)) {
            armMotor.set(ControlMode.PercentOutput, -voltage);
        } else if (currentRotations < (setpointRotations - ANGLE_TOLERANCE)) {
            armMotor.set(ControlMode.PercentOutput, voltage);
        } else {
            armMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }
}
