package planetaryprotector.research.horizontal.adjective;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalAdjective;
public class AdjectiveSolar extends HorizontalAdjective{
    @Override
    public float getWidth(float size){
        return size*2.5f;
    }
    @Override
    public float getHeight(float size){
        return size*.625f;
    }
    @Override
    public void render(float x, float y, float w, float h){
        Core.drawHollowCosGear(x, y, h/4, h/2, h/20, 16, 0, 0, 3);//gear
        Renderer.fillHollowRegularPolygon(x, y, 100, w/6-h/32, h/2-h/32, w/6, h/2);//small oval
        Renderer.fillHollowRegularPolygon(x, y, 100, w/4-h/26, h/2-h/26, w/4, h/2);//mid oval
        Renderer.fillHollowRegularPolygon(x, y, 100, w/2.75f-h/20, h/2-h/20, w/2.75f, h/2);//large oval
    }
}
