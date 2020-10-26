package planetaryprotector.research.horizontal.verb;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalVerb;
public class VerbOperate extends HorizontalVerb{
    @Override
    public double getWidth(double size){
        return size*3.5;
    }
    @Override
    public double getHeight(double size){
        return size;
    }
    @Override
    protected void render(double x, double y, double w, double h){
        Core.drawHollowCosGear(x, y, h/4, h/2, h/14, 8, 0, 0, 3);//gear
        Core.drawHorizontalLine(x-w*.49, y-h/12, x-h/5, h/24, 0);//top left line
        Core.drawHorizontalLine(x+h/5, y-h/12, x+w*.49, h/24, 0);//top right line
        Core.drawHorizontalLine(x-w*.49, y+h/12, x-h/5, h/24, 0);//bottom left line
        Core.drawHorizontalLine(x+h/5, y+h/12, x+w*.49, h/24, 0);//bottom right line
    }
}