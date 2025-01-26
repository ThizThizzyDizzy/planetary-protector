package planetaryprotector.research.horizontal.verb;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalVerb;
public class VerbUnknown extends HorizontalVerb{
    private final String verb;
    public VerbUnknown(String verb){
        this.verb = verb;
    }
    @Override
    public float getWidth(float size){
        return (Renderer.getStringWidth(verb, size-10)+20)*2;
    }
    @Override
    public float getHeight(float size){
        return size;
    }
    @Override
    public void render(float x, float y, float w, float h){
        h -= 10;
        float length = Renderer.getStringWidth(verb, h);
        Renderer.drawCenteredText(x-length/2, y-h/2, x+length/2, y+h/2, verb);
    }
}
