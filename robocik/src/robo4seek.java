
import robocode.*;
import robocode.Robot;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.util.Random;
import java.awt.*;


public class robo4seek extends AdvancedRobot {
    private Random random = new Random();
    int count = 0; // Keeps track of how long we've
    // been searching for our target
    double gunTurnAmt; // How much to turn our gun when searching
    String trackName; // Name of the robot we're currently tracking
    int wallMargin = 30;
    /**
     * run:  Tracker's main run function
     */
    public void run() {

        // Prepare gun
        trackName = null; // Initialize to not tracking anyone
        setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
        gunTurnAmt = 10; // Initialize gunTurn to 10
        turnRadarRight(360);
        // Loop forever
        while (true) {

//            if(getX() <= wallMargin ||
//                    // or we're too close to the right wall
//                    getX() >= getBattleFieldWidth() - wallMargin ||
//                    // or we're too close to the bottom wall
//                    getY() <= wallMargin ||
//                    // or we're too close to the top wall
//                    getY() >= getBattleFieldHeight() - wallMargin)
//            {
//                if(random.nextBoolean())
//                    turnLeft(random.nextInt(90)+135);
//                else
//                    turnRight(random.nextInt(90)+135);
//            }

            turnRadarRight(gunTurnAmt);
            count++;
            if (count > 2) {
                gunTurnAmt = -10;
            }
            if (count > 5) {
                gunTurnAmt = 10;
            }
            if (count > 11) {
                trackName = null;
            }
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        if (trackName != null && !e.getName().equals(trackName)) {
            return;
        }

        if (trackName == null) {
            trackName = e.getName();
        }
        count = 0;
        if (e.getDistance() > 50) {
            gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));

            turnRadarRight(gunTurnAmt);
            turnRight(e.getBearing());
            setAhead(e.getDistance() - 50);
            return;
        }

        gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
        turnRadarRight(gunTurnAmt);



        if (e.getDistance() <= 50) {
            if (e.getBearing() > -90 && e.getBearing() <= 90) {
                setAhead(e.getDistance());
            } else {
                setAhead(e.getDistance());
            }
        }
        scan();
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        super.onHitWall(event);

        if(random.nextBoolean())
            turnLeft(random.nextInt(90)+90);
        else
            turnRight(random.nextInt(90)+90);
    }

    public void onWin(WinEvent e) {
        for (int i = 0; i < 50; i++) {
            turnRight(30);
            turnLeft(30);
        }
    }
}
