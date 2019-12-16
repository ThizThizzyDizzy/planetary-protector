package planetaryprotector.research.horizontal.verb;
import planetaryprotector.research.horizontal.HorizontalVerb;
import simplelibraryextended.opengl.AdvancedRenderer2D;
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
        AdvancedRenderer2D.drawHollowCosGear(x, y, h/4, h/2, h/14, 8, 0, 0, 3);//gear
        AdvancedRenderer2D.drawHorizontalLine(x-w*.49, y-h/12, x-h/5, h/24, 0);//top left line
        AdvancedRenderer2D.drawHorizontalLine(x+h/5, y-h/12, x+w*.49, h/24, 0);//top right line
        AdvancedRenderer2D.drawHorizontalLine(x-w*.49, y+h/12, x-h/5, h/24, 0);//bottom left line
        AdvancedRenderer2D.drawHorizontalLine(x+h/5, y+h/12, x+w*.49, h/24, 0);//bottom right line
    }
}