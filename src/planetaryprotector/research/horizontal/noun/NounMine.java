package planetaryprotector.research.horizontal.noun;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalNoun;
public class NounMine extends HorizontalNoun{
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
        Renderer.fillHollowCosGear(x, y, h/4, h/2, h/50, 6, 0, 3);//gear
        //right
        Renderer.fillHollowRegularPolygonSegment(x+w/3, y, 100, w/6-h/28, h/2-h/28, w/6, h/2, 58, 91);//small oval
        Renderer.fillHollowRegularPolygonSegment(x+w/2, y, 100, w/4-h/22, h/2-h/22, w/4, h/2, 62, 87);//mid oval
        Renderer.fillHollowRegularPolygonSegment(x+w/1.4375f, y, 100, w/2.75f-h/16, h/2-h/16, w/2.75f, h/2, 65, 84);//large oval
        //left
        Renderer.fillHollowRegularPolygonSegment(x-w/3, y, 100, w/6-h/28, h/2-h/28, w/6, h/2, 8, 41);//small oval
        Renderer.fillHollowRegularPolygonSegment(x-w/2, y, 100, w/4-h/22, h/2-h/22, w/4, h/2, 12, 37);//mid oval
        Renderer.fillHollowRegularPolygonSegment(x-w/1.4375f, y, 100, w/2.75f-h/16, h/2-h/16, w/2.75f, h/2, 15, 34);//large oval
    }
}
