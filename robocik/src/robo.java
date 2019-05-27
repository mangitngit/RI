import robocode.AdvancedRobot;
import robocode.HitWallEvent;
import static robocode.util.Utils.normalAbsoluteAngle;
import static robocode.util.Utils.normalRelativeAngle;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class robo extends AdvancedRobot {
    private Random random = new Random();
    public void run() {
        setColors(Color.red, Color.green, Color.yellow);
        while(true){
            ahead(20);
        }
    }

    @Override
    public void onPaint(Graphics2D g) {
        super.onPaint(g);
        g.setColor(Color.yellow);
        g.drawString(String.valueOf("X: "+(int)(float)this.getX()), (float)this.getX()+35, (float)this.getY());
        g.drawString(String.valueOf("Y: "+(int)(float)this.getY()), (float)this.getX()+35, (float)this.getY()-15);
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        super.onHitWall(event);

        if(random.nextBoolean())
            turnLeft(random.nextInt(90)+90);
        else
            turnRight(random.nextInt(90)+90);
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        super.onMouseClicked(e);
        double aimX = e.getX();
        double aimY = e.getY();
        double angle = normalAbsoluteAngle(Math.atan2(aimX - this.getX(), aimY - this.getY()));
        setTurnRightRadians(normalRelativeAngle(angle - getGunHeadingRadians()));
    }

}

