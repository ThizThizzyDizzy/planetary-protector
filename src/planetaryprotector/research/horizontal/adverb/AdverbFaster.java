package planetaryprotector.research.horizontal.adverb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
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
        Renderer.fillHollowCosGear(x, y, h/4, h/2, h/50, 6, 0, 3);//gear
        //left ovals
        Renderer.fillHollowRegularPolygonSegment(x+w/6, y, 100, w/3-h/28, h/2-h/28, w/3, h/2, 55, 94);//small oval
        Renderer.fillHollowRegularPolygonSegment(x+w/4, y, 100, w/2-h/22, h/2-h/22, w/2, h/2, 55, 94);//mid oval
        Renderer.fillHollowRegularPolygonSegment(x+w/2.75f, y, 100, w/1.375f-h/16, h/2-h/16, w/1.375f, h/2, 55, 94);//large oval
        //right ovals
        Renderer.fillHollowRegularPolygonSegment(x-w/6, y, 100, w/3-h/28, h/2-h/28, w/3, h/2, 5, 44);//small oval
        Renderer.fillHollowRegularPolygonSegment(x-w/4, y, 100, w/2-h/22, h/2-h/22, w/2, h/2, 5, 44);//mid oval
        Renderer.fillHollowRegularPolygonSegment(x-w/2.75f, y, 100, w/1.375f-h/16, h/2-h/16, w/1.375f, h/2, 5, 44);//large oval
    }
}
