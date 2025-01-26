package planetaryprotector.research.horizontal.adverb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalAdverb;
public class AdverbUnknown extends HorizontalAdverb{
    private final String adverb;
    public AdverbUnknown(String adverb){
        this.adverb = adverb;
    }
    @Override
    public float getWidth(float size){
        return (Renderer.getStringWidth(adverb, size-20)+40)*2;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    public void render(float x, float y, float w, float h){
        Renderer.fillHollowRegularPolygon(x, y, 100, w/2-h/13, h/2-h/13, w/2, h/2);//outer oval
        h -= 20;
        float length = Renderer.getStringWidth(adverb, h);
        Renderer.drawCenteredText(x-length/2, y-h/2, x+length/2, y+h/2, adverb);
    }
}
