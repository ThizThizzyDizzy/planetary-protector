package planetaryprotector.research.horizontal;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.Renderer2D;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public abstract class HorizontalVerb extends Renderer2D{
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
    public abstract double getWidth(double size);
    public abstract double getHeight(double size);
    public final void draw(double x, double y, double size){
        if(tense!=null)tense.draw(x, y, size);
        double w = getWidth(size);
        double h = getHeight(size);
        AdvancedRenderer2D.drawOval(x, y, w/2, h/2, size/13, 100, 0);//outer oval
        AdvancedRenderer2D.drawOval(x-w/2, y, w/10, h/3, size/15, 100, 0, 0, 13);//left connection oval top
        AdvancedRenderer2D.drawOval(x-w/2, y, w/10, h/3, size/15, 100, 0, 37, 50);//left connection oval bottom
        AdvancedRenderer2D.drawOval(x+w/2, y, w/10, h/3, size/15, 100, 0, -13, 0);//right connection oval top
        AdvancedRenderer2D.drawOval(x+w/2, y, w/10, h/3, size/15, 100, 0, -50, -37);//right connection oval bottom
        AdvancedRenderer2D.drawOval(x, y, h/4, h/4, size/20, 50, 0);//Tense container
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
        double Y = 0;
        double lastSize = getHeight(size);
        for(HorizontalAdverb adverb : adverbs){
            Y+=lastSize/2+adverb.getHeight(size)/2;
            lastSize = adverb.getHeight(size);
            adverb.draw(x, y+Y, size);
            GL11.glPushMatrix();
            GL11.glScaled(1, -1, 1);
            adverb.draw(x, y+Y, size);
            GL11.glPopMatrix();
        }
    }
    protected abstract void render(double x, double y, double w, double h);
}