package planetaryprotector.research.horizontal;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
public abstract class HorizontalVerb{
    private HorizontalPreposition preposition;
    private HorizontalTense tense;
    private ArrayList<HorizontalAdverb> adverbs = new ArrayList<>();
    public HorizontalVerb setPreposition(HorizontalPreposition preposition){
        this.preposition = preposition;
        return this;
    }
    public HorizontalVerb setTense(HorizontalTense tense){
        this.tense = tense;
        return this;
    }
    public HorizontalVerb addAdverb(HorizontalAdverb adverb){
        adverbs.add(adverb);
        return this;
    }
    public abstract float getWidth(float size);
    public abstract float getHeight(float size);
    public final void draw(float x, float y, float size){
        if(tense!=null)tense.draw(x, y, size);
        float w = getWidth(size);
        float h = getHeight(size);
        Renderer.fillHollowRegularPolygon(x, y, 100, w/2-size/13, h/2-size/13, w/2, h/2);//outer oval
        Core.drawOval(x-w/2, y, w/10, h/3, size/15, 100, 0, 0, 13);//left connection oval top
        Core.drawOval(x-w/2, y, w/10, h/3, size/15, 100, 0, 37, 50);//left connection oval bottom
        Core.drawOval(x+w/2, y, w/10, h/3, size/15, 100, 0, -13, 0);//right connection oval top
        Core.drawOval(x+w/2, y, w/10, h/3, size/15, 100, 0, -50, -37);//right connection oval bottom
        Renderer.fillHollowRegularPolygon(x, y, 50, h/4-size/20, h/4-size/20, h/4, h/4);//Tense container
        render(x, y, w, h);
        if(preposition!=null){
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, 0);
            preposition.draw(getWidth(size)/2, getHeight(size)/2, w, h);
            GL11.glPushMatrix();
            GL11.glScaled(-1, 1, 1);
            preposition.draw(getWidth(size)/2, getHeight(size)/2, w, h);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScaled(1, -1, 1);
            preposition.draw(getWidth(size)/2, getHeight(size)/2, w, h);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScaled(-1, -1, 1);
            preposition.draw(getWidth(size)/2, getHeight(size)/2, w, h);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
        float Y = 0;
        float lastSize = getHeight(size);
        for(HorizontalAdverb adverb : adverbs){
            Y += lastSize/2+adverb.getHeight(size)/2;
            lastSize = adverb.getHeight(size);
            adverb.draw(x, y+Y, size);
            GL11.glPushMatrix();
            GL11.glScaled(1, -1, 1);
            adverb.draw(x, y+Y, size);
            GL11.glPopMatrix();
        }
    }
    protected abstract void render(float x, float y, float w, float h);
}
