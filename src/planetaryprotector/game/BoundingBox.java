package planetaryprotector.game;
import java.util.List;
import java.util.Random;
import planetaryprotector.GameObject;
import planetaryprotector.structure.Structure;
public class BoundingBox{
    public static BoundingBox enclosing(List<? extends GameObject> objects){
        if(objects.isEmpty())return null;
        double x1,y1,x2,y2;
        x1 = x2 = objects.get(0).x;
        y1 = y2 = objects.get(0).y;
        for(GameObject o : objects){
            if(o.x<x1)x1 = o.x;
            if(o.x+o.width>x2)x2 = o.x+o.width;
            if(o.y+o.height>y2)y2 = o.y+o.height;
            if(o instanceof Structure){
                double oy = o.y-((Structure) o).getStructureHeight();
                if(oy<y1)y1 = oy;
            }
        }
        return new BoundingBox(x1, y1, x2-x1, y2-y1);
    }
    public final double x;
    public final double y;
    public final double width;
    public final double height;
    public BoundingBox(double x, double y, double width, double height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public double randX(Random rand){
        return x+rand.nextDouble()*width;
    }
    public double randY(Random rand){
        return y+rand.nextDouble()*height;
    }
    public BoundingBox expand(double amount){
        return new BoundingBox(x-amount, y-amount, width+amount*2, height+amount*2);
    }
    public double area(){
        return width*height;
    }
}