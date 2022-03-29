// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.ToolShed;

import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.robot.Constants;
import frc.robot.navx;
import frc.robot.Constants.MKTRAIN;
import frc.robot.Constants.MKTURN;
import frc.robot.Factory.Motor.MkSwerveModule;
import frc.robot.Factory.Motor.MkSwerveTrain;

/** Add your docs here. */
public class SwerveAlgorithims {

    private MkSwerveModule[] modules;
    private variables var;
    private double hP = 0.001, hI = 0.0001, hD = hP * 0.1;
    private double hIntegral, hDerivative, hPreviousError, hError;

    private SwerveAlgorithims()
    {
        modules = MkSwerveTrain.getInstance().getModules();
        var = new variables();
    }

    public static SwerveAlgorithims getInstance()
    {
        return InstanceHolder.mInstance;
    }

    /**
     * See <a href="https://www.chiefdelphi.com/t/paper-4-wheel-independent-drive-independent-steering-swerve/107383">this thread</a>
     * for more information.
     * <p>
     * Note - this function uses 180 minus yaw due to the positioning of our navx.
     * @param FWD Forward axis of controller
     * @param STR Strafe axis of controller
     * @param RCW Rotational axis of controller
     * @author Ether
     */
    public void etherSwerve(double FWD, double STR, double RCW)
    {
        var.yaw = navx.getInstance().getNavxYaw();
        var.temp = FWD * Math.cos(Math.toRadians(var.yaw)) + STR * Math.sin(Math.toRadians(var.yaw));
        STR = -FWD * Math.sin(Math.toRadians(var.yaw)) + STR * Math.cos(Math.toRadians(var.yaw));
        FWD = var.temp;

        //SmartDashboard.putNumber("frd", FWD);
        //SmartDashboard.putNumber("str", STR);

        var.A = STR - RCW*(MKTRAIN.L/MKTRAIN.R);
        var.B = STR + RCW*(MKTRAIN.L/MKTRAIN.R);
        var.C = FWD - RCW*(MKTRAIN.W/MKTRAIN.R);
        var.D = FWD + RCW*(MKTRAIN.W/MKTRAIN.R);
        
        var.ws1 = Math.sqrt((Math.pow(var.B, 2)) + (Math.pow(var.C, 2)));      var.wa1 = Math.atan2(var.B,var.C)*180/Constants.kPi;
        var.ws2 = Math.sqrt((Math.pow(var.B, 2)) + (Math.pow(var.D, 2)));      var.wa2 = Math.atan2(var.B,var.D)*180/Constants.kPi;
        var.ws3 = Math.sqrt((Math.pow(var.A, 2)) + (Math.pow(var.D, 2)));      var.wa3 = Math.atan2(var.A,var.D)*180/Constants.kPi;
        var.ws4 = Math.sqrt((Math.pow(var.A, 2)) + (Math.pow(var.C, 2)));      var.wa4 = Math.atan2(var.A,var.C)*180/Constants.kPi; 

        var.max=var.ws1; if(var.ws2>var.max)var.max=var.ws2; if(var.ws3>var.max)var.max=var.ws3; if(var.ws4>var.max)var.max=var.ws4;
        if(var.max>1){var.ws1/=var.max; var.ws2/=var.max; var.ws3/=var.max; var.ws4/=var.max;}


        var.mod1 = SwerveAlgorithims.setDirection(modules[1].turnMotor().getPosition(), var.wa1);
        var.mod2 = SwerveAlgorithims.setDirection(modules[0].turnMotor().getPosition(), var.wa2);
        var.mod3 = SwerveAlgorithims.setDirection(modules[2].turnMotor().getPosition(), var.wa3);
        var.mod4 = SwerveAlgorithims.setDirection(modules[3].turnMotor().getPosition(), var.wa4);

        var.wa1 = var.mod1[0];
        var.wa2 = var.mod2[0];
        var.wa3 = var.mod3[0];
        var.wa4 = var.mod4[0];

        var.ws1 *= var.mod1[1];
        var.ws2 *= var.mod2[1];
        var.ws3 *= var.mod3[1];
        var.ws4 *= var.mod4[1];

        modules[1].setModule(var.ws2, ControlMode.PercentOutput, FalconAlgorithims.degreesToNative(var.wa2, MKTURN.greerRatio));
        modules[0].setModule(var.ws1, ControlMode.PercentOutput, FalconAlgorithims.degreesToNative(var.wa1, MKTURN.greerRatio));
        modules[2].setModule(var.ws3, ControlMode.PercentOutput, FalconAlgorithims.degreesToNative(var.wa3, MKTURN.greerRatio));
        modules[3].setModule(var.ws4, ControlMode.PercentOutput, FalconAlgorithims.degreesToNative(var.wa4, MKTURN.greerRatio));
    }



   /**
     * decides whether a driving motor should flip based on where the angular motor's setpoint is.
     * @param position position of the motor
     * @param setpoint setpoint for the motor
     * @return returns best angle of travel for the angular motor, as well as the flip value for the driving motor (as an array so it can return two things in one instead of two seperatly)
     * @author team 6624
     */
    public static double[] setDirection(double position, double setpoint)
    {
        double currentAngle = FalconAlgorithims.nativeToDegrees(position, MKTURN.greerRatio);
        // find closest angle to setpoint
        double setpointAngle = FalconAlgorithims.closestAngle(currentAngle, setpoint);
        // find closest angle to setpoint + 180
        double setpointAngleFlipped = FalconAlgorithims.closestAngle(currentAngle, setpoint + 180.0);
        // if the closest angle to setpoint is shorter
        if (Math.abs(setpointAngle) <= Math.abs(setpointAngleFlipped))
        {
            // unflip the motor direction use the setpoint
            return new double[] {(currentAngle + setpointAngle), 1.0};
        }
        // if the closest angle to setpoint + 180 is shorter
        else
        {
            // flip the motor direction and use the setpoint + 180
            return new double[] {(currentAngle + setpointAngleFlipped), -1.0};
        }
    }

    //programming done right
    public double headerStraighter(double hSetpoint)
    {
        hError = hSetpoint -  navx.getInstance().getNavxYaw();// Error = Target - Actual
        hIntegral += (hError*.02); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
        hDerivative = (hError - hPreviousError) / .02;
        return hP*hError + hI*hIntegral + hD*hDerivative;
    }

    private static class InstanceHolder
    {
        private static final SwerveAlgorithims mInstance = new SwerveAlgorithims();
    } 

    public static class variables 
    {
        public double temp;
        public double yaw;
        public double A;
        public double B;
        public double C;
        public double D;
        public double wa1;
        public double wa2;
        public double wa3;
        public double wa4;
        public double ws1;
        public double ws2;
        public double ws3;
        public double ws4;
        public double mod1[];
        public double mod2[];
        public double mod3[];
        public double mod4[];
        public double max;
    }
}
