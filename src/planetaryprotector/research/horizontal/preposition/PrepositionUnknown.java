package planetaryprotector.research.horizontal.preposition;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalPreposition;
public class PrepositionUnknown extends HorizontalPreposition{
    private final String preposition;
    public PrepositionUnknown(String preposition){
        this.preposition = preposition;
    }
    @Override
    public void draw(float x, float y, float w, float h){
        h-=20;
        float length = Renderer.getStringWidth(preposition, h);
        Renderer.drawCenteredText(x-length/2, y-h/2, x+length/2, y+h/2, preposition);
    }
}