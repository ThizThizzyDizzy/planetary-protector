package planetaryprotector.item;
import planetaryprotector.game.Game;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
public class DroppedItem extends GameObject{
    public final Item item;
    private int flashDelay = 20;
    private int flashTimer = 20;
    public int life = -1;//1200*5;
    private double opacity = 1;
    private double rot;
    public boolean dead = false;
    public DroppedItem(Game game, int x, int y, Item item){
        super(game, x,y,10,10);
        this.item=item;
        rot = game.rand.nextDouble()*360;
    }
    @Override
    public void draw(){
        GL11.glColor4d(1, 1, 1, opacity);
        if(item==Item.star){
            rot+=.75;
            GL11.glPushMatrix();
            GL11.glTranslated(x+width/2, y+height/2, 0);
            GL11.glRotated(rot, 0, 0, 1);
            drawRect(-width, -height, width, height, item.getWorldTexture());
            GL11.glPopMatrix();
        }else{
            drawRect(x, y, x+width, y+height, item.getWorldTexture());
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    public void tick() {
        if(life==-1){
            return;
        }
        life--;
        if(life<=0){
            dead = true;
        }
        if(life<600){
            flashTimer--;
            flashDelay = (life/600)*20;
            if(flashTimer<=0){
                flashTimer+=flashDelay;
                if(opacity==1){
                    opacity = 0.5;
                }else{
                    opacity = 1;
                }
            }
        }
    }
    public void damage(double x, double y){
        dead = true;
    }
}