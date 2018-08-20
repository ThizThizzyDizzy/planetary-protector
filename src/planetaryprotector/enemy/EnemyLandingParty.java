package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.MenuComponentShieldGenerator;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.BuildingType;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
public class EnemyLandingParty extends MenuComponentEnemy{
    public int initialDelay = 20*10;
    public double laserPower = 1.25;
    public double laserTime = 20*30;
    public double laserSize = 20;
    public double laserSizing = 1/3D;
    private final MenuGame game;
    public boolean dead = false;
    public EnemyLandingParty(MenuGame game){
        super(0, 0, 50, 50, 250);
        double[] location = getFurthestCorner();
        if(location==null){
            location = new double[]{game.base.x+game.base.width/2,game.base.y+game.base.height/2};
        }
        x=location[0];
        y=location[1];
        laserPower*=strength;
        this.game = game;
        for(int i = 0; i<strength-7; i++){
            crew.add(new EnemyAlien(x, y));
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        if(laserFiring!=null){
            double xDiff = laserFiring[0]-x;
            double yDiff = laserFiring[1]-y;
            double dist = Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(1, 0, 0, 1);
                MenuGame.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), laserSize/2D,10,0);
            }
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(1, .5, 0, 1);
                MenuGame.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), (laserSize*(2/3D))/2D,10,0);
            }
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(1, 1, 0, 1);
                MenuGame.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), (laserSize*(1/3D))/2D,10,0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
        width = height = 50*((initialDelay/20D)+1);
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, ImageStash.instance.getTexture("/textures/enemies/ship.png"));
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        if(health<=0){
            dead = true;
        }
        if(dead){
            increase = true;
            game.componentsToRemove.add(this);
            game.addParticleEffect(new MenuComponentParticle(x, y, ParticleEffectType.EXPLOSION, 1, true));
            if(increase&&strength<10){
                strength+=.5;
            }
            return;
        }
        DO:do{
            for(EnemyAlien alien : crew){
                if(!alien.dead){
                    break DO;
                }
            }
            if(crew.isEmpty()) break;
            running = true;
        }while(false);
        if(laserTime<=0||running){
            if(laserTime<=0){
                laserFiring = null;
            }
            initialDelay++;
            if(initialDelay>=20*10){
                if(!landed){
                    increase = true;
                }
                for(EnemyAlien alien : crew){
                    if(!alien.dead){
                        increase = true;
                    }
                }
                if(increase&&strength<10){
                    strength+=.5;
                }
                game.componentsToRemove.add(this);
            }
            return;
        }
        fireLaser();
        if(initialDelay>=20*5||landing){
            initialDelay--;
        }
        if(initialDelay<0){
            initialDelay = 0;
            land();
        }
    }
    boolean running = false;
    double[] laserFiring = null;
    boolean landing = false;
    public boolean landed = false;
    boolean increase = false;
    private void fireLaser(){
        laserFiring = null;
        double dist = Double.POSITIVE_INFINITY;
        MenuComponentShieldGenerator gen = null;
        for(MenuComponentBuilding building : game.buildings){
            if(building instanceof MenuComponentShieldGenerator){
                MenuComponentShieldGenerator g = (MenuComponentShieldGenerator) building;
                dist = Math.min(dist,game.distance(this, g));
                gen = g;
            }
        }
        if(gen==null) return;
        if(gen.getShieldSize()/2>dist-25&&initialDelay<20*5){
            dead = true;
            increase = true;
        }
        if(gen.getShieldSize()/2<dist-50){
            landing = true;
            return;
        }
        laserSize+=laserSizing;
        if(laserSize>=25){
            laserSizing*=-1;
        }
        if(laserSize<=15){
            laserSizing*=-1;
        }
        laserTime--;
        laserFiring = new double[]{gen.x+gen.width/2,gen.y+gen.height/2};
        gen.setShieldSize(gen.getShieldSize()-laserPower*75);
    }
    public static double[] getFurthestCorner(){
        ArrayList<MenuComponentShieldGenerator> shieldGen = new ArrayList<>();
        for(MenuComponentBuilding building : Core.game.buildings){
            if(building.type==BuildingType.SHIELD_GENERATOR){
                shieldGen.add((MenuComponentShieldGenerator) building);
            }
        }
        ArrayList<Double[]> possibleStrikes = new ArrayList<>();
        for(MenuComponentShieldGenerator gen : shieldGen){
            double dist = Core.game.distanceTo(gen,25d,25d);
            possibleStrikes.add(new Double[]{25d,25d,dist});
            dist = Core.game.distanceTo(gen,Display.getWidth()-25d,25d);
            possibleStrikes.add(new Double[]{Display.getWidth()-25d,25d,dist});
            dist = Core.game.distanceTo(gen,25d,Display.getHeight()-25d);
            possibleStrikes.add(new Double[]{25d,Display.getHeight()-25d,dist});
            dist = Core.game.distanceTo(gen,Display.getWidth()-25d,Display.getHeight()-25d);
            possibleStrikes.add(new Double[]{Display.getWidth()-25d,Display.getHeight()-25d,dist});
        }
        double max = Double.NEGATIVE_INFINITY;
        for(Double[] strike : possibleStrikes){
            max = Math.max(strike[2], max);
        }
        for(Double[] strike : possibleStrikes){
            if(strike[2]==max){
                return new double[]{strike[0],strike[1]};
            }
        }
        return null;
    }
    ArrayList<EnemyAlien> crew = new ArrayList<>();
    private void land(){
        landed = true;
        for(EnemyAlien alien : crew){
            if(game.aliens.contains(alien)) break;  
            if(alien.dead) continue;
            game.aliens.add(game.add(alien));
            break;
        }
    }
}