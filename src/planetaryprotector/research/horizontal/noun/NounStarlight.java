package planetaryprotector.research.horizontal.noun;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalNoun;
public class NounStarlight extends HorizontalNoun{
    @Override
    public float getWidth(float size){
        return size*3;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    protected void render(float x, float y, float w, float h){
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 100, w/12-h/32, h-h/32, w/12, h, 76, 23);//tiny oval
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 100, w/6-h/28, h-h/28, w/6, h, 76, 23);//small oval
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 100, w/4-h/24, h-h/24, w/4, h, 77, 23);//mid oval
        Renderer.fillHollowRegularPolygonSegment(x, y+h/2, 100, w/2.75f-h/21, h-h/21, w/2.75f, h, 78, 21);//large oval
    }
}
