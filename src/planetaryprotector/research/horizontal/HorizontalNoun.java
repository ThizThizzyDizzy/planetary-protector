package planetaryprotector.research.horizontal;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
public abstract class HorizontalNoun{
    private HorizontalNoun[] otherNouns = new HorizontalNoun[0];
    private HorizontalArticle article;
    private boolean plural;
    private ArrayList<HorizontalAdjective> adjectives = new ArrayList<>();
    public HorizontalNoun setNouns(HorizontalNoun... otherNouns){
        this.otherNouns = otherNouns;
        return this;
    }
    public HorizontalNoun setArticle(HorizontalArticle article){
        this.article = article;
        return this;
    }
    public HorizontalNoun setPlural(boolean plural){
        this.plural = plural;
        return this;
    }
    public HorizontalNoun addAdjective(HorizontalAdjective adjective){
        adjectives.add(adjective);
        return this;
    }
    public abstract float getWidth(float size);
    public abstract float getHeight(float size);
    public final void draw(float x, float y, float size){
        if(article!=null)article.draw(x, y, size);
        float w = getWidth(size);
        float h = getHeight(size);
        Renderer.fillHollowRegularPolygon(x, y, 100, w/2-size/12, h/2-size/12, w/2, h/2);//outer oval
        Renderer.fillHollowRegularPolygonSegment(x-w/2, y, 100, w/10-size/15, h/3-size/15, w/10, h/3, 0, 13);//left connection oval top
        Renderer.fillHollowRegularPolygonSegment(x-w/2, y, 100, w/10-size/15, h/3-size/15, w/10, h/3, 37, 50);//left connection oval bottom
        Renderer.fillHollowRegularPolygonSegment(x+w/2, y, 100, w/10-size/15, h/3-size/15, w/10, h/3, -13, 0);//right connection oval top
        Renderer.fillHollowRegularPolygonSegment(x+w/2, y, 100, w/10-size/15, h/3-size/15, w/10, h/3, -50, -37);//right connection oval bottom
        render(x, y, w, h);
        if(plural){
            Renderer.fillHollowRegularPolygonSegment(x, y, 360, w/2+h/5, h/2+h/5, w/2, h/2, 44, 46);
            Renderer.fillHollowRegularPolygonSegment(x, y, 360, w/2+h/5, h/2+h/5, w/2, h/2, 134, 136);
            Renderer.fillHollowRegularPolygonSegment(x, y, 360, w/2+h/5, h/2+h/5, w/2, h/2, 224, 226);
            Renderer.fillHollowRegularPolygonSegment(x, y, 360, w/2+h/5, h/2+h/5, w/2, h/2, -46, -44);
        }
        float Y = 0;
        float lastSize = getHeight(size);
        for(HorizontalNoun noun : otherNouns){
            Y += lastSize/2+noun.getHeight(size)/2;
            lastSize = noun.getHeight(size);
            noun.draw(x, y+Y, size);
            noun.draw(x, y-Y, size);
        }
        for(HorizontalAdjective adjective : adjectives){
            Y += lastSize/2+adjective.getHeight(size)/2;
            lastSize = adjective.getHeight(size);
            adjective.draw(x, y+Y, size);
            adjective.draw(x, y-Y, size);
        }
    }
    protected abstract void render(float x, float y, float w, float h);
}
