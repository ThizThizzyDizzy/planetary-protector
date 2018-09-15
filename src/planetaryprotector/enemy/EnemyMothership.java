package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.MenuComponentGenerator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.building.BuildingPowerConsumer;
import simplelibrary.opengl.ImageStash;
public class EnemyMothership extends MenuComponentEnemy{
    public int initialDelay = 20*10;
    public double laserPower = 15;
    public double laserSize = 75;
    public double laserSizing = 2.5D;
    public double powerLaserDamage = 3;
    public double powerLaserPower = 100;
    public double powerLaserSize = 75;
    public double powerLaserSizing = 2.5D;
    public int explosions = 25;
    public int phase = 1;
    public int explosionTimer = 10;
    public static final int maxHealth = 300000;
    private final MenuGame game;
    public boolean leaving = false;
    public EnemyMothership(){
        super(Display.getWidth()/2, 100, 250, 175, maxHealth);
        game = Core.game;
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(1, 0, 1, 1);
        drawRect(0, 0, 0, 0, 0);
        if(powerLaserFiring!=null){
            double xDiff = powerLaserFiring.x+powerLaserFiring.width/2-x;
            double yDiff = powerLaserFiring.y+powerLaserFiring.height/2-y;
            double dist = Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(0, 0, 1, 1);
                MenuGame.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), laserSize/2D,10,0);
            }
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(.125, .5, 1, 1);
                MenuGame.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), (laserSize*(2/3D))/2D,10,0);
            }
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(.25, 1, 1, 1);
                MenuGame.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), (laserSize*(1/3D))/2D,10,0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
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
        width = height = 250*((initialDelay/20D)+1);
        double opacity = 1;
        if(width>Display.getWidth()*(3/4D)){
            double d = width-Display.getWidth()*(3/4D);
            opacity = 1-(d/(Display.getWidth()*(1/4D)));
        }
        GL11.glColor4d(1, 1, 1, opacity);
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, ImageStash.instance.getTexture("/textures/enemies/mothership "+phase+".png"));
        GL11.glColor4d(0, 0, 0, opacity);
        drawRect(x-width/2, y+height/2, x+width/2, y+height/2+10, 0);
        if(phase==1){
            GL11.glColor4d(0, 1, 0, opacity);
        }
        if(phase==2){
            GL11.glColor4d(1, 1, 0, opacity);
        }
        if(phase==3){
            GL11.glColor4d(1, 0.5, 0, opacity);
        }
        if(phase>=4){
            GL11.glColor4d(1, 0, 0, opacity);
        }
        drawRectWithBounds(x-width/2,y+height/2,x-width/2+(width*(health/(double)maxHealth)),y+height/2+10,x-width/2+1, y+height/2+1, x+width/2-1, y+height/2+10-1, 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        if(health<=0){
            laserTimers = new int[0];
            landingPartyTimers = new int[0];
            if(asteroidLaser!=null){
                game.componentsToRemove.add(asteroidLaser);
                asteroidLaser = null;
            }
            if(asteroidAttack!=null){
                game.componentsToRemove.add(asteroidAttack);
                asteroidAttack = null;
            }
            dead = true;
        }
        if(dead){
            if(explosions>0){
                explosionTimer--;
                if(explosionTimer<=10){
                    explosionTimer+=10;
                    explosions--;
                    game.addParticleEffect(new MenuComponentParticle(MenuGame.rand.nextInt((int) width)+x-width/2, MenuGame.rand.nextInt((int) height)+y-height/2, ParticleEffectType.EXPLOSION, MenuGame.rand.nextInt(3)+2, true));
                }
            }else if(explosions==0){
                explosions--;
                game.addParticleEffect(new MenuComponentParticle(x, y, ParticleEffectType.EXPLOSION, 9, true));
                game.win();
            }
            return;
        }
        if(leaving){
            initialDelay++;
            if(initialDelay>20*10){
                game.componentsToRemove.add(this);
                game.mothership = null;
            }
        }
        double hp = health/(double)maxHealth;
        switch(phase){
            case 1:
                if(hp<.75){
                    phase++;
                    strength+=2.5;
                    laserTimers = new int[]{20*60, 20*60*6};
                    landingPartyTimers = new int[]{20*60*3};
                    Sounds.fadeSound("music");
                }
                break;
            case 2:
                if(hp<.5){
                    phase++;
                    strength+=5;
                    laserTimers = new int[]{20*60, 20*60*2, 20*60*3, 20*60*4,};
                    landingPartyTimers = new int[]{20*60*3, 20*60*6};
                    Sounds.fadeSound("music");
                }
                break;
            case 3:
                if(hp<.25){
                    phase++;
                    strength+=10;
                    laserTimers = new int[]{20*60, 20*60*2, 20*60*3, 20*60*4, 20*60*5, 20*60*6};
                    landingPartyTimers = new int[]{20*60*2, 20*60*4, 20*60*6};
                    Sounds.fadeSound("music");
                }
                break;
        }
        if(initialDelay>0&&!leaving){
            initialDelay--;
            return;
        }
        switch(phase){
            case 4:
                firePowerLaserRandomly();
                repairTime = 1;
            case 3:
                if(phase==3){
                    repairTime = 5;
                }
                if(asteroidLaser==null){
                    asteroidLaser = game.add(new MothershipAsteroidLaser());
                }else{
                    if(asteroidLaserDelay>0){
                        asteroidLaserDelay--;
                    }else{
                        asteroidLaser.tick();
                    }
                }
            case 2:
                fireLaserRandomly();
            case 1:
                if(asteroidAttack==null){
                    startAsteroidAttack();
                }else{
                    asteroidAttack.tick();
                }
        }
        if(repairTime>-1){
            repairTimer--;
            if(repairTimer<0){
                health++;
                if(health>maxHealth){
                    health = maxHealth;
                }
                repairTimer+=repairTime;
            }
        }
        for(int i : laserTimers){
            i--;
            if(i<0){
                i+=20*60*5;
                game.enemies.add(game.add(new EnemyLaser(game)));
            }
        }
        for(int i : landingPartyTimers){
            i--;
            if(i<0){
                i+=20*60*10;
                game.enemies.add(game.add(new EnemyLandingParty(game)));
            }
        }
        if(EnemyMeteorStrike.getMeteorStrike()!=null){
            EnemyMeteorStrike strike = new EnemyMeteorStrike(game);
            strike.initialDelay = 20;
            game.enemies.add(game.add(strike));
        }
    }
    int repairTime = -1;
    int repairTimer = 0;
    int[] laserTimers = new int[]{20*60};
    int[] landingPartyTimers = new int[]{};
    double[] laserFiring = null;
    double randomLaserTimer = 100;
    double randomLaserTime = 40;
    double randomLaserDelay = 50;
    MenuComponentBuilding powerLaserFiring = null;
    double powerLaserTimer = 100;
    double powerLaserTime = 400;
    double powerLaserDelay = 500;
    int[] begin;
    int[] end;
    int asteroidLaserDelay = 20;
    MothershipAsteroidAttack asteroidAttack;
    MothershipAsteroidLaser asteroidLaser;
    private void fireLaserRandomly(){
        if(randomLaserTimer>=0){
            laserFiring = null;
            randomLaserTimer--;
            if(randomLaserTimer==0){
                begin = new int[]{MenuGame.rand.nextInt(Display.getWidth()),MenuGame.rand.nextInt(Display.getHeight())};
                end = new int[]{MenuGame.rand.nextInt(Display.getWidth()),MenuGame.rand.nextInt(Display.getHeight())};
                randomLaserTimer = -randomLaserTime;
            }
        }else{
            randomLaserTimer++;
            double percent = (-randomLaserTimer)/randomLaserTime;
            double[] diff = new double[]{end[0]-begin[0],end[1]-begin[1]};
            laserFiring = new double[]{diff[0]*percent+begin[0],diff[1]*percent+begin[1]};
            fireLaser();
            if(randomLaserTimer==0){
                randomLaserTimer = randomLaserDelay;
            }
        }
    }
    private void firePowerLaserRandomly(){
        boolean redo = false;
        if(powerLaserFiring!=null){
            if(powerLaserFiring instanceof MenuComponentGenerator){
                if(((MenuComponentGenerator) powerLaserFiring).power<powerLaserPower){
                    redo = true;
                }
            }
            if(powerLaserFiring instanceof BuildingPowerConsumer){
                if(((BuildingPowerConsumer) powerLaserFiring).power<powerLaserPower){
                    redo = true;
                }
            }
        }
        if(powerLaserTimer>=0||redo){
            powerLaserFiring = null;
            powerLaserTimer--;
            if(powerLaserTimer==0||redo){
                for(MenuComponentBuilding b : game.buildings){
                    if(b instanceof MenuComponentGenerator){
                        if(((MenuComponentGenerator) b).power>=powerLaserPower){
                            powerLaserFiring = b;
                            break;
                        }
                    }
                    if(b instanceof BuildingPowerConsumer){
                        if(((BuildingPowerConsumer) b).power>=powerLaserPower){
                            powerLaserFiring = b;
                            break;
                        }
                    }
                }
                if(powerLaserFiring!=null){
                    powerLaserTimer = -powerLaserTime;
                }else{
                    powerLaserTimer++;
                }
            }
        }else{
            powerLaserTimer++;
            if(powerLaserFiring instanceof BuildingPowerConsumer){
                ((BuildingPowerConsumer)powerLaserFiring).power-=powerLaserPower;
            }
            if(powerLaserFiring instanceof MenuComponentGenerator){
                ((MenuComponentGenerator)powerLaserFiring).power-=powerLaserPower;
            }
            firePowerLaser();
            if(powerLaserTimer==0){
                powerLaserTimer = powerLaserDelay;
            }
        }
    }
    private void fireLaser(){
        laserSize+=laserSizing;
        if(laserSize>=80){
            laserSizing = -1;
        }
        if(laserSize<=70){
            laserSizing = 1;
        }
        game.damage(laserFiring[0], laserFiring[1], (int)laserPower);
        game.pushParticles(laserFiring[0], laserFiring[1], laserSize*1.5, laserSize/2);
    }
    private void firePowerLaser(){
        powerLaserSize+=powerLaserSizing;
        if(powerLaserSize>=80){
            powerLaserSizing = -1;
        }
        if(powerLaserSize<=70){
            powerLaserSizing = 1;
        }
        game.damage(powerLaserFiring.x+powerLaserFiring.width/2,powerLaserFiring.y+powerLaserFiring.height/2, (int)powerLaserDamage);
    }
    private void startAsteroidAttack(){
        asteroidAttack = game.add(new MothershipAsteroidAttack(this));
    }
    public void shieldBlast(){
        health -= 5000;
        if(asteroidAttack!=null){
            game.componentsToRemove.add(asteroidAttack);
            asteroidAttack = null;
        }
        randomLaserTimer+=100;
        asteroidLaserDelay += 100;
    }
}