// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;

/** Add your docs here. */
public final class Constants {

    public static final double kPi = 3.14159265359;

    public static class MKFALCON 
    {
        public static final int velocityMeasAmount = 16;
        public static final int statusOneMeas = 25;
        public static final int statusTwoMeas = 25;
        public static final double voltComp = 12;
        public static final double oneEncoderRotation = 2048;
    }

    public static class MKDRIVE 
    {
        public static final double kS = 0;
        public static final double kA = 0;
        public static final double kV = 0;
        
        public static final double kP = 0;
        public static final double kI = 0;
        public static final double kD = 0;
        public static final double kF = 0;

        public static final double[] pidf = {kP, kI, kD, kF};

        public static final NeutralMode mode = NeutralMode.Brake;

        public static final boolean inverted = false;

        public static final int scurve = 6;

        public static final double greerRatio = 0;

        public static final double wheelDiameterInches = 4; 
        public static final double wheelCircumference = wheelDiameterInches * kPi;    
    }

    public static class MKTURN 
    {
        public static final double kP = 0;
        public static final double kI = 0;
        public static final double kD = 0;
        public static final double kF = 0;
        
        public static final double[] pidf = {kP, kI, kD, kF};

        public static final NeutralMode mode = NeutralMode.Brake;

        public static final boolean inverted = true;

        public static final int scurve = 6;

        public static final double greerRatio = 0;
    }

    public static class MKCANCODER
    {
        public static final double topLeftOffset = -72.685546875;
        public static final double topRightOffset = -9.4921875;
        public static final double bottomLeftOffset = -117.24609375;
        public static final double bottomRightOffset = 46.0546875;

        public static final AbsoluteSensorRange range = AbsoluteSensorRange.Signed_PlusMinus180;

        public static final boolean inverted = true;
    }

    public static class MKTRAIN 
    {
        public static final double[][][] pidf = 
        {
            {
                MKDRIVE.pidf,
                MKTURN.pidf
            },
            {
                MKDRIVE.pidf,
                MKTURN.pidf
            },
            {
                MKDRIVE.pidf,
                MKTURN.pidf
            },
            {
                MKDRIVE.pidf,
                MKTURN.pidf
            },
        };

        public static final NeutralMode[][] mode = 
        {
            {MKDRIVE.mode, MKTURN.mode},
            {MKDRIVE.mode, MKTURN.mode},
            {MKDRIVE.mode, MKTURN.mode},
            {MKDRIVE.mode, MKTURN.mode}
        };

        public static final boolean[][][] inverted =
        {
            {{MKDRIVE.inverted}, {MKTURN.inverted, MKCANCODER.inverted}},
            {{MKDRIVE.inverted}, {MKTURN.inverted, MKCANCODER.inverted}},
            {{MKDRIVE.inverted}, {MKTURN.inverted, MKCANCODER.inverted}},
            {{MKDRIVE.inverted}, {MKTURN.inverted, MKCANCODER.inverted}}
        };

        public static final int[][] scurve = 
        {
            {MKDRIVE.scurve, MKTURN.scurve},
            {MKDRIVE.scurve, MKTURN.scurve},
            {MKDRIVE.scurve, MKTURN.scurve},
            {MKDRIVE.scurve, MKTURN.scurve}
        };

        public static final double[] offset = {MKCANCODER.topLeftOffset, MKCANCODER.topRightOffset, MKCANCODER.bottomLeftOffset, MKCANCODER.bottomRightOffset};

        public static final double L = 22.57;
        public static final double W = 22.57;
        public static final double R = Math.sqrt(Math.pow(L, 2) + Math.pow(W, 2));

    }

    public static class NAVX 
    {
        public static final double offset = 0;
    }

    public static class CANID 
    {
        public static final int topDriveLeftCANID = 3; 
        public static final int topDriveRightCANID = 5; 
        public static final int bottomDriveLeftCANID = 2; 
        public static final int bottomDriveRightCANID = 7;

        public static final int topTurnLeftCANID = 4; 
        public static final int topTurnRightCANID = 6; 
        public static final int bottomTurnLeftCANID = 1;
        public static final int bottomTurnRightCANID = 8; 

        public static final int topTurnLeftCANCoderCANID = 16; 
        public static final int topTurnRightCANCoderCANID = 18; 
        public static final int bottomTurnLeftCANCoderCANID = 15; 
        public static final int bottomTurnRightCANCoderCANID = 17;
        
        public static final int[][][] MkTrainIds =
        {
            {{topDriveLeftCANID}, {topTurnLeftCANID, topTurnLeftCANCoderCANID}},
            {{topDriveRightCANID}, {topTurnRightCANID, topTurnRightCANCoderCANID}},
            {{bottomDriveLeftCANID}, {bottomTurnLeftCANID, bottomTurnLeftCANCoderCANID}},
            {{bottomDriveRightCANID}, {bottomTurnRightCANID, bottomTurnRightCANCoderCANID}}
        };
    }
}
