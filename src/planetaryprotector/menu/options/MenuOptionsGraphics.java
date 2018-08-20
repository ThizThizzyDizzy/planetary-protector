package planetaryprotector.menu.options;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
public class MenuOptionsGraphics extends Menu{
    private final MenuComponentButton back;
    public static boolean fog, clouds, particulateMeteors, fire, particulateFire;
    public static int particles = 1;
    private final MenuComponentOptionButton fogToggle, cloudsToggle, particleMeteorsToggle, particlesToggle, fireToggle, particleFireToggle;
    public MenuOptionsGraphics(GUI gui, Menu parent){
        super(gui, parent);
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true));
        double yOffset = 120;
        cloudsToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Clouds", true, true, clouds?0:1, "On", "Off"));
        yOffset+=back.height*2;
        fogToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Fog", true, true, fog?0:1, "On", "Off"));
        yOffset+=back.height*2;
        fireToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Fire", true, true, fire?0:1, "On", "Off"));
        yOffset+=back.height*2;
        particleMeteorsToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Particulate Meteors", true, true, particulateMeteors?0:1, "On", "Off"));
        yOffset+=back.height*2;
        particleFireToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Particulate Fire", true, true, particulateFire?0:1, "On", "Off"));
        yOffset+=back.height*2;
        particlesToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Particles", true, true, particles, "Minimal", "Normal", "Excessive"));
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==back){
            gui.open(parent);
        }
    }
    @Override
    public void renderBackground(){
        GL11.glColor4d(1, 1, 1, 1);
        super.renderBackground();
        GL11.glColor4d(1, 1, 1, 1);
        fog = fogToggle.getIndex()==0;
        clouds = cloudsToggle.getIndex()==0;
        particulateMeteors = particleMeteorsToggle.getIndex()==0;
        fire = fireToggle.getIndex()==0;
        particulateFire = particleFireToggle.getIndex()==0;
        particles = particlesToggle.getIndex();
    }
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        GL11.glColor4d(1, 1, 1, 1);
        fogToggle.render();
        GL11.glColor4d(1, 1, 1, 1);
        cloudsToggle.render();
        GL11.glColor4d(1, 1, 1, 1);
        particleMeteorsToggle.render();
        GL11.glColor4d(1, 1, 1, 1);
        fireToggle.render();
        GL11.glColor4d(1, 1, 1, 1);
        particleFireToggle.render();
        GL11.glColor4d(1, 1, 1, 1);
        particlesToggle.render();
        GL11.glColor4d(1, 1, 1, 1);
    }
}