
import robocode.*;
import robocode.Robot;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.util.Random;
import java.awt.*;
import java.util.Random;

public class robo3 extends AdvancedRobot {
    private Random random = new Random();
    int count = 0; // Keeps track of how long we've
    // been searching for our target
    double gunTurnAmt; // How much to turn our gun when searching
    String trackName; // Name of the robot we're currently tracking
    int wallMargin = 20;
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
//                if(random.nextBoolean()) {
//                    turnLeft(random.nextInt(90) + 135);
//                    setBack(70);
//                }
//                else
//                {
//                    turnRight(random.nextInt(90) + 135);
//                    setBack(70);
//                }
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
        if (e.getDistance() > 150) {
            gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));

            turnRadarRight(gunTurnAmt);
            setBack(140);
            if(false)
                turnLeft(e.getBearing()+90);
            else
                turnRight(e.getBearing()+90);

            return;
        }

        gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
        turnRadarRight(gunTurnAmt);

        if (e.getDistance() <= 150) {
            if (e.getBearing() > -90 && e.getBearing() <= 90) {
                setBack(100);
            } else {
                setAhead(100);
            }
        }
        scan();
    }

    public void onHitRobot(HitRobotEvent e) {
        trackName = e.getName();
        gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
        setAhead(50);
        turnRadarRight(gunTurnAmt);

    }

    public void onWin(WinEvent e) {
        for (int i = 0; i < 50; i++) {
            turnRight(30);
            turnLeft(30);
        }
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        super.onHitWall(event);

            setAhead(100);
            turnRight(90);


    }
}
