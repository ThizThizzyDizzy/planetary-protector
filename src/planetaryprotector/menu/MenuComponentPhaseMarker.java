package planetaryprotector.menu;
import planetaryprotector.game.Game;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentPhaseMarker extends MenuComponent{
    private final Game game;
    private final int phase;
    public MenuComponentPhaseMarker(MenuGame menu){
        super(0, 0, Display.getWidth(), Display.getHeight());
        game = menu.game;
        this.phase = menu.game.phase;
    }
    public double opacity = .2;
    private boolean opacitizing = true;
    @Override
    public void mouseEvent(double x, double y, int button, boolean isDown) {
        if(!isDown)return;
        if(opacitizing){
            if(opacity<.8)return;
            opacitizing = false;
            game.paused = false;
        }
    }
    @Override
    public void tick(){
        if(opacitizing){
            opacity+=0.045;
            if(opacity>1){
                opacity = 1;
            }
        }else{
            opacity-=0.02;
        }
    }
    @Override
    public void render(){
        if(game.doNotDisturb||!game.paused){
            opacitizing = false;
        }
        if(phase>3)return;
        GL11.glColor4d(1, 1, 1, opacity);
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/phase/"+phase+".png"));
        GL11.glColor4d(1, 1, 1, 1);
    }
}