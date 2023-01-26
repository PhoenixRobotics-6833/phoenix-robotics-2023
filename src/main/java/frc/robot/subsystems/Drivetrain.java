// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.SparkMaxAbsoluteEncoder;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
public class Drivetrain extends SubsystemBase {
  /** Creates a new Drivetrain. */
  public Drivetrain() {}
  Spark motarRight1 = new Spark(Constants.MOTAR_RIGHT1_ID);
  Spark motarLeft1 = new Spark(Constants.MOTAR_LEFT1_ID);

  @Override
  public void periodic() {
    setDefaultCommand(Drivetrain());
    // This method will be called once per scheduler run
  }

  public void setRightMotars(double speed) {
    motarRight1.set(speed);
  }
  public void setLeftMotars(double speed) {
    motarLeft1.set(-speed);
  }
}
