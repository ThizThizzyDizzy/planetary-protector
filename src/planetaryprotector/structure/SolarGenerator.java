    package planetaryprotector.structure;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import planetaryprotector.game.Game;
import simplelibrary.config2.Config;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class SolarGenerator extends Structure implements PowerProducer, StructureDemolishable{
    private int frame = 0;
    static final int frames = 4;
    private int speed = 0;//0-100
    private double delay = 0;
    private int spinTimer = 0;
    public SolarGenerator(Game game, int x, int y) {
        super(StructureType.SOLAR_GENERATOR, game, x, y, 100, 100);
    }
    public SolarGenerator(Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        super(StructureType.SOLAR_GENERATOR, game, x, y, 100, 100, level, upgrades);
    }
    @Override
    public void tick(){
        super.tick();
        speed = (int) ((getProduction()/getMaxProduction())*100);
        speed = Math.max(0, Math.min(100, speed));
        if(speed>0){
            delay = 100d/frames/speed;
            spinTimer++;
            while(spinTimer>=delay){
                spinTimer-=delay;
                frame++;
                if(frame>=frames)frame = 0;
            }
        }
    }
    
    @Override
    public void renderBackground(){
        drawRect(x, y, x+width, y+height, StructureType.EMPTY_PLOT.getTexture());
        super.renderBackground();
    }
    @Override
    public void render(){
        drawRect(x, y-getStructureHeight(), x+width, y+height, type.getTexture(), 0, frame/(double)frames, 1, (frame+1)/(double)frames);
        for(Upgrade upgrade : type.upgrades){
            int count = getUpgrades(upgrade);
            if(count==0)continue;
            drawRect(x, y-getStructureHeight(), x+width, y+height, upgrade.getTexture(type, count), 0, frame/(double)frames, 1, (frame+1)/(double)frames);
        }
        renderDamages();
    }
    @Override
    public void renderForeground(){
        super.renderForeground();
        Game.theme.applyTextColor();
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+level);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public static SolarGenerator loadSpecific(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        return new SolarGenerator(game, x, y, level, upgrades);
    }
    @Override
    public double getProduction(){
        double sunlight = Math.min(1,game.getSunlight()*Math.pow(1.38,getUpgrades(Upgrade.PHOTOVOLTAIC_SENSITIVITY)));
        if(hasUpgrade(Upgrade.STARLIGHT_GENERATION))sunlight = (1+sunlight)/2;
        return getMaxProduction()*sunlight;
    }
    public double getMaxProduction(){
        return Math.max(level, (49/400d)*Math.pow(level, 2)+1)/2;
    }
    @Override
    public void producePower(double power){}
    @Override
    public boolean isRenewable(){
        return true;
    }
    @Override
    public boolean isPowerActive(){
        return true;
    }
    @Override
    public double getDisplayPower(){
        return 0;
    }
    @Override
    public double getDisplayMaxPower(){
        return 0;
    }
}