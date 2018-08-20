package planetaryprotector.enemy;
import planetaryprotector.Core;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.component.ZComponent;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentMeteorShower extends MenuComponent{
    private double opacity;
    public int opacitizing = 0;
    public boolean hide = false;
    public MenuComponentMeteorShower(){
        super(0,0,Display.getWidth(),Display.getHeight());
        opacity = 0;
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(1, 1, 1, opacity);
        drawRect(0,0,Display.getWidth(),Display.getHeight(), ImageStash.instance.getTexture("/textures/meteors.png"));
        GL11.glColor4d(1, 1, 1, 1);
    }

    @Override
    public void tick() {
        if(hide){
            opacitizing = -Math.abs(opacitizing);
        }
        if(!Core.game.paused){
            opacity += opacitizing*0.1;
            opacity = Math.max(0,Math.min(1,opacity));
        }
    }
}