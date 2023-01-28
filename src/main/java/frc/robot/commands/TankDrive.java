// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.io.Console;
import java.lang.module.ModuleDescriptor.Requires;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.XboxController;
public class TankDrive extends CommandBase {
  private XboxController inputDevice = new XboxController(Constants.USB_PORT_ID);
  /** Creates a new TankDrive. */
  public TankDrive() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.chassis);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double throtle = inputDevice.getRawAxis(Constants.RIGHT_TRIGGER) - inputDevice.getRawAxis(Constants.LEFT_TRIGGER);
    double stickX = inputDevice.getRawAxis(Constants.LEFT_STICK_X);
    System.out.println("working");
    Robot.chassis.setLeftMotars(throtle + stickX);
    Robot.chassis.setRightMotars(throtle - stickX);

  }

  
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.chassis.setLeftMotars(0);
    Robot.chassis.setRightMotars(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
