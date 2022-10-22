package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.RampRate.*;

public class Robot extends TimedRobot {
  private DifferentialDrive m_drive;
  private Joystick m_joy = new Joystick(0);
  private CANSparkMax m_leftAft;
  private CANSparkMax m_leftFront;
  private CANSparkMax m_rightAft;
  private CANSparkMax m_rightFront;
  private RampRate m_throttleRamp;
  private double u_upRampRate =  .4;
  private double u_downRampRate = .1;

  @Override
  public void robotInit() {

    m_leftAft = new CANSparkMax(6, MotorType.kBrushless);
    m_leftFront = new CANSparkMax(7, MotorType.kBrushless);
    m_rightAft = new CANSparkMax(9, MotorType.kBrushless);
    m_rightFront = new CANSparkMax(8, MotorType.kBrushless);

    m_leftAft.restoreFactoryDefaults();
    m_leftFront.restoreFactoryDefaults();
    m_rightAft.restoreFactoryDefaults();
    m_rightFront.restoreFactoryDefaults();

    m_rightAft.setInverted(true);
    m_rightFront.setInverted(true);

    MotorControllerGroup m_leftGroup = new MotorControllerGroup(m_leftFront, m_leftAft);
    MotorControllerGroup m_rightGroup = new MotorControllerGroup(m_rightFront, m_rightAft);

    m_drive = new DifferentialDrive(m_leftGroup, m_rightGroup);

  

    m_throttleRamp.setRampRates(u_downRampRate, u_downRampRate);

  }
  
  @Override
  public void teleopPeriodic() {
    
    double throttleNextValue = m_throttleRamp.getNextValue(-m_joy.getY()); 


    m_drive.curvatureDrive(throttleNextValue, m_joy.getX(), m_joy.getRawButtonPressed(3)); 

    m_throttleRamp.setLastValue(throttleNextValue);
  }
}