package planetaryprotector.research.horizontal.adverb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalAdverb;
public class AdverbLess extends HorizontalAdverb{
    @Override
    public float getWidth(float size){
        return size*2.5f;
    }
    @Override
    public float getHeight(float size){
        return size*.75f;
    }
    @Override
    public void render(float x, float y, float w, float h){
        Renderer.fillHollowCosGear(x, y-h/12, h/4, h/2, h/50, 3, 60, 3);//gear
    }
}
