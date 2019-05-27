/**
 * Copyright (c) 2001-2018 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package OscuroTeam;
import robocode.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;


/**
 * SimpleDroid - a sample robot by Mathew Nelson.
 * <p>
 * Follows orders of team leader.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
import robocode.Droid;
import robocode.MessageEvent;
import robocode.TeamRobot;
import static robocode.util.Utils.normalRelativeAngleDegrees;


/**
 * SimpleDroid - a sample robot by Mathew Nelson.
 * <p>
 * Follows orders of team leader.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class OscuroDroid extends TeamRobot implements Droid {
    private boolean moved = false;
    private boolean inCorner = false;
    /**
     * run:  Droid's default behavior
     */
    public void run() {
        out.println("MyFirstDroid ready.");
        while(true)
        {
            ahead(300);
            back(300);
        }
    }

    /**
     * onMessageReceived:  What to do when our leader sends a message
     */
    public void onHitRobot(HitRobotEvent e) {
        // If he's in front of us, set back up a bit.
        if (e.getBearing() > -90 && e.getBearing() < 90) {
            back(100);
        } // else he's in back of us, so set ahead a bit.
        else {
            ahead(100);
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        turnLeft(90 - e.getBearing());
    }

    public void onMessageReceived(MessageEvent e) {
        // Fire at a point
        if (e.getMessage() instanceof Point) {
            Point p = (Point) e.getMessage();
            // Calculate x and y to target
            double dx = p.getX() - this.getX();
            double dy = p.getY() - this.getY();
            // Calculate angle to target
            double theta = Math.toDegrees(Math.atan2(dx, dy));

            if (getDistanceRemaining() == 0 && getTurnRemaining() == 0) {
                if (inCorner) {
                    if (moved) {
                        setTurnLeft(90);
                        moved = false;
                    } else {
                        setAhead(160);
                        moved = true;
                        inCorner = false;
                    }
                } else {

                    if ((getHeading() % 90) != 0) {
                        setTurnLeft((getY() > (getBattleFieldHeight() / 2)) ? getHeading()
                                : getHeading() - 180);
                    } else if (getY() > 30 && getY() < getBattleFieldHeight() - 30) {
                        setAhead(getHeading() > 90 ? getY() - 20 : getBattleFieldHeight() - getY()
                                - 20);
                    } else if (getHeading() != 90 && getHeading() != 270) {
                        if (getX() < 350) {
                            setTurnLeft(getY() > 300 ? 90 : -90);
                        } else {
                            setTurnLeft(getY() > 300 ? -90 : 90);
                        }
                    } else if (getX() > 30 && getX() < getBattleFieldWidth() - 30) {
                        setAhead(getHeading() < 180 ? getX() - 20 : getBattleFieldWidth() - getX()
                                - 20);
                    } else if (getHeading() == 270) {
                        setTurnLeft(getY() > 200 ? 90 : 180);
                        inCorner = true;
                    } else if (getHeading() == 90) {
                        setTurnLeft(getY() > 200 ? 180 : 90);
                        inCorner = true;
                    }
                }
            }


            // Turn gun to target
            turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
            // Fire hard!
            setStop();
            fire(4);
        } // Set our colors
        else if (e.getMessage() instanceof RobotColors) {
            RobotColors c = (RobotColors) e.getMessage();

            setBodyColor(c.bodyColor);
            setGunColor(c.gunColor);
            setRadarColor(c.radarColor);
            setScanColor(c.scanColor);
            setBulletColor(c.bulletColor);
        }
    }
}