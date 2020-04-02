package planetaryprotector.menu.component;
import planetaryprotector.item.ItemStack;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentActionButton extends MenuComponentButton{
    private final MenuGame menu;
    private final Game game;
    public final Action action;
    public MenuComponentActionButton(MenuGame menu, Game game, int x, int y, int width, int height, Action action){
        super(x, y, width, height, action.label, action.update.getEnabled());
        textInset+=15;
        this.menu = menu;
        this.game = game;
        this.action = action;
    }
    @Override
    public void render(){
        super.render();
        removeRenderBound();
        GL11.glColor4d(1, 1, 1, 1);
        if(isMouseOver){
            double Y = y;
            double H = height/2;
            for(ItemStack stack : action.tooltip){
                drawRect(x+width, Y, x+width+H, Y+H, stack.item.getTexture());
                drawText(x+width+H, Y, Display.getWidth()+x, Y+H, stack.count+"");
                Y+=H;
            }
        }
        if(!action.isImportant()){
            int id = menu.actionButtons.indexOf(this);
            if(id>=0&&id<=11){
                GL11.glColor4d(1, 1, 1, .75);
                String num = (id+1)+"";
                if(id==9)num = "0";
                if(id==10)num = "-";
                if(id==11)num = "=";
                drawText(x+textInset, y+height/2, x+width-textInset, y+height-textInset, num);
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
    }
    public void update(){
        enabled = action.update.getEnabled();
    }
    public void perform(){
        if(action.listener==null)return;
        action.listener.actionPerformed(null);
        game.actionUpdateRequired = 2;
    }
}