import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import java.awt.*;
import robocode.BattleRules;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import robocode.MouseClickedEvent;
import java.awt.event.MouseEvent;
import java.lang.Math;



public class robo5 extends AdvancedRobot
{
    boolean kolor = false;
    double Xcl=-1;
    double Ycl=-1;
    boolean skan = true;
    double x,y;
    double w,h;
    int sizeB = 25;
    int numW;
    int numH;
    int Nx;
    int Ny;
    boolean create = true;
    List<String> enem = new ArrayList<String>();
    List<Double> corX = new ArrayList<Double>();
    List<Double> corY = new ArrayList<Double>();

    List<Node> lOtw = new ArrayList<Node>();
    List<Node> lZam = new ArrayList<Node>();
    List<Point> trasa = new ArrayList<Point>();
    boolean[][] map;

    public void run()
    {

        w = getBattleFieldWidth();
        h = getBattleFieldHeight();
        numW = (int)Math.round(w / sizeB);
        numH = (int)Math.round(h / sizeB);
        map = new boolean[numH][numW];

        while(true)
        {

            if(create==true)
            {
                for (int i = 0; i<numH; i++)
                {
                    for (int j=0; j<numW; j++)
                    {
                        map[i][j] = false;
                    }

                }
                turnRadarRight(360);
                skan = false;
                create = false;
            }

            Random rn = new Random();
            int i = rn.nextInt(120) + 120;
            x = getX();
            y = getY();

            Nx =  (int)Math.round((int)x/sizeB);
            Ny =  (int)Math.round((int)y/sizeB);

            if(x<20 || x>w-20 || y<20 || y>h-20)
            {
                turnRight(i);
            }
            ahead(0);


        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if(skan == true)
        {
            if(!enem.contains(e.getName()))
            {
                enem.add(e.getName());
                double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);
                double scannedX = (double)(getX() + Math.sin(angle) * e.getDistance());
                double scannedY = (double)(getY() + Math.cos(angle) * e.getDistance());
                corX.add(scannedX);
                corY.add(scannedY);
                map[(int)Math.round((int)scannedY/sizeB)][(int)Math.round((int)scannedX/sizeB)] = true;

                map[(int)Math.round((int)(scannedY-16)/sizeB)][(int)Math.round((int)(scannedX-16)/sizeB)] = true;
                map[(int)Math.round((int)(scannedY-16)/sizeB)][(int)Math.round((int)(scannedX+16)/sizeB)] = true;
                map[(int)Math.round((int)(scannedY+16)/sizeB)][(int)Math.round((int)(scannedX-16)/sizeB)] = true;
                map[(int)Math.round((int)(scannedY+16)/sizeB)][(int)Math.round((int)(scannedX+16)/sizeB)] = true;

            }
        }
    }

    public void onMouseClicked(MouseEvent e)
    {
        Xcl = (int)Math.round((int)e.getX()/sizeB);
        Ycl = (int)Math.round((int)e.getY()/sizeB);
        lOtw.clear();
        lZam.clear();
        trasa.clear();

        Node Start = new Node(new Point(Nx,Ny), new Point(-1,-1),0);
        Node aktualny = Start;
        lZam.add(aktualny);
        while(aktualny.cor.x!=Xcl || aktualny.cor.y != Ycl)
        {

            searchNeigh(aktualny);

            int i;
            i = findLeast();
            aktualny = getElementFromList(lOtw.get(i).cor);
            lZam.add(getElementFromList(lOtw.get(i).cor));
            lOtw.remove(i);

        }
        findRoute();

    }

    private void findRoute()
    {
        int endX = (int)Xcl;
        int endY = (int)Ycl;
        Point punkt = new Point(endX, endY);

        while(!(punkt.x==Nx && punkt.y==Ny))
        {
            int j = getListIndex2(punkt);
            trasa.add(lZam.get(j).cor);
            punkt = lZam.get(j).rodzic;
        }
        punkt = new Point(Nx,Ny);
        trasa.add(punkt);


    }

    private int findLeast()
    {
        int index = -1;
        int F = 1000;
        for (int i = 0; i < lOtw.size(); i++)
        {
            if (lOtw.get(i).F < F)
            {
                F = lOtw.get(i).F;
                index = i;
            }
        }

        return index;
    }

