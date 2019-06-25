package planetaryprotector.menu.options;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
import simplelibrary.opengl.gui.components.MenuComponentSlider;
public class MenuOptionsGraphics extends Menu{
    private final MenuComponentButton back;
    public static boolean fog, clouds, particulateMeteors;
    public static int particles = 1;
    public static float cloudIntensity = .7f, fogIntensity = .9f;
    private final MenuComponentOptionButton fogToggle, cloudsToggle, particleMeteorsToggle, particlesToggle;
    private final MenuComponentSlider cloudIntensitySlider, fogIntensitySlider;
    public MenuOptionsGraphics(GUI gui, Menu parent){
        super(gui, parent);
        back = add(new MenuComponentButton(Display.getWidth()/2-200, Display.getHeight()-80, 400, 40, "Back", true));
        double yOffset = 120;
        cloudsToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Clouds", true, true, clouds?0:1, "On", "Off"));
        yOffset+=back.height*2;
        fogToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Fog", true, true, fog?0:1, "On", "Off"));
        yOffset+=back.height*2;
        particleMeteorsToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Particulate Meteors", true, true, particulateMeteors?0:1, "On", "Off"));
        yOffset+=back.height*2;
        particlesToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Particles", true, true, particles, "Minimal", "Normal", "Excessive"));
        yOffset+=back.height*2;
        cloudIntensitySlider = add(new MenuComponentSlider(back.x, yOffset, back.width, back.height, .1, 1, cloudIntensity, 10, true));
        yOffset+=back.height*2;
        fogIntensitySlider = add(new MenuComponentSlider(back.x, yOffset, back.width, back.height, .1, 1, fogIntensity, 10, true));
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
        particles = particlesToggle.getIndex();
        cloudIntensity = (float)cloudIntensitySlider.getValue();
        fogIntensity = (float)fogIntensitySlider.getValue();
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
        particlesToggle.render();
        GL11.glColor4d(1, 1, 1, 1);
        cloudIntensitySlider.render();
        GL11.glColor4d(1, 1, 1, 1);
        fogIntensitySlider.render();
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(cloudIntensitySlider.x, cloudIntensitySlider.y-20, cloudIntensitySlider.x+cloudIntensitySlider.width, cloudIntensitySlider.y, "Cloud Intensity");
        drawCenteredText(fogIntensitySlider.x, fogIntensitySlider.y-20, fogIntensitySlider.x+fogIntensitySlider.width, fogIntensitySlider.y, "Fog Intensity");
    }
}