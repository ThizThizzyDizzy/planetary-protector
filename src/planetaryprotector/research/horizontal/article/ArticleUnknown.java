package planetaryprotector.research.horizontal.article;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalArticle;
public class ArticleUnknown extends HorizontalArticle{
    private final String article;
    public ArticleUnknown(String article){
        this.article = article;
    }
    @Override
    protected void render(float x, float y, float size){
        size -= 20;
        float length = Renderer.getStringWidth(article, size);
        Renderer.drawCenteredText(x-length/2, y-size/2, x+length/2, y+size/2, article);
    }
}
