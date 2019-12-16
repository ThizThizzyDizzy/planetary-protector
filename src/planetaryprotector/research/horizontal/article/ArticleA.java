package planetaryprotector.research.horizontal.article;
import planetaryprotector.research.horizontal.HorizontalArticle;
import simplelibraryextended.opengl.AdvancedRenderer2D;
public class ArticleA extends HorizontalArticle{
    @Override
    protected void render(double x, double y, double s){
        AdvancedRenderer2D.drawOval(x-s/4, y+s/4, s/4, s/4, s/30, 40, 0, 0, 10);
        AdvancedRenderer2D.drawOval(x-s/4, y-s/4, s/4, s/4, s/30, 40, 0, 10, 20);
        AdvancedRenderer2D.drawOval(x+s/4, y-s/4, s/4, s/4, s/30, 40, 0, 20, 30);
        AdvancedRenderer2D.drawOval(x+s/4, y+s/4, s/4, s/4, s/30, 40, 0, 30, 40);
    }
}