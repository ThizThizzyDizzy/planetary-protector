package planetaryprotector.structure;
import org.lwjgl.opengl.GL11;
import simplelibrary.image.Color;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.Renderer2D;
public class Shield extends Renderer2D{
    private final ShieldGenerator generator;
    public int x,y;
    public Shield(ShieldGenerator generator, double x, double y){
        this.generator = generator;
    }
    public void render(int millisSinceLastTick){
        drawShield(generator.x+x, generator.y+y, generator.getShieldSize()/2, 1f);
        for(double d : generator.shieldDecals){
            d+=Math.min(50, millisSinceLastTick)/50d*ShieldGenerator.shieldDecalSpeed;
            drawShield2(generator.x+x, generator.y+y, d, (1-(float)(d/(generator.getShieldSize()/2)))/2f);
        }
    }
    public static void drawShield(double x, double y, double radius, float alpha){
        drawCircle(x, y, radius*.95, radius, (int)(radius*2), new Color(0f, 1f, 1f, 0f), new Color(0f, 1f, 1f, alpha*.5f));
        drawCircle(x, y, 0, radius, (int)(radius*2), new Color(0f, 1f, 1f, alpha*.0625f), new Color(0f, 1f, 1f, alpha*.125f));
    }
    public static void drawShield2(double x, double y, double radius, float alpha){
        drawCircle(x, y, radius, radius*1.25, (int)(radius*2), new Color(0f, 1f, 1f, alpha*.5f), new Color(0f, 1f, 1f, 0f));
        drawCircle(x, y, radius*.75, radius, (int)(radius*2), new Color(0f, 1f, 1f, 0f), new Color(0f, 1f, 1f, alpha*.5f));
        drawCircle(x, y, 0, radius, (int)(radius*2), new Color(0f, 1f, 1f, alpha*.0625f), new Color(0f, 1f, 1f, alpha*.125f));
    }
    public static void drawCircle(double x, double y, double innerRadius, double outerRadius, int quality, Color innerColor, Color outerColor){
        if(quality<3){
            return;
        }
        ImageStash.instance.bindTexture(0);
        GL11.glBegin(GL11.GL_QUADS);
        double angle = 0;
        for(int i = 0; i<quality; i++){
            GL11.glColor4f(outerColor.getRed()/255f, outerColor.getGreen()/255f, outerColor.getBlue()/255f, outerColor.getAlpha()/255f);
            double X = x+Math.cos(Math.toRadians(angle-90))*outerRadius;
            double Y = y+Math.sin(Math.toRadians(angle-90))*outerRadius;
            GL11.glVertex2d(X, Y);
            GL11.glColor4f(innerColor.getRed()/255f, innerColor.getGreen()/255f, innerColor.getBlue()/255f, innerColor.getAlpha()/255f);
            X = x+Math.cos(Math.toRadians(angle-90))*innerRadius;
            Y = y+Math.sin(Math.toRadians(angle-90))*innerRadius;
            GL11.glVertex2d(X, Y);
            angle+=(360D/quality);
            X = x+Math.cos(Math.toRadians(angle-90))*innerRadius;
            Y = y+Math.sin(Math.toRadians(angle-90))*innerRadius;
            GL11.glVertex2d(X, Y);
            GL11.glColor4f(outerColor.getRed()/255f, outerColor.getGreen()/255f, outerColor.getBlue()/255f, outerColor.getAlpha()/255f);
            X = x+Math.cos(Math.toRadians(angle-90))*outerRadius;
            Y = y+Math.sin(Math.toRadians(angle-90))*outerRadius;
            GL11.glVertex2d(X, Y);
        }
        GL11.glEnd();
    }
}