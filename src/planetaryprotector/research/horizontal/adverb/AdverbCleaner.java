package planetaryprotector.research.horizontal.adverb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalAdverb;
public class AdverbCleaner extends HorizontalAdverb{
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
        Renderer.fillHollowCosGear(x, y, h/4, h/2, h/50, 4, 0, 3);//gear
        Renderer.fillHollowRegularPolygon(x, y, 100, w/6-h/28, h/2-h/28, w/6, h/2);//small oval
        Renderer.fillHollowRegularPolygon(x, y, 100, w/4-h/22, h/2-h/22, w/4, h/2);//mid oval
        Renderer.fillHollowRegularPolygon(x, y, 100, w/2.75f-h/16, h/2-h/16, w/2.75f, h/2);//large oval
    }
}
