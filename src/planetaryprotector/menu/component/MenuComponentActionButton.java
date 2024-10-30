package planetaryprotector.menu.component;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import planetaryprotector.item.ItemStack;
import org.lwjgl.opengl.GL11;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
public class MenuComponentActionButton extends Button{
    private final MenuGame menu;
    private final Game game;
    public final Action action;
    public MenuComponentActionButton(MenuGame menu, Game game, int x, int y, int width, int height, Action action){
        super(action.label, action.update.getEnabled());
        label.labelInset += 15;
        this.menu = menu;
        this.game = game;
        this.action = action;
        addAction(() -> {
            perform();
            game.actionUpdateRequired = 2;
        });//TODO just like don't
    }
    @Override
    public void render(double deltaTime){
        super.render(deltaTime);
        Renderer.setColor(Color.WHITE);
        if(isCursorFocused()){
            float Y = y;
            float H = getHeight()/2;
            for(ItemStack stack : action.tooltip){
                Renderer.fillRect(x+getWidth(), Y, x+getWidth()+H, Y+H, stack.item.getTexture());
                Renderer.drawText(x+getWidth()+H, Y, DizzyEngine.screenSize.x+x, Y+H, stack.count+"");
                Y += H;
            }
        }
        if(!action.isImportant()){
            int id = menu.actionButtons.indexOf(this);
            if(id>=0&&id<=11){
                Renderer.setColor(1, 1, 1, .75);
                String num = (id+1)+"";
                if(id==9)num = "0";
                if(id==10)num = "-";
                if(id==11)num = "=";
                Renderer.drawText(x+label.labelInset, y+getHeight()/2, x+getWidth()-label.labelInset, y+getHeight()-label.labelInset, num);
                Renderer.setColor(1, 1, 1, 1);
            }
        }
    }
    public void update(){
        enabled = action.update.getEnabled();
    }
    public void perform(){
        if(action.runFunc==null)return;
        action.runFunc.run();
        game.actionUpdateRequired = 2;
    }
}
