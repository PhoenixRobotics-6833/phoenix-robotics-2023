// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;  

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

import java.util.concurrent.*;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static double SPEED_MOD = 0.5;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "Docing";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  DigitalInput limitSwitch = new DigitalInput(0);
  private XboxController inputDevice = new XboxController(0);
  private XboxController inputDeviceS = new XboxController(1);
  CANSparkMax motorLF = new CANSparkMax (1, MotorType.kBrushless);
  CANSparkMax motorLB = new CANSparkMax (2, MotorType.kBrushless);
  CANSparkMax motorRF = new CANSparkMax (3, MotorType.kBrushless);
  CANSparkMax motorRB = new CANSparkMax (4, MotorType.kBrushless);
  CANSparkMax motorClamp = new CANSparkMax (5, MotorType.kBrushless);
  WPI_TalonSRX motorLift = new WPI_TalonSRX(6);
  TalonSRX motorLBrake = new TalonSRX(7);
  TalonSRX motorRBrake = new TalonSRX(8);
  public double armState = 0;
  // module type, foward channel, reverse channel
  DoubleSolenoid pneumaticArm = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 2);
  
  RelativeEncoder encoderLF; 
  RelativeEncoder encoderRF; 

  static double MOTOR_RATIO = 2.23071667;
  static double ARM_MOTOR_SPEED = 0.5;
  static double initDistance = 136;
  static double initDoc = 160;
// AUTO VARS

  double taskIndex;
  double aDistance;
  double lDistance;
  double rDistance;

// GLOBAL FUNCTIONS

  public void armClamp(double speed) {
    motorClamp.set(speed);
  }

  public void armLift(double speed) {
    motorLift.set(TalonSRXControlMode.PercentOutput, speed);
  }

  public void setRightMotars(double speed) {
    // System.out.println("Right:" + speed);
    motorRF.set(-speed);
    motorRB.set(-speed);
  }
  public void setLeftMotars(double speed) {
    // System.out.println("Left:" + speed);
    motorLF.set(speed);
    motorLB.set(speed);
  }

  public double calculateRotations(double inches) {
    return(inches/MOTOR_RATIO);
  }

// AUTO FUNCTIONS //

  public void incrementTask() {
    taskIndex += 1;
    setRightMotars(0.0);
    setLeftMotars(0.0);
    encoderLF.setPosition(0.0);
    encoderRF.setPosition(0.0);
  }

  public void updateDistances() {
    lDistance = encoderLF.getPosition();
    rDistance = -encoderRF.getPosition();
    aDistance = (lDistance + rDistance)/2;
  }

  public void linearMove(double distance, double speed) {
    while (aDistance > calculateRotations(distance)) {
      updateDistances();
      setLeftMotars(speed);
      setRightMotars(speed);
    }
  }
  public void linearMoveF(double distance, double speed) {
    while (aDistance < calculateRotations(distance)) {
      updateDistances();
      setLeftMotars(speed);
      setRightMotars(speed);
    }
  }
  public void dock() {
    linearMove(-110, -0.6);
    incrementTask();
  }
  public void coms() {
    linearMove(-initDistance, -0.3);
    incrementTask();
  }
  public void comsDoc() {
    linearMove(-initDoc, -0.3);
    incrementTask();
  }
  public void dockB() {
    linearMoveF(86, 0.2);
    incrementTask();
  }
  public void arm() {
    double startTime = System.currentTimeMillis();
          while( System.currentTimeMillis() < startTime + 750){
            //System.out.println(System.currentTimeMillis());
            motorLift.set(TalonSRXControlMode.PercentOutput, -0.8);
          }
          motorLift.set(TalonSRXControlMode.PercentOutput, 0);
          startTime = System.currentTimeMillis();
          while( System.currentTimeMillis() < startTime + 600) {
            motorClamp.set(-0.3);

          }
          motorClamp.set(0);
          startTime = System.currentTimeMillis();
          while( System.currentTimeMillis() < startTime + 750){
            //System.out.println(System.currentTimeMillis());
            motorLift.set(0.8);
          }
          motorLift.set(TalonSRXControlMode.PercentOutput, 0);
          startTime = System.currentTimeMillis();
          while( System.currentTimeMillis() < startTime + 600) {
            motorClamp.set(0.3);

          }
          motorClamp.set(0);
          incrementTask();
          
  }
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Docing", kCustomAuto);
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
    taskIndex = 0;

    System.out.println("Auto selected: " + m_autoSelected);
    encoderLF = motorLF.getEncoder();
    encoderRF = motorRF.getEncoder();
    encoderLF.setPosition(0.0);
    encoderRF.setPosition(0.0);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    updateDistances();
    
    
    switch (m_autoSelected) {
      case kDefaultAuto:
      if (taskIndex == 0 ) {
        while (taskIndex == 0) {
          arm();
        }
        if (taskIndex == 1) {
          coms();
          //dockB();
        }
        }//endline
      case kCustomAuto:
      if (taskIndex == 0 ) {
        while (taskIndex == 0) {
          arm();

        }
        if (taskIndex == 1) {
          comsDoc();
          dockB();
        }
        }//endline
    }
  }

  /** T+his function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    motorLBrake.setNeutralMode(NeutralMode.Brake);
  }
  
  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if (inputDeviceS.getStartButtonPressed()) {
      motorRBrake.set(TalonSRXControlMode.PercentOutput, -0.25);

      motorLBrake.set(TalonSRXControlMode.PercentOutput, 0.25);
    }
    if (inputDeviceS.getBackButtonPressed()) {
      motorRBrake.set(TalonSRXControlMode.PercentOutput, 0);

      motorLBrake.set(TalonSRXControlMode.PercentOutput, 0);
    }

    double armLiftSpeed = inputDeviceS.getRawAxis(1) * -0.5;
    //double armClampSpeed = (inputDeviceS.getRawAxis(3) - inputDeviceS.getRawAxis(2)) * 0.5;

    if (inputDeviceS.getRightBumperPressed()) {
      if (armState == 0) {
        armState = 1;
      } else {
        armState = 0;
      }
    }
    if (armState == 0) {
      pneumaticArm.set(Value.kForward);
    } else {
      pneumaticArm.set(Value.kReverse);
    }
    if (limitSwitch.get()) {
      armLift(armLiftSpeed);
      System.out.println("switch");
    } else if (armLiftSpeed > 0.1) {
      armLift(armLiftSpeed);
      
    }
    System.out.println(armLiftSpeed);
    //armClamp(armClampSpeed);
    
    double throtle = (inputDevice.getRawAxis(3) - inputDevice.getRawAxis(2));
    double stickX = inputDevice.getRawAxis(0);
    setLeftMotars((throtle + stickX) * SPEED_MOD);
    setRightMotars((throtle - stickX) * SPEED_MOD);
   
    if (inputDeviceS.getAButtonPressed()) {
      double aTIME = System.currentTimeMillis();
      while( System.currentTimeMillis() < aTIME + 900) {
        motorLift.set(TalonSRXControlMode.PercentOutput,1);

      }
      motorLift.set(TalonSRXControlMode.PercentOutput, 0);

    }
    
  

    
    //System.out.println(averageSpeed);
  }

  ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
 
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
