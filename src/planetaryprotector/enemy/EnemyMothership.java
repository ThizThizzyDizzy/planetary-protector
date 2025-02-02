package planetaryprotector.enemy;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Sounds;
import planetaryprotector.particle.Particle;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.structure.CoalGenerator;
import planetaryprotector.game.BoundingBox;
import planetaryprotector.structure.Structure;
import planetaryprotector.structure.PowerConsumer;
public class EnemyMothership extends Enemy{
    public int initialDelay = 20*10;
    public float laserPower = 15;
    public float laserSize = 75;
    public float laserSizing = 2.5f;
    public float powerLaserDamage = 3;
    public float powerLaserPower = 100;
    public float powerLaserSize = 75;
    public float powerLaserSizing = 2.5f;
    public int explosions = 25;
    public int phase = 1;
    public int explosionTimer = 10;
    public static final int maxHealth = 300000;
    public boolean leaving = false;
    public boolean dying = false;
    public EnemyMothership(Game game){
        super(game, game.getCityBoundingBox().getCenterX(), 100, 250, 175, maxHealth);
    }
    @Override
    public void draw(){
        Renderer.setColor(1, 0, 1, 1);
        Renderer.fillRect(0, 0, 0, 0, 0);
        if(powerLaserFiring!=null){
            float xDiff = powerLaserFiring.x+powerLaserFiring.width/2-x;
            float yDiff = powerLaserFiring.y+powerLaserFiring.height/2-y;
            float dist = (float)Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.setColor(0, 0, 1, 1);
                Renderer.fillRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), 10, powerLaserSize/2f);
            }
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.setColor(.125f, .5f, 1, 1);
                Renderer.fillRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), 10, (powerLaserSize*(2/3f))/2f);
            }
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.setColor(.25f, 1, 1, 1);
                Renderer.fillRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), 10, (powerLaserSize*(1/3f))/2f);
                Renderer.setColor(1, 1, 1, 1);
            }
        }
        Renderer.setColor(1, 1, 1, 1);
        Renderer.fillRect(0, 0, 0, 0, 0);
        if(laserFiring!=null){
            float xDiff = laserFiring[0]-x;
            float yDiff = laserFiring[1]-y;
            float dist = (float)Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.setColor(1, 0, 0, 1);
                Renderer.fillRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), 10, laserSize/2f);
            }
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.setColor(1, .5f, 0, 1);
                Renderer.fillRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), 10, (laserSize*(2/3f))/2f);
            }
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.setColor(1, 1, 0, 1);
                Renderer.fillRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), 10, (laserSize*(1/3f))/2f);
                Renderer.setColor(1, 1, 1, 1);
            }
        }
        width = height = (int)(250*((initialDelay/20D)+1));
        float opacity = 1;
        if(width>game.getCityBoundingBox().width*(3/4D)){//TODO redo
            float d = width-game.getCityBoundingBox().width*(3/4f);
            opacity = 1-(d/(game.getCityBoundingBox().width*(1/4f)));
        }
        Renderer.setColor(1, 1, 1, opacity);
        Renderer.fillRect(x-width/2, y-height/2, x+width/2, y+height/2, ResourceManager.getTexture("/textures/enemies/mothership "+phase+".png"));
        Renderer.setColor(0, 0, 0, opacity);
        Renderer.fillRect(x-width/2, y+height/2, x+width/2, y+height/2+10, 0);
        if(phase==1){
            Renderer.setColor(0, 1, 0, opacity);
        }
        if(phase==2){
            Renderer.setColor(1, 1, 0, opacity);
        }
        if(phase==3){
            Renderer.setColor(1, 0.5f, 0, opacity);
        }
        if(phase>=4){
            Renderer.setColor(1, 0, 0, opacity);
        }
        Renderer.bound(x-width/2+1, y+height/2+1, x+width/2-1, y+height/2+10-1);
        Renderer.fillRect(x-width/2, y+height/2, x-width/2+(width*(health/(float)maxHealth)), y+height/2+10, 0);
        Renderer.unBound();
        Renderer.setColor(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        if(health<=0){
            laserTimers = new int[0];
            landingPartyTimers = new int[0];
            if(asteroidLaser!=null){
                asteroidLaser = null;
            }
            if(asteroidAttack!=null){
                asteroidAttack = null;
            }
            dying = true;
        }
        if(dying){
            if(explosions>0){
                explosionTimer--;
                if(explosionTimer<=10){
                    explosionTimer += 10;
                    explosions--;
                    game.addParticleEffect(new Particle(game, game.rand.nextInt((int)width)+x-width/2, game.rand.nextInt((int)height)+y-height/2, ParticleEffectType.EXPLOSION, game.rand.nextInt(3)+2, true));
                }
            }else if(explosions==0){
                explosions--;
                game.addParticleEffect(new Particle(game, x, y, ParticleEffectType.EXPLOSION, 9, true));
                game.win();
            }
            return;
        }
        if(leaving){
            initialDelay++;
            if(initialDelay>20*10){
                dead = true;
            }
        }
        float hp = health/(float)maxHealth;
        switch(phase){
            case 1:
                if(hp<.75){
                    phase++;
                    strength += 2.5;
                    laserTimers = new int[]{20*60, 20*60*6};
                    landingPartyTimers = new int[]{20*60*3};
                    Sounds.fadeMusic();
                }
                break;
            case 2:
                if(hp<.5){
                    phase++;
                    strength += 5;
                    laserTimers = new int[]{20*60, 20*60*2, 20*60*3, 20*60*4,};
                    landingPartyTimers = new int[]{20*60*3, 20*60*6};
                    Sounds.fadeMusic();
                }
                break;
            case 3:
                if(hp<.25){
                    phase++;
                    strength += 10;
                    laserTimers = new int[]{20*60, 20*60*2, 20*60*3, 20*60*4, 20*60*5, 20*60*6};
                    landingPartyTimers = new int[]{20*60*2, 20*60*4, 20*60*6};
                    Sounds.fadeMusic();
                }
                break;
        }
        if(initialDelay>0&&!leaving){
            initialDelay--;
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
                    asteroidLaser = new MothershipAsteroidLaser(game);
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
        if(asteroidAttack!=null)asteroidAttack.tick();
        if(asteroidLaser!=null)asteroidLaser.tick();
        if(repairTime>-1){
            repairTimer--;
            if(repairTimer<0){
                health++;
                if(health>maxHealth){
                    health = maxHealth;
                }
                repairTimer += repairTime;
            }
        }
        for(int i : laserTimers){
            i--;
            if(i<0){
                i += 20*60*5;
                game.addEnemy(new EnemyLaser(game));
            }
        }
        for(int i : landingPartyTimers){
            i--;
            if(i<0){
                i += 20*60*10;
                game.addEnemy(new EnemyLandingParty(game));
            }
        }
        if(EnemyMeteorStrike.getMeteorStrike(game)!=null){
            EnemyMeteorStrike strike = new EnemyMeteorStrike(game);
            strike.initialDelay = 20;
            game.addEnemy(strike);
        }
    }
    int repairTime = -1;
    int repairTimer = 0;
    int[] laserTimers = new int[]{20*60};
    int[] landingPartyTimers = new int[]{};
    int[] laserFiring = null;
    float randomLaserTimer = 100;
    float randomLaserTime = 40;
    float randomLaserDelay = 50;
    Structure powerLaserFiring = null;
    float powerLaserTimer = 100;
    float powerLaserTime = 400;
    float powerLaserDelay = 500;
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
                BoundingBox bbox = game.getCityBoundingBox();
                begin = new int[]{(int)bbox.randX(game.rand), (int)bbox.randY(game.rand)};
                end = new int[]{(int)bbox.randX(game.rand), (int)bbox.randY(game.rand)};
                randomLaserTimer = -randomLaserTime;
            }
        }else{
            randomLaserTimer++;
            float percent = (-randomLaserTimer)/randomLaserTime;
            int[] diff = new int[]{end[0]-begin[0], end[1]-begin[1]};
            laserFiring = new int[]{(int)(diff[0]*percent+begin[0]), (int)(diff[1]*percent+begin[1])};
            fireLaser();
            if(randomLaserTimer==0){
                randomLaserTimer = randomLaserDelay;
            }
        }
    }
    private void firePowerLaserRandomly(){
        boolean redo = false;
        if(powerLaserFiring!=null){
            if(powerLaserFiring instanceof CoalGenerator){
                if(((CoalGenerator)powerLaserFiring).power<powerLaserPower){
                    redo = true;
                }
            }
            if(powerLaserFiring instanceof PowerConsumer){
                if(((PowerConsumer)powerLaserFiring).getPower()<powerLaserPower){
                    redo = true;
                }
            }
        }
        if(powerLaserTimer>=0||redo){
            powerLaserFiring = null;
            powerLaserTimer--;
            if(powerLaserTimer==0||redo){
                for(Structure s : game.structures){
                    if(s instanceof CoalGenerator){
                        if(((CoalGenerator)s).power>=powerLaserPower){
                            powerLaserFiring = s;
                            break;
                        }
                    }
                    if(s instanceof PowerConsumer){
                        if(((PowerConsumer)s).getPower()>=powerLaserPower){
                            powerLaserFiring = s;
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
            if(powerLaserFiring instanceof PowerConsumer){
                ((PowerConsumer)powerLaserFiring).addPower(-powerLaserPower);
            }
            if(powerLaserFiring instanceof CoalGenerator){
                ((CoalGenerator)powerLaserFiring).power -= powerLaserPower;
            }
            firePowerLaser();
            if(powerLaserTimer==0){
                powerLaserTimer = powerLaserDelay;
            }
        }
    }
    private void fireLaser(){
        if(powerLaserFiring==null)return;
        laserSize += laserSizing;
        if(laserSize>=80){
            laserSizing = -1;
        }
        if(laserSize<=70){
            laserSizing = 1;
        }
        game.damage(laserFiring[0], laserFiring[1], (int)laserPower);
        game.pushParticles(laserFiring[0], laserFiring[1], laserSize*1.5, laserSize/2, Particle.PushCause.LASER);
    }
    private void firePowerLaser(){
        if(powerLaserFiring==null)return;
        powerLaserSize += powerLaserSizing;
        if(powerLaserSize>=80){
            powerLaserSizing = -1;
        }
        if(powerLaserSize<=70){
            powerLaserSizing = 1;
        }
        game.damage(powerLaserFiring.x+powerLaserFiring.width/2, powerLaserFiring.y+powerLaserFiring.height/2, (int)powerLaserDamage);
    }
    private void startAsteroidAttack(){
        asteroidAttack = new MothershipAsteroidAttack(this);
    }
    @Override
    public void shieldBlast(){
        health -= 5000;
        if(asteroidAttack!=null){
            asteroidAttack = null;
        }
        randomLaserTimer += 100;
        asteroidLaserDelay += 100;
    }
}
