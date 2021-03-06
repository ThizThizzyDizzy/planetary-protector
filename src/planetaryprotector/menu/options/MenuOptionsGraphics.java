package planetaryprotector.menu.options;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.game.Game;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
import simplelibrary.opengl.gui.components.MenuComponentSlider;
public class MenuOptionsGraphics extends Menu{
    private final MenuComponentButton back;
    public static boolean fog, clouds, particulateMeteors;
    public static int particles = 1, theme = 0, health = 0;
    public static float cloudIntensity = .7f, fogIntensity = .9f;
    private final MenuComponentOptionButton fogToggle, cloudsToggle, particleMeteorsToggle, particlesToggle, themeToggle, healthToggle;
    private final MenuComponentSlider cloudIntensitySlider, fogIntensitySlider;
    public MenuOptionsGraphics(GUI gui, Menu parent){
        super(gui, parent);
        back = add(new MenuComponentButton(Core.helper.displayWidth()/2-200, Core.helper.displayHeight()-80, 400, 40, "Back", true));
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
        yOffset+=back.height*2;
        themeToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Theme", true, true, theme, "Auto", "Normal", "Snowy"));
        yOffset+=back.height*2;
        healthToggle = add(new MenuComponentOptionButton(back.x, yOffset, back.width, back.height, "Health Bar Mode", true, true, health, "None", "Show when damaged", "Show always"));
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
        drawRect(0,0,Core.helper.displayWidth(), Core.helper.displayHeight(), Game.theme.getBackgroundTexture(1));
        fog = fogToggle.getIndex()==0;
        clouds = cloudsToggle.getIndex()==0;
        particulateMeteors = particleMeteorsToggle.getIndex()==0;
        particles = particlesToggle.getIndex();
        if(theme!=themeToggle.getIndex()){
            theme = themeToggle.getIndex();
            Game.refreshTheme();
        }
        health = healthToggle.getIndex();
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
        themeToggle.render();
        GL11.glColor4d(1, 1, 1, 1);
        healthToggle.render();
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(cloudIntensitySlider.x, cloudIntensitySlider.y-20, cloudIntensitySlider.x+cloudIntensitySlider.width, cloudIntensitySlider.y, "Cloud Intensity");
        drawCenteredText(fogIntensitySlider.x, fogIntensitySlider.y-20, fogIntensitySlider.x+fogIntensitySlider.width, fogIntensitySlider.y, "Fog Intensity");
    }
}