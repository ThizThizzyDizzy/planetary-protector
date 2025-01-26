package planetaryprotector.research.horizontal.adverb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalAdverb;
public class AdverbFaster extends HorizontalAdverb{
    @Override
    public float getWidth(float size){
        return size*2.5f;
    }
    @Override
    public float getHeight(float size){
        return size*.8f;
    }
    @Override
    public void render(float x, float y, float w, float h){
        Renderer.fillHollowRegularPolygon(x, y, 100, w/2-h/13, h/2-h/13, w/2, h/2);//outer oval
        Core.drawHollowCosGear(x, y, h/4, h/2, h/50, 6, 0, 0, 3);//gear
        //left ovals
        Core.drawOval(x+w/6, y, w/3, h/2, h/28, 100, 0, 55, 94);//small oval
        Core.drawOval(x+w/4, y, w/2, h/2, h/22, 100, 0, 55, 94);//mid oval
        Core.drawOval(x+w/2.75, y, w/1.375, h/2, h/16, 100, 0, 55, 94);//large oval
        //right ovals
        Core.drawOval(x-w/6, y, w/3, h/2, h/28, 100, 0, 5, 44);//small oval
        Core.drawOval(x-w/4, y, w/2, h/2, h/22, 100, 0, 5, 44);//mid oval
        Core.drawOval(x-w/2.75, y, w/1.375, h/2, h/16, 100, 0, 5, 44);//large oval
    }
}
