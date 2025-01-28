package planetaryprotector.research.horizontal.verb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalVerb;
public class VerbConsumeElectricity extends HorizontalVerb{
    @Override
    public float getWidth(float size){
        return size*3.5f;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    protected void render(float x, float y, float w, float h){
        Renderer.fillHollowCosGear(x, y, h/4, h/2, h/20, 4, 0, 3);//gear
        Renderer.drawHorizontalLine(x-w/2, y, x-h/4, h/20, 0);//left line
        Renderer.drawHorizontalLine(x+h/4, y, x+w/2, h/20, 0);//right line
        //right ovals
        //top ovals
        Core.drawOval(x, y-h/2, w/6, h/2, h/28, 100, 0, 27, 43);//small oval
        Core.drawOval(x, y-h/2, w/4, h/2, h/22, 100, 0, 29, 45);//mid oval
        Core.drawOval(x, y-h/2, w/2.75, h/2, h/16, 100, 0, 31, 45);//large oval
        //bottom ovals
        Core.drawOval(x, y+h/2, w/6, h/2, h/28, 100, 0, 77, 93);//small oval
        Core.drawOval(x, y+h/2, w/4, h/2, h/22, 100, 0, 79, 95);//mid oval
        Core.drawOval(x, y+h/2, w/2.75, h/2, h/16, 100, 0, 81, 95);//large oval
        //left ovals
        //top ovals
        Core.drawOval(x, y-h/2, w/6, h/2, h/28, 100, 0, 57, 73);//small oval
        Core.drawOval(x, y-h/2, w/4, h/2, h/22, 100, 0, 55, 71);//mid oval
        Core.drawOval(x, y-h/2, w/2.75, h/2, h/16, 100, 0, 55, 69);//large oval
        //bottom ovals
        Core.drawOval(x, y+h/2, w/6, h/2, h/28, 100, 0, 7, 23);//small oval
        Core.drawOval(x, y+h/2, w/4, h/2, h/22, 100, 0, 5, 21);//mid oval
        Core.drawOval(x, y+h/2, w/2.75, h/2, h/16, 100, 0, 5, 19);//large oval
    }
}
