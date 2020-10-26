package planetaryprotector.research.horizontal.verb;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalVerb;
public class VerbInfuse extends HorizontalVerb{
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
        Core.drawHollowCosGear(x, y, h/4, h/2, h/20, 12, 0, 0, 3);//gear
        Core.drawHorizontalLine(x-w/2, y, x-w/4, h/40, 0);//outer left horizontal line
        Core.drawHorizontalLine(x+w/4, y, x+w/2, h/40, 0);//outer right horizontal line
        Core.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, -30,-29);//outer top left diagonal line
        Core.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, 29,30);//outer top right diagonal line
        Core.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, 129,130);//outer bottom left diagonal line
        Core.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, 70,71);//outer bottom right diagonal line
    }
}