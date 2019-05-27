package planetaryprotector.menu.component;
import planetaryprotector.item.ItemStack;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.ActionUpdate;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public abstract class MenuComponentActionButton extends MenuComponentButton implements ActionListener{
    private final ActionUpdate update;
    public final ItemStack[] tooltip;
    public MenuComponentActionButton(int x, int y, int width, int height, String label, ActionUpdate update, ItemStack... tooltip){
        super(x, y, width, height, label, update.getEnabled());
        this.update = update;
        this.tooltip = tooltip;
        textInset+=10;
    }
    @Override
    public void render(){
        super.render();
        removeRenderBound();
        GL11.glColor4d(1, 1, 1, 1);
        if(isMouseOver){
            double Y = y;
            double H = height/2;
            for(ItemStack stack : tooltip){
                drawRect(x+width, Y, x+width+H, Y+H, ImageStash.instance.getTexture("/textures/items/"+stack.item.texture+".png"));
                drawText(x+width+H, Y, Display.getWidth()+x, Y+H, stack.count+"");
                Y+=H;
            }
        }
    }
    @Override
    public abstract void actionPerformed(ActionEvent e);
    public void update(){
        enabled = update.getEnabled();
    }
}