    public void onPaint(Graphics2D g) {
        g.setColor(new Color(255,0,0,100));
        String xx = Double.toString(x);
        String yy = Double.toString(y);
        g.drawString("X: "+xx, 10, 20);
        g.drawString("Y: "+yy, 10, 10);

        String x2 = Double.toString(Xcl);
        String y2 = Double.toString(Ycl);
        g.drawString("x2: "+x2, 500, 20);
        g.drawString("y2: "+y2, 500, 10);

//        for(int i = 0; i<numH; i++)
//            g.drawLine(0, i*sizeB, (int)w, i*sizeB);
//        for(int j = 0; j<numW; j++)
//            g.drawLine(j*sizeB, 0, j*sizeB,(int)h);

        for(int i = 0; i<numH; i++)
        {
            for(int j = 0; j<numW; j++)
            {
                if(map[i][j]==true)
                {
                    g.fillRect(j*sizeB,i*sizeB,sizeB,sizeB);
                }
            }
        }
        g.setColor(new Color(0,255,0,200));
        for(int i=0; i<lOtw.size(); i++)
        {
            g.fillRect(lOtw.get(i).cor.x*sizeB,lOtw.get(i).cor.y*sizeB,sizeB,sizeB);
        }

        g.setColor(new Color(0,0,0,200));
        for(int i=0; i<lZam.size(); i++)
        {
            g.fillRect(lZam.get(i).cor.x*sizeB,lZam.get(i).cor.y*sizeB,sizeB,sizeB);
        }

        g.setColor(new Color(255,255,0,200));
        if(Xcl !=-1 && Ycl != -1)
            g.fillRect((int)Xcl*sizeB,(int)Ycl*sizeB,sizeB,sizeB);

        for(int i=0; i<trasa.size(); i++)
        {
            g.fillRect(trasa.get(i).x*sizeB,trasa.get(i).y*sizeB,sizeB,sizeB);
        }

    }
    private int getListIndex1(Point kor)
    {
        int index = -1;
        for (int i = 0; i < lOtw.size(); i++)
        {
            if (lOtw.get(i).cor.x == kor.x && lOtw.get(i).cor.y == kor.y)
            {
                index = i;
                break;
            }
        }
        return index;
    }

    private int getListIndex2(Point kor)
    {
        int index = -1;
        for (int i = 0; i < lZam.size(); i++)
        {
            if (lZam.get(i).cor.x == kor.x && lZam.get(i).cor.y == kor.y)
            {
                index = i;
                break;
            }
        }
        return index;
    }

    private Node getElementFromList(Point kor)
    {
        int index = -1;
        for (int i = 0; i < lOtw.size(); i++)
        {
            if (lOtw.get(i).cor == kor)
            {
                index = i;
                break;
            }
        }
        return lOtw.get(index);
    }


    private void searchNeigh(Node punkt)
    {
        int F = 5000;
        for(int i=-1; i<2; i++)
        {
            for(int j=-1; j<2; j++)
            {

                int g;
                if(i==0 || j==0)
                    g=punkt.G+10;
                else
                    g=punkt.G+14;
                int cX = punkt.cor.x+i;
                int cY = punkt.cor.y+j;
                Node newPunkt = new Node(new Point(cX,cY),punkt.cor,g);
                if(cX>=0 && cX<numW && cY>=0 && cY<numH)
                    if(!(i==0 && j==0))
                    {
                        if(map[cY][cX]==false)
                        {

                            if(lZam.size()!=0 && getListIndex2(newPunkt.cor)==-1)
                            {
                                if(getListIndex1(newPunkt.cor)==-1)
                                {
                                    lOtw.add(newPunkt);
                                }
                                else
                                {
                                    //System.out.print("Edytowanie otwartej \n");
                                    int ind = getListIndex1(newPunkt.cor);
                                    if(F<lOtw.get(ind).F)
                                        lOtw.set(ind,newPunkt);
                                }
                            }


                        }
                    }
            }
        }
    }


    class Node
    {
        int D = 10;

        Point cor;
        Point rodzic;
        int G;
        int H;
        int F;

        public Node(Point cor, Point rodzic, int G)
        {
            this.cor = cor;
            this.rodzic = rodzic;
            double zasieg = D * Math.sqrt(Math.pow((cor.x - Xcl),2)+Math.pow((cor.y - Ycl),2));
            this.H = (int)zasieg;
            this.G = G;
            this.F = H + G;
        }


    }

}