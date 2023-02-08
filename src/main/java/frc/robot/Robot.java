// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static double SPEED_MOD = 0.15;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  CANSparkMax motorLF = new CANSparkMax (1, MotorType.kBrushless);
  CANSparkMax motorLB = new CANSparkMax (2, MotorType.kBrushless);
  CANSparkMax motorRF = new CANSparkMax (3, MotorType.kBrushless);
  CANSparkMax motorRB = new CANSparkMax (4, MotorType.kBrushless);

  RelativeEncoder encoderLF; 
  RelativeEncoder encoderRF; 


  private XboxController inputDevice = new XboxController(0);

  public void setRightMotars(double speed) {
    motorRF.set(-speed);
    motorRB.set(-speed);
  }
  public void setLeftMotars(double speed) {
    motorLF.set(speed);
    motorLB.set(speed);
  }


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    encoderLF = motorLF.getEncoder();
    encoderRF = motorRF.getEncoder();
    encoderLF.setPosition(0.0);
    encoderRF.setPosition(0.0);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    double lDistance = encoderLF.getPosition();
    double rDistance = -encoderRF.getPosition();

    double haltDistance = 30.0;
    double testSpeed = 0.5;
    switch (m_autoSelected) {
      case kCustomAuto:

      case kDefaultAuto:
      default:
      
        if (rDistance < haltDistance) {
          setLeftMotars(testSpeed);
          setRightMotars(testSpeed);
        } else {
          setRightMotars(0.0);
          setLeftMotars(0.0);
          System.out.println("Task halted");
        }
        
        //System.out.println("left" + lDistance);
        //System.out.println("right" + rDistance);
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double throtle = (inputDevice.getRawAxis(3) - inputDevice.getRawAxis(2)) ;
    double stickX = inputDevice.getRawAxis(0);
    setLeftMotars((throtle + stickX) * SPEED_MOD);
    setRightMotars((throtle - stickX) * SPEED_MOD);
    double averageSpeed = (throtle + stickX + throtle - stickX)/2;
    System.out.println(averageSpeed);
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
