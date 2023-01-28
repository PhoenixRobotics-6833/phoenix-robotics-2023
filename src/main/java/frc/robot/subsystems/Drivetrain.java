// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.concurrent.locks.Condition;

import com.revrobotics.SparkMaxAbsoluteEncoder;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.TankDrive;
public class Drivetrain extends SubsystemBase {
  /** Creates a new Drivetrain. */
  public Drivetrain() {}
  Spark motarLF = new Spark(Constants.MOTOR_LEFT_FRONT_ID);
  Spark motarLB = new Spark(Constants.MOTOR_LEFT_BACK_ID);
  Spark motarRF = new Spark(Constants.MOTOR_RIGHT_FRONT_ID);
  Spark motarRB = new Spark(Constants.MOTOR_RIGHT_FRONT_ID);

  @Override
  public void periodic() {
    setDefaultCommand(new TankDrive());
    // This method will be called once per scheduler run
  }

  public void setRightMotars(double speed) {
    motarRF.set(speed);
    motarRB.set(speed);
  }
  public void setLeftMotars(double speed) {
    motarLF.set(speed);
    motarLB.set(speed);
  }
}
