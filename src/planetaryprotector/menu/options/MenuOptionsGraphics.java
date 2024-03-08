package planetaryprotector.menu.options;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import com.thizthizzydizzy.dizzyengine.ui.component.Button;
import com.thizthizzydizzy.dizzyengine.ui.component.OptionButton;
import com.thizthizzydizzy.dizzyengine.ui.component.Slider;
import com.thizthizzydizzy.dizzyengine.ui.layout.ConstrainedLayout;
import com.thizthizzydizzy.dizzyengine.ui.layout.constraint.PositionAnchorConstraint;
import planetaryprotector.Options;
import planetaryprotector.game.Game;
public class MenuOptionsGraphics extends Menu{
    public MenuOptionsGraphics(){
        var layout = setLayout(new ConstrainedLayout());
        var back = add(new Button("Back"));
        back.setSize(400, 40);
        back.addAction(() -> new MenuOptions().open());
        layout.constrain(back, new PositionAnchorConstraint(.5f, 0, .5f, 1, 0, -80));
        var clouds = add(new OptionButton("Clouds", Options.options.clouds?0:1, "On", "Off"));
        clouds.setSize(400, 40);
        clouds.addAction(() -> Options.options.clouds = clouds.getIndex()==0);
        layout.constrain(clouds, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 120));
        var fog = add(new OptionButton("Fog", Options.options.fog?0:1, "On", "Off"));
        fog.setSize(400, 40);
        fog.addAction(() -> Options.options.fog = fog.getIndex()==0);
        layout.constrain(fog, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 200));
        var particleMeteors = add(new OptionButton("Particulate Meteors", Options.options.particulateMeteors?0:1, "On", "Off"));
        particleMeteors.setSize(400, 40);
        particleMeteors.addAction(() -> Options.options.particulateMeteors = particleMeteors.getIndex()==0);
        layout.constrain(particleMeteors, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 280));
        var particles = add(new OptionButton("Particles", Options.options.particles, "Minimal", "Normal", "Excessive"));
        particles.setSize(400, 40);
        particles.addAction(() -> Options.options.particles = particles.getIndex());
        layout.constrain(particles, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 360));
        var cloudIntensity = add(new Slider(.1, 1, Options.options.cloudIntensity, "Cloud Intensity"));
        cloudIntensity.setSize(400, 40);
        cloudIntensity.addAction(() -> Options.options.cloudIntensity = cloudIntensity.getValueF());
        layout.constrain(cloudIntensity, new PositionAnchorConstraint(.5f, 0f, .5f, 0, 0, 440));
        var fogIntensity = add(new Slider(.1, 1, Options.options.fogIntensity, "Fog Intensity"));
        fogIntensity.setSize(400, 40);
        fogIntensity.addAction(() -> Options.options.fogIntensity = fogIntensity.getValueF());
        layout.constrain(fogIntensity, new PositionAnchorConstraint(.5f, 0f, .5f, 0, 0, 520));
        var theme = add(new OptionButton("Theme", Options.options.theme, "Auto", "Normal", "Snowy"));
        theme.setSize(400, 40);
        theme.addAction(() -> {
            Options.options.theme = theme.getIndex();
            Game.refreshTheme();
        });
        layout.constrain(theme, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 600));
        var health = add(new OptionButton("Health Bar Mode", Options.options.health, "None", "Show when damaged", "Show always"));
        health.setSize(400, 40);
        health.addAction(() -> Options.options.health = health.getIndex());
        layout.constrain(health, new PositionAnchorConstraint(.5f, 0, .5f, 0, 0, 680));
    }
    @Override
    public void draw(double deltaTime){
        super.draw(deltaTime);
        Renderer.fillRect(0, 0, getWidth(), getHeight(), Game.theme.getBackgroundTexture(1));
    }
}