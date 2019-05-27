/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import java.awt.event.MouseEvent;
import java.lang.Math;
import java.awt.geom.Point2D;
import java.util.*;
/**
 *
 * @author franek
 */


public class robo2 extends Robot{

    private double x = 0;
    private double y = 0;
    private double angle = 0;
    private double heading;
    private double upper_wall_distance;
    private double right_wall_distance;
    private Boolean no_obstacles;
    private int scannedX = Integer.MIN_VALUE;
    private int scannedY = Integer.MIN_VALUE;
    int robot_size = 25;
    int rozmiar_pola = 25;
    int current_field;

    List<Point2D> obstacleList = new ArrayList<Point2D>();
    List<Rectangle> mapFieldsList = new ArrayList<Rectangle>();
    List<Boolean> mapBoolList = new ArrayList<Boolean>();
    List<Integer> wayToDestination = new ArrayList<Integer>();

    public void run() {

        init();
        turnRadarRight(360);
        while (true) {

            search_obstacles();



            if (no_obstacles){
                doNothing();
            }
            else{
                turnRight(Math.random()*100);
                no_obstacles = true;
            }
        }
    }

    public void search_obstacles(){

        x = this.getX();
        y = this.getY();
        heading = this.getHeading();

        upper_wall_distance = this.getBattleFieldHeight() - y;
        right_wall_distance = this.getBattleFieldWidth() - x;

        if(upper_wall_distance < 70 && (heading > 270 || heading < 90))
            no_obstacles = false;
        else if (y < 70 && heading > 90 && heading < 270)
            no_obstacles = false;
        else if (right_wall_distance < 70 && heading > 0 && heading < 180)
            no_obstacles = false;
        else if (x < 70 && heading > 180 && heading < 360)
            no_obstacles = false;
        else
            no_obstacles = true;

    }

    public void init(){

        for(int i=0; i<this.getBattleFieldWidth(); i += rozmiar_pola)
            for(int j=0; j<this.getBattleFieldHeight(); j += rozmiar_pola){
                mapFieldsList.add(new Rectangle(i, j, rozmiar_pola,rozmiar_pola));
            }

        for(int i=0; i<mapFieldsList.size(); i++){
            mapBoolList.add(true);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);

        scannedX = (int)(getX() + Math.sin(angle) * e.getDistance());
        scannedY = (int)(getY() + Math.cos(angle) * e.getDistance());

        Rectangle actualRect = new Rectangle(scannedX-robot_size/2, scannedY-robot_size/2, robot_size, robot_size);

        for(int i=0; i<mapFieldsList.size(); i++){
            if(mapFieldsList.get(i).intersects(actualRect))
                mapBoolList.set(i, false);
        }
    }

    public void onMousePressed(MouseEvent e){

        double mouse_x = e.getX();
        double mouse_y = e.getY();

        x = this.getX();
        y = this.getY();
        heading = this.getHeading();

        angle = Math.atan2(mouse_x - x, mouse_y - y);
        angle = Math.toDegrees(angle);

        if(angle < 0)
            angle += 360;

        wayToDestination.clear();
        double rotation_angle = angle - heading;
        double ahead_distance = Math.sqrt((mouse_x - x)*(mouse_x - x) + (mouse_y - y)*(mouse_y - y));
        double distance_left = ahead_distance;
        double best_choice = Double.MAX_VALUE;
        current_field = 0;
        int next_field = 0;

        for(int i=0; i<mapFieldsList.size(); i++){
            if(mapFieldsList.get(i).contains(x, y)){
                current_field = i;
                break;
            }
        }

        while(best_choice>20){
            boolean lewo = true, prawo= true, gora = true, dol = true;
            int lower_field = current_field - 1;
            int upper_field = current_field + 1;
            int left_field = current_field - (int)this.getBattleFieldHeight()/rozmiar_pola;
            int right_field = current_field + (int)this.getBattleFieldHeight()/rozmiar_pola;


            distance_left = Math.sqrt((mouse_x - mapFieldsList.get(right_field).x)*
                    (mouse_x - mapFieldsList.get(right_field).x) +
                    (mouse_y -  mapFieldsList.get(right_field).y)*
                            (mouse_y -  mapFieldsList.get(right_field).y));

            if(best_choice > distance_left && mapBoolList.get(right_field) == true){
                best_choice = distance_left;
                next_field = right_field;
                if (prawo) {
                    ahead(25);
                    turnRight(90);
                    prawo = false;
                    lewo = gora = dol = true;
                }
                ahead(25);
            }


            distance_left = Math.sqrt((mouse_x - mapFieldsList.get(upper_field).x)*
                    (mouse_x - mapFieldsList.get(upper_field).x) +
                    (mouse_y -  mapFieldsList.get(upper_field).y)*
                            (mouse_y -  mapFieldsList.get(upper_field).y));

            if(best_choice > distance_left && mapBoolList.get(upper_field) == true){
                best_choice = distance_left;
                next_field = upper_field;
                ahead(25);
            }

            distance_left = Math.sqrt((mouse_x - mapFieldsList.get(left_field).x)*
                    (mouse_x - mapFieldsList.get(left_field).x) +
                    (mouse_y -  mapFieldsList.get(left_field).y)*
                            (mouse_y -  mapFieldsList.get(left_field).y));

            if(best_choice > distance_left && mapBoolList.get(left_field) == true){
                best_choice = distance_left;
                next_field = left_field;
                if (lewo) {
                    ahead(25);
                    turnLeft(90);
                    lewo = false;
                    prawo = gora = dol = true;
                }
                ahead(25);
            }

            distance_left = Math.sqrt((mouse_x - mapFieldsList.get(lower_field).x)*
                    (mouse_x - mapFieldsList.get(lower_field).x) +
                    (mouse_y -  mapFieldsList.get(lower_field).y)*
                            (mouse_y -  mapFieldsList.get(lower_field).y));

            if(best_choice > distance_left && mapBoolList.get(lower_field) == true){
                best_choice = distance_left;
                next_field = lower_field;
                if (dol) {
                    ahead(25);
                    turnRight(90);
                    dol = false;
                    lewo = gora = prawo = true;
                }
                ahead(25);
            }

            current_field = next_field;
            wayToDestination.add(current_field);
        }
        //turnRight(rotation_angle);
        //ahead(ahead_distance);
    }

    public void onPaint(Graphics2D g) {


        g.setColor(java.awt.Color.GREEN);
        for (int i=0; i<mapBoolList.size(); i++){
            if(!mapBoolList.get(i)){
                g.setColor(new Color(255, 0, 0, 100));
                g.fillRect(mapFieldsList.get(i).x, mapFieldsList.get(i).y, mapFieldsList.get(i).height, mapFieldsList.get(i).width);
            }
            else{
                g.setColor(new Color(255, 255, 255, 0));
                g.fillRect(mapFieldsList.get(i).x, mapFieldsList.get(i).y, mapFieldsList.get(i).height, mapFieldsList.get(i).width);
            }
        }

        for(int i=0; i<wayToDestination.size(); i++){
            g.setColor(new Color(255, 255, 0));
            g.fillRect(mapFieldsList.get(wayToDestination.get(i)).x, mapFieldsList.get(wayToDestination.get(i)).y, rozmiar_pola, rozmiar_pola);
        }
        //g.setColor(new Color(255, 0, 255, 50));
        //g.fillRect(scannedX-robot_size/2, scannedY-robot_size/2, robot_size, robot_size);
    }
}

