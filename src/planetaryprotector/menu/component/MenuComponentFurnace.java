package planetaryprotector.menu.component;
import planetaryprotector.item.Item;
import planetaryprotector.menu.MenuGame;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentFurnace extends MenuComponentButton{
    public int coal;
    public int ironOre;
    private int timer = 100;
    public final MenuGame game;
    public int level = 0;
    public static final int maxLevel = 2;
    public boolean auto = false;
    public int total = 0;
    public MenuComponentFurnace(float x, float y, float width, float height, MenuGame myparent){
        super(x, y, width, height, "", true);
        this.game = myparent;
    }
    @Override
    public void render(){
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/pot "+level+".png"));
        if(isMouseOver&&level<maxLevel){
            GL11.glColor4d(0, 1, 0, .25);
            double percent = total/Math.pow(20, level+1);
            drawRect(x,y,x+(width*percent), y+height, 0);
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
    public void upgrade(){
        if(level>=maxLevel)return;
        if(total>=Math.pow(20, level+1)){
            level++;
            if(level>=2){
                auto = true;
            }
        }
    }
    @Override
    public void tick(){
        if(auto){
            if(MenuGame.rand.nextBoolean()){
                game.addIron(1);
            }else{
                game.addFuel(1);
            }
        }
        y = Display.getHeight()-100;
        if(coal>=Math.pow(10, level)&&ironOre>=Math.pow(10, level)){
           if(timer<=0){
               addIron();
               timer = 100;
           }else{
               timer--;
           }
        }
    }
    private void addIron(){
        ironOre-=Math.pow(10, level);
        coal-=Math.pow(10, level);
        for(int i = 0; i<Math.pow(10, level); i++)
            game.addResources(Item.ironIngot);
        total+=Math.pow(10, level);
        game.componentsToAdd.add(new MenuComponentRising(Display.getWidth()-60, Display.getHeight()-25, Item.ironIngot));
    }
}