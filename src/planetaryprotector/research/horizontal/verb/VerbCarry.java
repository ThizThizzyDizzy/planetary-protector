package planetaryprotector.research.horizontal.verb;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalVerb;
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
        Core.drawOval(x-w/2, y, w/5, h, h/20, 200, 0, 39, 61);//left shape thing
        Core.drawOval(x+w/2, y, w/5, h, h/20, 200, 0, -61, -39);//right shape thing
    }
}