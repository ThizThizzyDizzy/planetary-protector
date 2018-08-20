package planetaryprotector.item;
import org.lwjgl.opengl.Display;
import planetaryprotector.menu.MenuGame;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.component.ZComponent;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentDroppedItem extends MenuComponent{
    public final Item item;
    private final MenuGame myparent;
    private int flashDelay = 20;
    private int flashTimer = 20;
    public int life = -1;//1200*5;
    private double opacity = 1;
    public MenuComponentDroppedItem(double x, double y, Item item, MenuGame myparent){
        super(x,y,10,10);
        this.item=item;
        this.myparent = myparent;
    }
    @Override
    public void renderBackground(){
        GL11.glColor4d(1, 1, 1, opacity);
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/items/"+item.texture+".png"));
        GL11.glColor4d(1, 1, 1, 1);
    }
    public void render(){}
    @Override
    public void tick() {
        super.tick();
        if(x<0||x>Display.getWidth()||y<0||y>Display.getHeight()){
            life = 1;
        }
        if(life==-1){
            return;
        }
        life--;
        if(life<=0){
            myparent.componentsToRemove.add(this);
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
        myparent.componentsToRemove.add(this);
    }
}