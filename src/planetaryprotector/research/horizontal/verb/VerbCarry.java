package planetaryprotector.research.horizontal.verb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalVerb;
public class VerbCarry extends HorizontalVerb{
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
        Renderer.fillHollowRegularPolygonSegment(x-w/2, y, 200, w/5-h/20, h-h/20, w/5, h, 39, 61);//left shape thing
        Renderer.fillHollowRegularPolygonSegment(x+w/2, y, 200, w/5-h/20, h-h/20, w/5, h, -61, -39);//right shape thing
    }
}
