package planetaryprotector.research.horizontal.article;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.research.horizontal.HorizontalArticle;
public class ArticleA extends HorizontalArticle{
    @Override
    protected void render(float x, float y, float s){
        Renderer.fillHollowRegularPolygonSegment(x-s/4, y+s/4, 40, s/4-s/30, s/4-s/30, s/4, s/4, 0, 10);
        Renderer.fillHollowRegularPolygonSegment(x-s/4, y-s/4, 40, s/4-s/30, s/4-s/30, s/4, s/4, 10, 20);
        Renderer.fillHollowRegularPolygonSegment(x+s/4, y-s/4, 40, s/4-s/30, s/4-s/30, s/4, s/4, 20, 30);
        Renderer.fillHollowRegularPolygonSegment(x+s/4, y+s/4, 40, s/4-s/30, s/4-s/30, s/4, s/4, 30, 40);
    }
}
