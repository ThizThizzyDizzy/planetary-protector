package planetaryprotector.particle;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
import java.util.Iterator;
import org.joml.Matrix4f;
import planetaryprotector.Options;
import planetaryprotector.game.Game;
import planetaryprotector.menu.component.GameObjectAnimated;
import planetaryprotector.structure.Structure;
public class Particle extends GameObjectAnimated{
    public final ParticleEffectType type;
    protected float rotation;
    public float opacity = 1;
    protected int radius;
    private final int size;
    public boolean air = false;
    private float xing = 0;//used to smooth out double-based movements over longer X distance
    //clouds
    public float strength;
    private double rateOfChange;
    public static final int rainThreshold = 25;
    public static final int lightningThreshold = 40;
    private double speed = 1;
    protected double rotSpeed = 1;
    private static final double lightningChance = 0.0005;
    //FIRE
    public boolean fading;
    public Lightning lightning = null;
    private ArrayList<float[]> snow = new ArrayList<>();
    public Particle(Game game, int x, int y, ParticleEffectType type){
        this(game, x, y, type, 1);
    }
    public Particle(Game game, int x, int y, ParticleEffectType type, int size){
        this(game, x, y, type, size, false);
    }
    public Particle(Game game, int x, int y, ParticleEffectType type, int size, boolean air){
        super(game, x, y, 50, 50, type.images);
        this.type = type;
        this.size = size;
        this.air = air;
        if(type==ParticleEffectType.CLOUD){
            rotation = game.rand.nextInt(360);
            strength = game.rand.nextInt(25);
            rateOfChange = (game.rand.nextDouble()-.25)/100;
            this.air = true;
        }
    }
    public Particle(Game game, int x, int y, float cloudStrength, double cloudRateOfChange, double speed){
        this(game, x, y, ParticleEffectType.CLOUD);
        strength = cloudStrength;
        rateOfChange = cloudRateOfChange;
        this.speed = speed;
    }
    @Override
    public void draw(){
        if(opacity<=0){
            dead = true;
        }
        if(dead){
            return;
        }
        float x = this.x+xing;
        switch(type){
            case EXPLOSION:
                //smoke
                for(int j = 0; j<5; j++){
                    int radius = this.radius+5*j;
                    int c = 90*(Options.options.particles+1);
                    for(int i = 0; i<c; i++){
                        float X = (float)(Math.cos(Math.toRadians(i*(360d/c)))*radius+x-width/2);
                        float Y = (float)(Math.sin(Math.toRadians(i*(360d/c)))*radius+y-height/2);
                        Renderer.setColor(0, 0, 0, opacity);
                        Renderer.fillRect(X, Y, X+width, Y+height, ResourceManager.getTexture(ParticleEffectType.SMOKE.images[0]));
                        Renderer.setColor(1, 1, 1, 1);
                    }
                }
                //explosion
                for(int j = 0; j<10; j++){
                    float radius = this.radius/10f+2*j;
                    int c = 22*(Options.options.particles+1);
                    for(int i = 0; i<45; i++){
                        float X = (float)(Math.cos(Math.toRadians(i*(360d/c)))*radius+x-width/2);
                        float Y = (float)(Math.sin(Math.toRadians(i*(360d/c)))*radius+y-height/2);
                        Renderer.setColor(1, opacity, 0, opacity);
                        Renderer.fillRect(X, Y, X+width, Y+height, ResourceManager.getTexture(images[0]));
                        Renderer.setColor(1, 1, 1, 1);
                    }
                }
                return;
            case SMOKE:
                Renderer.setColor(0, 0, 0, opacity);
                Renderer.pushModel(new Matrix4f().translate(x+width/2, y+height/2, 0).rotate(rotation, 0, 0, 1));
                Renderer.fillRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ResourceManager.getTexture(images[0]));
                Renderer.popModel();
                Renderer.setColor(1, 1, 1, 1);
                return;
            case CLOUD:
                if(strength>lightningThreshold){
                    //add lightning
                    if(lightning==null&&game.rand.nextDouble()*strength>lightningThreshold&&game.rand.nextDouble()<lightningChance){
                        lightning = new Lightning();
                        float lastX = 0;
                        float lastY = 0;
                        float Y = 0;
                        float X = 0;
                        while(Y<400){
                            Y = lastY+game.rand.nextInt(40)+10;
                            X = (float)(lastX+(game.rand.nextDouble()-.5)*30);
                            lightning.addLine(x+width/2+lastX, y+height/2+lastY, x+width/2+X, y+height/2+Y);
                            if(game.rand.nextBoolean()){
                                float YY = lastY+game.rand.nextInt(40)+10;
                                float XX = (float)(lastX+(game.rand.nextDouble()-.5)*30);
                                lightning.addBranch(x+width/2+lastX, y+height/2+lastY, x+width/2+XX, y+height/2+YY);
                            }
                            lastX = X;
                            lastY = Y;
                        }
                        Renderer.fillRect(x+width/2+X, y+height/2+Y, x+width/2+X+1, y+height/2+Y+1);
                    }
                }
                if(strength>rainThreshold){
                    //draw rain
                    if(Game.theme==Game.Theme.SNOWY){
                        Renderer.setColor(1, 1, 1, .875f*Options.options.cloudIntensity);
                        for(float[] i : snow){
                            Renderer.fillRect(x+i[0]+width/2, y+i[1]+i[2]+height/2, x+i[0]+width/2+1, y+i[1]+i[2]+height/2+1);
                        }
                    }else{
                        Renderer.setColor(0, 0, 1, .75f*Options.options.cloudIntensity);
                        for(int i = 0; i<strength; i++){
                            float X = game.rand.nextInt((int)width)-width/2;
                            float Y = game.rand.nextInt((int)(height+500))-height/2;
                            Renderer.fillRect(x+width/2+X, y+height/2+Y, x+width/2+X+1, y+height/2+Y+5, 0);
                        }
                    }
                }
                if(lightning!=null){
                    //draw lightning
                    lightning.render();
                    if(lightning.dead){
                        lightning = null;
                    }
                }
                opacity = (float)(Math.log10(strength/(rainThreshold/4))*.75f);
                float lightness = 1-((strength-(rainThreshold*(3/4f)))/50);
                if(Game.theme==Game.Theme.SNOWY)
                    lightness = (float)Math.sqrt(lightness);
                Renderer.setColor(lightness, lightness, lightness, opacity*Options.options.cloudIntensity);
                Renderer.pushModel(new Matrix4f().translate(x+width/2, y+height/2, 0).rotate(rotation, 0, 0, 1));
                Renderer.fillRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ResourceManager.getTexture(images[0]));
                Renderer.popModel();
                Renderer.setColor(1, 1, 1, 1);
                return;
            case FIRE:
                for(float[] i : smoke){
                    float r = Math.max(0, Math.min(1, (10-i[4])/10));
                    float g = Math.max(0, Math.min(1, (10-i[4])/20));
                    Renderer.setColor(r, g, 0, i[3]);
                    Renderer.pushModel(new Matrix4f().translate(x+i[0]+width/2, y+i[1]-i[5]+height/2, 0).rotate(i[2], 0, 0, 1));
                    Renderer.fillRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ResourceManager.getTexture(images[0]));
                    Renderer.popModel();
                }
                Renderer.setColor(1, 1, 1, 1);
                if(fading)return;
                Renderer.setColor(1, .5f, 0, opacity);
                Renderer.pushModel(new Matrix4f().translate(x+width/2, y+height/2, 0).rotate(rotation, 0, 0, 1));
                Renderer.fillRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ResourceManager.getTexture(images[0]));
                Renderer.popModel();
                Renderer.setColor(1, 1, 1, 1);
                return;
            default:
                Renderer.setColor(1, 1, 1, opacity);
                Renderer.pushModel(new Matrix4f().translate(x+width/2, y+height/2, 0).rotate(rotation, 0, 0, 1));
                Renderer.fillRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ResourceManager.getTexture(images[0]));
                Renderer.popModel();
                Renderer.setColor(1, 1, 1, 1);
        }
    }
    private int smokeTimer;
    private ArrayList<float[]> smoke = new ArrayList<>();
    @Override
    public void tick(){
        if(opacity<=0){
            return;
        }
        if(type==ParticleEffectType.EXPLOSION){
            opacity -= 0.005*(11-size);
            radius += 5+0.5*((11-size));
            game.pushParticles(x+width/2, y+height/2, radius, (5+.5*((11-size)))*Math.min(1, opacity*5), PushCause.EXPLOSION);
            if(size>=10){
                for(Structure structure : game.structures){
                    if(structure.getCenter().distance(x, y)<=radius&&!structure.type.isBackgroundStructure()){
                        structure.onHit(structure.x-25, structure.y+structure.height-25);
                    }
                }
            }
            return;
        }
        if(type==ParticleEffectType.SMOKE){
            rotation += 0.01;
            opacity -= 0.05;
        }
        if(type==ParticleEffectType.CLOUD){
            strength += rateOfChange;
            rotation += rotSpeed;
            if(x>game.getCityBoundingBox().getRight()+game.getXGamePadding()){
                dead = true;
            }
            xing += speed;
            int actualX = (int)xing;
            x += actualX;
            xing -= actualX;
            for(Iterator<float[]> it = snow.iterator(); it.hasNext();){
                float[] i = it.next();
                if(game.rand.nextBoolean()){
                    i[0] += game.rand.nextInt(3)-1;
                }
                i[0] -= speed*.1;
                i[2]++;
                if(i[2]>=500){
                    it.remove();
                }
            }
            for(int i = 0; i<strength/100; i++){
                float X = game.rand.nextInt((int)width)-width/2;
                float Y = game.rand.nextInt((int)height)-height/2;
                snow.add(new float[]{X, Y, 0});
            }
        }
        if(type==ParticleEffectType.FIRE){
            smokeTimer++;
            if(fading&&smoke.isEmpty())dead = true;
            int delay = 5;
            switch(Options.options.particles){
                case 0:
                    delay = 10;
                    break;
                case 1:
                    delay = 5;
                    break;
                case 2:
                    delay = 2;
                    break;
            }
            if(smokeTimer>=delay&&!fading){
                smokeTimer = 0;
                smoke.add(new float[]{0, 0, 0, 0, 0, 0});
            }
            for(Iterator<float[]> it = smoke.iterator(); it.hasNext();){
                float[] i = it.next();
                i[4]++;
                i[0] = i[4]*3;
                i[1] = (float)(-Math.sqrt(i[4])*10);
                i[2] = i[4]*10;
                i[3] = 1-(i[4]/200);
                if(i[3]<=0){
                    it.remove();
                }
            }
        }
    }
    /**
     * move sub-particles up.
     * used to move smoke up on falling skyscrapers.
     *
     * @param i how far to move them.
     */
    public void offsetSubParticles(int i){
        for(float[] d : smoke){
            d[5] += i;
        }
    }
    public void push(double x, double y, PushCause cause){
        if(type==ParticleEffectType.EXPLOSION)return;
        if(type==ParticleEffectType.SMOKE){
            if(x>1||y>1){
                x /= Math.max(x, y);
                y /= Math.max(x, y);
            }
        }
        if(type==ParticleEffectType.FIRE){
            if(cause==PushCause.ASTEROID)return;
            for(float[] d : smoke){
                d[1] += x;
                d[2] += y;
            }
            return;
        }
        if(type==ParticleEffectType.CLOUD){
            for(float[] d : snow){
                d[0] -= x;
                d[1] -= y;
            }
        }
        this.x += x;
        this.y += y;
    }
    public void fade(double amount){
        if(this instanceof ParticleFog){
            ((ParticleFog)this).opacity -= amount/10;
        }
        if(type==ParticleEffectType.CLOUD){
            strength -= amount;
        }
    }
    private static class Lightning{
        public LightningLine line = null;
        public boolean dead = false;
        private void addLine(double x1, double y1, double x2, double y2){
            if(line==null){
                line = new LightningLine(x1, y1, x2, y2);
            }else{
                line.add(new LightningLine(x1, y1, x2, y2));
            }
        }
        private void addBranch(double x1, double y1, double x2, double y2){
            line.addBranch(new LightningLine(x1, y1, x2, y2));
        }
        public void render(){
            if(line.opacity<=0){
                dead = true;
            }
            line.render();
        }
    }
    private static class LightningLine{
        private final double x1;
        private final double y1;
        private final double x2;
        private final double y2;
        private boolean hasRendered = false;
        private LightningLine nextLine = null;
        private LightningLine branch = null;
        private boolean goBack;
        private boolean first = true;
        private float opacity = 5;
        public LightningLine(double x1, double y1, double x2, double y2){
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
        public void render(){
            if(first&&goBack){
                opacity -= .375;
            }
            if(nextLine!=null){
                nextLine.opacity = opacity;
            }
            if(branch!=null){
                branch.opacity = opacity;
            }
            Renderer.setColor(1, 1, .25f, opacity*Options.options.cloudIntensity);
            Renderer.drawText((float)x1, (float)y1, "TODO Lightning lines", 40);
//            ResourceManager.bindTexture(0);
//            GL11.glBegin(GL11.GL_LINES);
//            GL11.glVertex2d(x1, y1);
//            GL11.glVertex2d(x2, y2);
//            GL11.glEnd();
            if(hasRendered){
                if(nextLine==null){
                    goBack = true;
                }
                if(nextLine!=null){
                    nextLine.render();
                }
                if(nextLine!=null&&nextLine.goBack){
                    goBack = true;
                }
                if(branch!=null){
                    branch.render();
                }
            }
            hasRendered = true;
        }
        private void add(LightningLine line){
            line.first = false;
            if(nextLine==null){
                nextLine = line;
            }else{
                nextLine.add(line);
            }
        }
        private void addBranch(LightningLine line){
            line.first = false;
            if(nextLine==null){
                branch = line;
            }else{
                nextLine.addBranch(line);
            }
        }
    }
    /**
     * @return the CENTER of the particle
     */
    public double getX(){
        return x+width/2;
    }
    /**
     * @return the CENTER of the particle
     */
    public double getY(){
        return y+height/2;
    }
    public static enum PushCause{
        ASTEROID, EXPLOSION, LASER, SHEILD_BLAST;
    }
}
