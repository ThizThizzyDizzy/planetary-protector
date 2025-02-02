package planetaryprotector.structure;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
import java.util.Iterator;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.Particle;
import planetaryprotector.event.StructureChangeEventListener;
import planetaryprotector.game.BoundingBox;
import planetaryprotector.game.GameState;
public class ShieldGenerator extends Structure implements PowerConsumer, StructureDemolishable, StructureChangeEventListener{
    public double shieldSize = 0;
    public double maxShieldSize = 500;
    public float shieldStrength = 1;
    public double oldPower;
    public float blastRecharge = 0;
    public boolean canBlast = false;//replace with special upgrade
    public final Shield shield;
    private double power = 0;
    public Structure projectorTarget;
    int shieldDecalTimer = 0;
    public static final int shieldDecalInterval = 240;
    public static final float shieldDecalSpeed = 1;
    public ArrayList<Double> shieldDecals = new ArrayList<>();
    public ShieldGenerator(Game game, int x, int y){
        super(StructureType.SHIELD_GENERATOR, game, x, y, 100, 100);
        shield = new Shield(this, width/2, height/2);
    }
    public ShieldGenerator(Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        super(StructureType.SHIELD_GENERATOR, game, x, y, 100, 100, level, upgrades);
        canBlast = level>=10;
        shieldStrength = getStats(level);
        maxShieldSize += (level)*250;
        shield = new Shield(this, width/2, height/4);
    }
    public void blast(){
        if(!canBlast||blastRecharge!=0)return;
        blastRecharge = -50;
    }
    @Override
    public void tick(){
        super.tick();
        shieldDecalTimer--;
        if(shieldDecalTimer<0){
            shieldDecalTimer += shieldDecalInterval;
            shieldDecals.add(0d);
        }
        for(int i = 0; i<shieldDecals.size(); i++){
            shieldDecals.set(i, shieldDecals.get(i)+shieldDecalSpeed);
        }
        for(Iterator<Double> it = shieldDecals.iterator(); it.hasNext();){
            Double next = it.next();
            if(next>getShieldSize()/2)it.remove();
        }
        if(blastRecharge>0)blastRecharge--;
        if(blastRecharge<0){//<editor-fold defaultstate="collapsed" desc="Shield blast">
            blastRecharge++;
            BoundingBox city = game.getCityBoundingBox();
            double size = ((city.width*1.8)-(((-blastRecharge)%10)*(city.width/5)));
            shieldSize = size;
            game.pushParticles(x+width/2, y+height/2, size, size/50, Particle.PushCause.SHEILD_BLAST);
            if(size==0){
                for(Enemy enemy : game.enemies){
                    enemy.shieldBlast();
                }
                game.meteorShower = false;
                game.meteorTimer++;
            }
            if(blastRecharge==0){
                blastRecharge = 20*60*10;
                shieldSize = 0;
                power = 0;
            }
        }//</editor-fold>
        double surface = 2*Math.PI*Math.pow(shieldSize/2, 2);
        if(projectorTarget!=null){
            surface += projectorTarget.getStructureHeight()*4+10_000;
        }
        double powerDrawFactor = 50_000D;
        power -= surface/powerDrawFactor;
        double maxSurface = power/100*powerDrawFactor;
        double maxSize = Math.sqrt(maxSurface/2/Math.PI)*2;
        if(shieldSize>maxSize){
            shieldSize--;
        }
        shieldSize += (maxSize-shieldSize)/100;
        oldPower = power;
        if(power<=0){
            shieldSize = 0;
            power = 0;
        }
        shieldSize = Math.max(0, Math.min(maxShieldSize, shieldSize));
    }
    @Override
    public void renderBackground(){
        Renderer.fillRect(x, y, x+width, y+height, StructureType.EMPTY_PLOT.getTexture());
        super.renderBackground();
    }
    @Override
    public void renderForeground(){
        super.renderForeground();
        Game.theme.applyTextColor();
        Renderer.drawCenteredText(x, y+height-20, x+width, y+height, "Level "+level);
        Renderer.setColor(1, 1, 1, 1);
    }
    /**
     * @return the shieldSize
     */
    public double getShieldSize(){
        return shieldSize;
    }
    public double getShieldSize(double damage){
        double difference = -damage;
        double size = shieldSize;
        size += difference/shieldStrength;
        if(size<0){
            return 0;
        }
        return size;
    }
    /**
     * @param shieldSize the shieldSize to set
     */
    public void setShieldSize(double shieldSize){
        double difference = shieldSize-this.shieldSize;
        this.shieldSize += difference/shieldStrength;
        if(this.shieldSize<0){
            this.shieldSize = 0;
        }
    }
    @Override
    public GameState.Structure save(){
        var state = super.save();
        state.power = power;
        state.shieldGenerator = new GameState.Structure.ShieldGenerator();
        state.shieldGenerator.blastRecharge = blastRecharge;
        state.shieldGenerator.shieldSize = shieldSize;
        state.shieldGenerator.maxShieldSize = maxShieldSize;
        state.shieldGenerator.shieldStrength = shieldStrength;
        state.shieldGenerator.oldPower = oldPower;
        state.shieldGenerator.target = projectorTarget==null?-1:projectorTarget.getIndex();
        return state;
    }
    public static ShieldGenerator loadSpecific(GameState.Structure state, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        ShieldGenerator generator = new ShieldGenerator(game, x, y, level, upgrades);
        generator.power = state.power;
        generator.blastRecharge = state.shieldGenerator.blastRecharge;
        generator.shieldSize = state.shieldGenerator.shieldSize;
        generator.maxShieldSize = state.shieldGenerator.maxShieldSize;
        generator.shieldStrength = state.shieldGenerator.shieldStrength;
        generator.oldPower = state.shieldGenerator.oldPower;
        return generator;
    }
    private float getStats(int level){
        return (float)Math.max(level, (49/400d)*Math.pow(level, 2)+1);
    }
    @Override
    public void upgrade(){
        super.upgrade();
        canBlast = level>=10;
        shieldStrength = getStats(level);
        maxShieldSize += (level)*250;
    }
    @Override
    public double getMaxPower(){
        return Integer.MAX_VALUE;
    }
    @Override
    public double getDemand(){
        return 100;
    }
    @Override
    public double getPower(){
        return power;
    }
    @Override
    public void addPower(double power){
        this.power += power;
    }
    @Override
    public boolean isPowerActive(){
        return true;
    }
    @Override
    public void getDebugInfo(ArrayList<String> data){
        super.getDebugInfo(data);
        data.add("Shield size: "+shieldSize+"/"+maxShieldSize);
        data.add("Shield Strength: "+shieldStrength);
        data.add("oldPower: "+oldPower);
        data.add("Blast Recharge: "+blastRecharge);
        data.add("Can Blast: "+canBlast);
        data.add("Power: "+power);
        data.add("Projector target: "+(projectorTarget==null?"NONE":projectorTarget.toString()));
    }
    public void setProjectorTarget(Structure structure){
        if(projectorTarget!=null){
            projectorTarget.shield = null;
        }
        projectorTarget = structure;
        if(projectorTarget!=null){
            projectorTarget.shield = this;
        }
    }
    @Override
    public void onStructureChange(Structure from, Structure to){
        if(projectorTarget==from)setProjectorTarget(to);
    }
    @Override
    public void postLoad(Game game, GameState.Structure state){
        super.postLoad(game, state);
        int index = state.shieldGenerator.target;
        if(index!=-1){
            projectorTarget = game.structures.get(index);
        }
    }
    /**
     * Damage the shield
     *
     * @return true if the shield successfully absorbed the hit
     */
    public boolean shieldHit(){
        setShieldSize(getShieldSize()-100);
        if(getShieldSize()>0){
            return true;
        }
        setShieldSize(0);
        return false;
    }
    public double getProjectedShieldStrength(){
        return Math.min(1, shieldSize/100);
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        if(game.phase>=3&&canBlast){
            actions.add(new Action("Blast", this::blast, () -> {
                return blastRecharge==0;
            }));
        }
        if(hasUpgrade(Upgrade.SHIELD_PROJECTOR)){
            if(game.setTarget==null){
                actions.add(new Action(projectorTarget!=null?"Change Projector Target":"Set Projector Target", () -> {
                    if(game.setTarget==null){
                        game.setTarget = this;
                    }else{
                        game.setTarget = null;
                    }
                }, () -> {
                    return true;
                }));
                if(projectorTarget!=null){
                    actions.add(new Action("Clear Projector Target", () -> {
                        setProjectorTarget(null);
                        game.setTarget = null;
                    }, () -> {
                        return true;
                    }));
                }
            }
        }
    }
    @Override
    public double getDisplayPower(){
        return shieldSize;
    }
    @Override
    public double getDisplayMaxPower(){
        return maxShieldSize;
    }
}
