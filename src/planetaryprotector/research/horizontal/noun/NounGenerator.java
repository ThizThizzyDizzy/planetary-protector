package planetaryprotector.research.horizontal.noun;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalNoun;
public class NounGenerator extends HorizontalNoun{
    @Override
    public float getWidth(float size){
        return size*2.5f;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    protected void render(float x, float y, float w, float h){
        Renderer.fillHollowCosGear(x, y, h/4, h/2, h/20, 16, 0, 3);//gear
        Renderer.fillHollowRegularPolygon(x, y, 100, w/6-h/28, h/2-h/28, w/6, h/2);//small oval
        Renderer.fillHollowRegularPolygon(x, y, 100, w/4-h/22, h/2-h/22, w/4, h/2);//mid oval
        Renderer.fillHollowRegularPolygon(x, y, 100, w/2.75f-h/16, h/2-h/16, w/2.75f, h/2);//large oval
        Core.drawHorizontalLine(x-w/2, y, x-h/4, h/20, 0);//left line
        Core.drawHorizontalLine(x+h/4, y, x+w/2, h/20, 0);//right line
    }
}