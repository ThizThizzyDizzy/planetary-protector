package planetaryprotector.research.horizontal;
import java.util.ArrayList;
import planetaryprotector.Core;
import simplelibrary.opengl.Renderer2D;
public abstract class HorizontalNoun extends Renderer2D{
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
    public abstract double getWidth(double size);
    public abstract double getHeight(double size);
    public final void draw(double x, double y, double size){
        if(article!=null)article.draw(x, y, size);
        double w = getWidth(size);
        double h = getHeight(size);
        Core.drawOval(x, y, w/2, h/2, size/12, 100, 0);//outer oval
        Core.drawOval(x-w/2, y, w/10, h/3, size/15, 100, 0, 0, 13);//left connection oval top
        Core.drawOval(x-w/2, y, w/10, h/3, size/15, 100, 0, 37, 50);//left connection oval bottom
        Core.drawOval(x+w/2, y, w/10, h/3, size/15, 100, 0, -13, 0);//right connection oval top
        Core.drawOval(x+w/2, y, w/10, h/3, size/15, 100, 0, -50, -37);//right connection oval bottom
        render(x, y, w, h);
        if(plural){
            Core.drawOval(x, y, w/2, h/2, -h/5, 360, 0, 44, 46);
            Core.drawOval(x, y, w/2, h/2, -h/5, 360, 0, 134, 136);
            Core.drawOval(x, y, w/2, h/2, -h/5, 360, 0, 224, 226);
            Core.drawOval(x, y, w/2, h/2, -h/5, 360, 0, -46, -44);
        }
        double Y = 0;
        double lastSize = getHeight(size);
        for(HorizontalNoun noun : otherNouns){
            Y+=lastSize/2+noun.getHeight(size)/2;
            lastSize = noun.getHeight(size);
            noun.draw(x, y+Y, size);
            noun.draw(x, y-Y, size);
        }
        for(HorizontalAdjective adjective : adjectives){
            Y+=lastSize/2+adjective.getHeight(size)/2;
            lastSize = adjective.getHeight(size);
            adjective.draw(x, y+Y, size);
            adjective.draw(x, y-Y, size);
        }
    }
    protected abstract void render(double x, double y, double w, double h);
}