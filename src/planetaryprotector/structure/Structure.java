package planetaryprotector.structure;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.game.Game;
import planetaryprotector.structure.building.Building;
import planetaryprotector.structure.building.ShieldGenerator;
import simplelibrary.config2.Config;
public abstract class Structure extends GameObject{
    public double mouseover = 0;
    public ShieldGenerator shield = null;//the shield generator that is currently projecting a shield on this building
    public Structure(Game game, double x, double y, double width, double height){
        super(game, x, y, width, height);
    }
    public void tick(){}
    public void drawOverlay(){
        drawRect(x, y-getStructureHeight(), x+width, y+height, 0);
    }
    public void draw(){
        if(!isBackgroundStructure()){
            drawRect(x, y-getStructureHeight(), x+width, y+height, getTexture());
        }
    }
    @Override
    public void render(){
        draw();
        GL11.glColor4d(0, 1, 1, mouseover);
        drawOverlay();
        GL11.glColor4d(1, 1, 1, 1);
        if(shield!=null){
            GL11.glColor4d(.75, .875, 1, shield.getProjectedShieldStrength()/10d);
            drawOverlay();
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
    public void renderBackground(){
        if(isBackgroundStructure()){
            drawRect(x, y, x+width, y+height, getTexture());
        }
    }
    public static Structure load(Config cfg, Game game){
        if(cfg.hasProperty("level"))return Building.load(cfg, game);
        throw new IllegalArgumentException("Invalid structure!");
    }
    public abstract String getName();
    public abstract Config saveStructure(Config cfg);
    public final boolean onHit(double x, double y){
        if(shield!=null){
            if(shield.shieldHit())return true;
        }
        return damage(x,y);
    }
    public abstract boolean damage(double x, double y);
    public abstract boolean isBackgroundStructure();
    public int getStructureHeight(){
        return 0;
    }
    protected abstract int getTexture();
    /**
     * Called after all structures are loaded- this is in case any structure needs to load data regarding another structure (For example, shield projector target)
     * @param game the game that is being loaded
     * @param config the config to load from
     */
    public abstract void postLoad(Game game, Config config);
    public abstract void getDebugInfo(ArrayList<String> data);
    public int getIndex(){
        return game.structures.indexOf(this);
    }
    @Override
    public double getRandX(Random rand){
        return rand.nextDouble()*width;
    }
    @Override
    public double getRandY(Random rand){
        return rand.nextDouble()*(height+getStructureHeight())-getStructureHeight();
    }
    public abstract boolean isSelectable();
    public abstract boolean canBeShielded();
}