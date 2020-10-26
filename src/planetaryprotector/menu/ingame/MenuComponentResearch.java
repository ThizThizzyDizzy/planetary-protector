package planetaryprotector.menu.ingame;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.research.Research;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentResearch extends MenuComponent{
    private final MenuResearch menu;
    public final Research research;
    public MenuComponentResearch(MenuResearch menu, Research research){
        super(0,0,MenuResearch.researchSize,MenuResearch.researchSize);
        this.menu = menu;
        this.research = research;
    }
    @Override
    public void render(){
        double borderThickness = width/12;
        double border = borderThickness/.4;
        int quality = 36;
        GL11.glColor4d(.95, .95, .95, 1);
        drawRect(x+border, y, x+width-border, y+height, 0);
        drawRect(x, y+border, x+width, y+height-border, 0);
        Core.drawOval(x+border, y+border, border, border, border, quality, 0, 27, 36);
        Core.drawOval(x+width-border, y+border, border, border, border, quality, 0, 0, 9);
        Core.drawOval(x+width-border, y+height-border, border, border, border, quality, 0, 9, 18);
        Core.drawOval(x+border, y+height-border, border, border, border, quality, 0, 18, 27);
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(x+borderThickness, y+borderThickness, x+width-borderThickness, y+height-borderThickness, research.getTexture());
        GL11.glColor4d(.75, .75, .75, 1);
        Core.drawOval(x+border, y+border, border, border, borderThickness, quality, 0, 27, 36);
        Core.drawOval(x+width-border, y+border, border, border, borderThickness, quality, 0, 0, 9);
        Core.drawOval(x+width-border, y+height-border, border, border, borderThickness, quality, 0, 9, 18);
        Core.drawOval(x+border, y+height-border, border, border, borderThickness, quality, 0, 18, 27);
        drawRect(x+border, y, x+width-border, y+borderThickness, 0);
        drawRect(x+width-borderThickness, y+border, x+width, y+height-border, 0);
        drawRect(x+border, y+height-borderThickness, x+width-border, y+height, 0);
        drawRect(x, y+border, x+borderThickness, y+height-border, 0);
    }
    @Override
    public void onMouseButton(double x, double y, int button, boolean pressed, int mods){
        super.onMouseButton(x, y, button, pressed, mods);
        if(button==0&&pressed)menu.selected.research = research;
    }
}