package planetaryprotector.research.horizontal.article;
import planetaryprotector.Core;
import planetaryprotector.research.horizontal.HorizontalArticle;
public class ArticleA extends HorizontalArticle{
    @Override
    protected void render(double x, double y, double s){
        Core.drawOval(x-s/4, y+s/4, s/4, s/4, s/30, 40, 0, 0, 10);
        Core.drawOval(x-s/4, y-s/4, s/4, s/4, s/30, 40, 0, 10, 20);
        Core.drawOval(x+s/4, y-s/4, s/4, s/4, s/30, 40, 0, 20, 30);
        Core.drawOval(x+s/4, y+s/4, s/4, s/4, s/30, 40, 0, 30, 40);
    }
}