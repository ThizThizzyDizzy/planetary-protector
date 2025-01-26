package planetaryprotector.research.horizontal.tense;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalTense;
import planetaryprotector.research.lang.Lang.Tense;
public class TenseUnknown extends HorizontalTense{
    private final Tense tense;
    public TenseUnknown(Tense tense){
        this.tense = tense;
    }
    @Override
    protected void render(float x, float y, float size){
        size -= 20;
        float length = Renderer.getStringWidth(tense.toString(), size);
        Renderer.drawCenteredText(x-length/2, y-size/2, x+length/2, y+size/2, tense.toString());
    }
}
