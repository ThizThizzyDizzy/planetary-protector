package planetaryprotector.research.horizontal.noun;
import planetaryprotector.research.horizontal.HorizontalNoun;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class NounWorker extends HorizontalNoun{
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
        AdvancedRenderer2D.drawOval(x, y, w/4, h/4, h/24, 100, 0);//inner oval
        AdvancedRenderer2D.drawOval(x, y+h/2, w/2, h/2, h/28, 200, 0, -15, 15);//top inner piece
        AdvancedRenderer2D.drawOval(x, y-h/2, w/2, h/2, h/28, 200, 0, 85, 115);//bottom inner piece
        AdvancedRenderer2D.drawOval(x+w/8, y, w/4, h/2, h/40, 100, 0, 10, 40);//right outer piece
        AdvancedRenderer2D.drawOval(x-w/8, y, w/4, h/2, h/40, 100, 0, 60, 90);//left outer piece
        AdvancedRenderer2D.drawVerticalLine(x, y-h/4, y+h/4, h/40, 0);//inner vertical line
        AdvancedRenderer2D.drawHorizontalLine(x-w/2, y, x-w/4, h/40, 0);//outer left horizontal line
        AdvancedRenderer2D.drawHorizontalLine(x+w/4, y, x+w/2, h/40, 0);//outer right horizontal line
        AdvancedRenderer2D.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, -30,-29);//outer top left diagonal line
        AdvancedRenderer2D.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, 29,30);//outer top right diagonal line
        AdvancedRenderer2D.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, 129,130);//outer bottom left diagonal line
        AdvancedRenderer2D.drawOval(x, y, w/2, h/2, w/4, h/4, 200, 0, 70,71);//outer bottom right diagonal line
    }
}