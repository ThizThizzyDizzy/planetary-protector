package planetaryprotector.game;
import java.util.List;
import java.util.Random;
import planetaryprotector.GameObject;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.Skyscraper;
public class BoundingBox{
    public static BoundingBox enclosing(List<? extends GameObject> objects){
        if(objects.isEmpty())return null;
        int x1,y1,x2,y2;
        x1 = x2 = objects.get(0).x;
        y1 = y2 = objects.get(0).y;
        for(GameObject o : objects){
            int left = o.x;
            int top = o.y;
            int right = o.x+o.width;
            int bottom = o.y+o.height;
            if(o instanceof Structure){
                int oy = o.y-((Structure) o).getStructureHeight();
                if(oy<y1)y1 = oy;
            }
            if(o instanceof Skyscraper){
                Skyscraper sky = (Skyscraper)o;
                bottom = sky.y+sky.height-sky.fallen;
            }
            if(left<x1)x1 = left;
            if(top<y1)y1 = top;
            if(right>x2)x2 = right;
            if(bottom>y2)y2 = bottom;
        }
        return new BoundingBox(x1, y1, x2-x1, y2-y1);
    }
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public BoundingBox(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public int randX(Random rand){
        return (int)(x+Math.round(rand.nextDouble()*width));
    }
    public int randY(Random rand){
        return (int)(y+Math.round(rand.nextDouble()*height));
    }
    public BoundingBox expand(int amount){
        return new BoundingBox(x-amount, y-amount, width+amount*2, height+amount*2);
    }
    public int area(){
        return width*height;
    }
    public int[] getCenter(){
        return new int[]{getCenterX(), getCenterY()};
    }
    public int getCenterX(){
        return x+width/2;
    }
    public int getCenterY(){
        return y+height/2;
    }
    public int getLeft(){
        return x;
    }
    public int getTop(){
        return y;
    }
    public int getRight(){
        return x+width;
    }
    public int getBottom(){
        return y+height;
    }
    public BoundingBox expandLeft(int expansion){
        return new BoundingBox(x-expansion, y, width+expansion, height);
    }
    public BoundingBox expandUp(int expansion){
        return new BoundingBox(x, y-expansion, width, height+expansion);
    }
    public BoundingBox expandRight(int expansion){
        return new BoundingBox(x, y, width+expansion, height);
    }
    public BoundingBox expandDown(int expansion){
        return new BoundingBox(x, y, width, height+expansion);
    }
}