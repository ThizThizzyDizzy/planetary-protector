package planetaryprotector.friendly;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector.enemy.EnemyMeteorStrike;
import planetaryprotector.enemy.MenuComponentEnemy;
import planetaryprotector.building.MenuComponentSilo;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentDrone extends MenuComponent{
    int power = 0;
    int maxPower = 20*60*5;
    double[] target = new double[]{0,0};
    MenuComponentSilo silo;
    double speed = 2.5;
    public double laserPower = 1/4D;
    public double laserSize = 20;
    public double laserSizing = 1/3D;
    boolean charge = false;
    boolean deaded = false;
    public MenuComponentDrone(MenuComponentSilo silo, int power){
        super(silo.x+silo.width/2, silo.y+silo.height/2, 50, 50);
        this.silo = silo;
        this.power = power;
    }
    @Override
    public void tick(){
        if(deaded) return;
        if(power<=0){
            Core.game.componentsToRemove.add(this);
            Core.game.addParticleEffect(new MenuComponentParticle(x, y, ParticleEffectType.EXPLOSION, 1, true));
            if(silo!=null)silo.drones--;
            if(silo!=null)silo.droneList.remove(this);
            deaded = true;
            return;
        }
        power--;
        if(silo!=null&&(silo.damages.size()>=10||!Core.game.buildings.contains(silo))){
            silo = null;
        }
        if(silo!=null&&Core.game.distance(this, silo)<100&&silo.power>=100&&power<=maxPower-10){
            silo.power-=100;
            power+=10;
        }
        power = Math.max(0,Math.min(maxPower, power));
        if(en!=null){
            target = new double[]{en.x,en.y};
            if(Core.game.distance(this, en)<=en.width/2+width*5){
                fireLaser();
            }
            if(en!=null){
                if(Core.game.distance(this, en)<=en.width/2+width){
                    target = new double[]{x,y};
                }
            }
        }else{
            target = new double[]{x,y};
            findEnemy();
        }
        if(silo!=null&&power<20*30){
            charge = true;
        }
        if(charge){
            if(silo==null||power>=20*60*2.5){
                charge = false;
            }
            target = new double[]{silo.x+silo.width/2,silo.y+silo.height/2};
        }
        if(power<20*15){
            laserFiring = null;
        }
        if(laserFiring!=null){
            power-=laserPower;
            en.health-=laserPower;
        }
        if(x<target[0]){
            x+=speed;
        }
        if(x>target[0]){
            x-=speed;
        }
        if(y<target[1]){
            y+=speed;
        }
        if(y>target[1]){
            y-=speed;
        }
    }
    double[] laserFiring = null;
    MenuComponentEnemy en = null;
    private void findEnemy(){
        double dist = Double.POSITIVE_INFINITY;
        MenuGame game = Core.game;
        en = null;
        for(MenuComponentEnemy enemy : game.enemies){
            if(!(enemy instanceof EnemyMeteorStrike)){
                dist = Math.min(dist,game.distance(this, enemy));
                en = enemy;
            }
        }
        if(en==null&&game.mothership!=null){
            en = game.mothership;
        }
    }
    private void fireLaser(){
        laserFiring = null;
        double dist = Double.POSITIVE_INFINITY;
        MenuGame game = Core.game;
        en = null;
        for(MenuComponentEnemy enemy : game.enemies){
            if(!(enemy instanceof EnemyMeteorStrike)){
                dist = Math.min(dist,game.distance(this, enemy));
                en = enemy;
            }
        }
        if(en==null&&game.mothership!=null){
            en = game.mothership;
        }
        if(en==null) return;
        laserSize+=laserSizing;
        if(laserSize>=25){
            laserSizing*=-1;
        }
        if(laserSize<=15){
            laserSizing*=-1;
        }
        laserFiring = new double[]{en.x,en.y};
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(0, 0, 0, 0, 0);
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
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, ImageStash.instance.getTexture("/textures/drone.png"));
        GL11.glColor4d(0, 0, 0, 1);
        drawRect(x-width/2, y+height/2, x+width/2, y+height/2+5, 0);
        GL11.glColor4d(0, 0.25, 1, 1);
        drawRectWithBounds(x-width/2,y+height/2,x-width/2+(width*(power/(double)maxPower)),y+height/2+5,x-width/2+1, y+height/2+1, x+width/2-1, y+height/2+5-1, 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
}