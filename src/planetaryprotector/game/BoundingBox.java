package planetaryprotector.game;
import java.util.List;
import java.util.Random;
import planetaryprotector.GameObject;
public class BoundingBox{
    public static BoundingBox enclosing(List<? extends GameObject> objects, boolean includeHeight){
        if(objects.isEmpty())return null;
        int x1, y1, x2, y2;
        x1 = x2 = objects.get(0).x;
        y1 = y2 = objects.get(0).y;
        for(GameObject o : objects){
            var bbox = o.getBoundingBox(includeHeight);
            x1 = Math.min(x1, bbox.x);
            y1 = Math.min(y1, bbox.y);
            x2 = Math.max(x2, bbox.x+bbox.width);
            y2 = Math.max(y2, bbox.y+bbox.height);
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
    public boolean contains(int x, int y){
        return x>=this.x&&y>=this.y&&x<this.x+width&&y<this.y+height;
    }
    public boolean intersects(BoundingBox other){
        return intersection(other)!=null;
    }
    public BoundingBox intersection(BoundingBox other){
        int newX1 = Math.max(x, other.x);
        int newY1 = Math.max(y, other.y);
        int newX2 = Math.min(x+width, other.x+other.width);
        int newY2 = Math.min(y+height, other.y+other.height);
        if(newX2<=newX1||newY2<=newY1)return null;
        return new BoundingBox(newX1, newY1, newX2-newX1, newY2-newY1);
    }
}
