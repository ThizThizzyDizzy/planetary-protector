package planetaryprotector.research.horizontal.verb;
import planetaryprotector.research.horizontal.HorizontalVerb;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class VerbCarry extends HorizontalVerb{
    @Override
    public double getWidth(double size){
        return size*2.5;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    protected void render(double x, double y, double w, double h){
        AdvancedRenderer2D.drawOval(x-w/2, y, w/5, h, h/20, 200, 0, 39, 61);//left shape thing
        AdvancedRenderer2D.drawOval(x+w/2, y, w/5, h, h/20, 200, 0, -61, -39);//right shape thing
    }
}