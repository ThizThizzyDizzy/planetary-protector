package planetaryprotector.particle;
import planetaryprotector.Core;
import planetaryprotector.building.Building;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.BuildingDamage;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.component.GameObjectAnimated;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class Particle extends GameObjectAnimated{
    public final ParticleEffectType type;
    protected int rotation;
    public double opacity = 1;
    protected int radius;
    private final int size;
    public boolean air = false;
    //clouds
    public double strength;
    private double rateOfChange;
    public static final int rainThreshold = 25;
    public static final int lightningThreshold = 40;
    private double speed = 1;
    protected double rotSpeed = 1;
    private static final double lightningChance = 0.0005;
    //FIRE
    public boolean fading;
    public Lightning lightning = null;
    public Particle(double x, double y, ParticleEffectType type){
        this(x, y, type, 1);
    }
    public Particle(double x, double y, ParticleEffectType type, int size){
        this(x, y, type, size, false);
    }
    public Particle(double x, double y, ParticleEffectType type, int size, boolean air){
        super(x, y, 50, 50, type.images);
        this.type = type;
        this.size = size;
        this.air = air;
        if(type==ParticleEffectType.CLOUD){
            rotation = Core.game.rand.nextInt(360);
            strength = Core.game.rand.nextInt(25);
            rateOfChange = (Core.game.rand.nextDouble()-.25)/100;
            this.air = true;
        }
    }
    public Particle(double x, double y, double cloudStrength, double cloudRateOfChange, double speed){
        this(x, y, ParticleEffectType.CLOUD);
        strength = cloudStrength;
        rateOfChange = cloudRateOfChange;
        this.speed = speed;
    }
    @Override
    public void render(){
        if(opacity<=0){
            dead = true;
        }
        if(dead){
            return;
        }
        if(type==ParticleEffectType.EXPLOSION){
            //smoke
            for(int j = 0; j<5; j++){
                int radius = this.radius+5*j;
                int c = 90*(MenuOptionsGraphics.particles+1);
                for(int i = 0; i<c; i++){
                    double X = Math.cos(Math.toRadians(i*(360d/c)))*radius+x-width/2;
                    double Y = Math.sin(Math.toRadians(i*(360d/c)))*radius+y-height/2;
                    GL11.glColor4d(0, 0, 0, opacity);
                    drawRect(X, Y, X+width, Y+height, ParticleEffectType.SMOKE.images[0]);
                    GL11.glColor4d(1, 1, 1, 1);
                }
            }
            //explosion
            for(int j = 0; j<10; j++){
                double radius = this.radius/10D+2*j;
                int c = 22*(MenuOptionsGraphics.particles+1);
                for(int i = 0; i<45; i++){
                    double X = Math.cos(Math.toRadians(i*(360d/c)))*radius+x-width/2;
                    double Y = Math.sin(Math.toRadians(i*(360d/c)))*radius+y-height/2;
                    GL11.glColor4d(1, opacity, 0, opacity);
                    drawRect(X, Y, X+width, Y+height, images[0]);
                    GL11.glColor4d(1, 1, 1, 1);
                }
            }
            return;
        }
        if(type==ParticleEffectType.SMOKE){
            GL11.glColor4d(0, 0, 0, opacity);
            GL11.glPushMatrix();
            GL11.glTranslated(x+width/2, y+height/2, 0);
            GL11.glRotated(rotation, 0, 0, 1);
            drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), images[0]);
            GL11.glPopMatrix();
            GL11.glColor4d(1, 1, 1, 1);
            return;
        }
        if(type==ParticleEffectType.CLOUD){
            if(strength>lightningThreshold){
                //add lightning
                if(lightning==null&&Core.game.rand.nextDouble()*strength>lightningThreshold&&Core.game.rand.nextDouble()<lightningChance){
                    lightning = new Lightning();
                    double lastX = 0;
                    double lastY = 0;
                    double Y = 0;
                    double X = 0;
                    while(Y<400){
                        Y = lastY+Core.game.rand.nextInt(40)+10;
                        X = lastX+(Core.game.rand.nextDouble()-.5)*30;
                        lightning.addLine(x+width/2+lastX, y+height/2+lastY, x+width/2+X, y+height/2+Y);
                        if(Core.game.rand.nextBoolean()){
                            double YY = lastY+Core.game.rand.nextInt(40)+10;
                            double XX = lastX+(Core.game.rand.nextDouble()-.5)*30;
                            lightning.addBranch(x+width/2+lastX, y+height/2+lastY, x+width/2+XX, y+height/2+YY);
                        }
                        lastX = X;
                        lastY = Y;
                    }
                    drawRect(x+width/2+X, y+height/2+Y, x+width/2+X+1, y+height/2+Y+1, 0);
                }
            }
            if(strength>rainThreshold){
                //draw rain
                GL11.glColor4d(0, 0, 1, .75*MenuOptionsGraphics.cloudIntensity);
                for(int i = 0; i<strength; i++){
                    double X = Core.game.rand.nextInt((int)width)-width/2;
                    double Y = Core.game.rand.nextInt((int)(height+500))-height/2;
                    drawRect(x+width/2+X, y+height/2+Y, x+width/2+X+1, y+height/2+Y+5, 0);
                }
            }
            if(lightning!=null){
                //draw lightning
                lightning.render();
                if(lightning.dead){
                    lightning = null;
                }
            }
            opacity = Math.log10(strength/(rainThreshold/4))*.75;
            double lightness = 1-((strength-(rainThreshold*(3/4d)))/50);
            GL11.glColor4d(lightness, lightness, lightness, opacity*MenuOptionsGraphics.cloudIntensity);
            GL11.glPushMatrix();
            GL11.glTranslated(x+width/2, y+height/2, 0);
            GL11.glRotated(rotation, 0, 0, 1);
            drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), images[0]);
            GL11.glPopMatrix();
            GL11.glColor4d(1, 1, 1, 1);
            return;
        }
        if(type==ParticleEffectType.FIRE){
            synchronized(smoke){
                for(double[] i : smoke){
                    double r = Math.max(0,Math.min(1,(10-i[4])/10));
                    double g = Math.max(0,Math.min(1,(10-i[4])/20));
                    GL11.glColor4d(r, g, 0, i[3]);
                    GL11.glPushMatrix();
                    GL11.glTranslated(x+i[0]+width/2, y+i[1]-i[5]+height/2, 0);
                    GL11.glRotated(i[2], 0, 0, 1);
                    drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), images[0]);
                    GL11.glPopMatrix();
                }
            }
            GL11.glColor4d(1, 1, 1, 1);
            if(fading)return;
            GL11.glColor4d(1, .5, 0, opacity);
            GL11.glPushMatrix();
            GL11.glTranslated(x+width/2, y+height/2, 0);
            GL11.glRotated(rotation, 0, 0, 1);
            drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), images[0]);
            GL11.glPopMatrix();
            GL11.glColor4d(1, 1, 1, 1);
            return;
        }
        GL11.glColor4d(1, 1, 1, opacity);
        GL11.glPushMatrix();
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rotation, 0, 0, 1);
        drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), images[0]);
        GL11.glPopMatrix();
        GL11.glColor4d(1, 1, 1, 1);
    }
    private int smokeTimer;
    private ArrayList<double[]> smoke = new ArrayList<>();
    @Override
    public void tick(){
        if(opacity<=0){
            return;
        }
        if(type==ParticleEffectType.EXPLOSION){
            opacity-=0.005*(11-size);
            radius+=5+0.5*((11-size));
            Core.game.pushParticles(x+width/2, y+height/2, radius, (5+.5*((11-size)))*Math.min(1, opacity*5));
            if(size>=10){
                synchronized(Core.game.buildings){
                    for(Building building : Core.game.buildings){
                        if(building.type==BuildingType.WRECK||building.type==BuildingType.EMPTY){
                            continue;
                        }
                        if(Core.distance(building, x, y)<=radius&&building.damages.size()<=10){
                            synchronized(building.damages){
                                building.damages.add(new BuildingDamage(building, building.x-25, building.y+building.height-25));
                            }
                        }
                    }
                }
            }
            return;
        }
        if(type==ParticleEffectType.SMOKE){
            rotation+=0.01;
            opacity-=0.05;
        }
        if(type==ParticleEffectType.CLOUD){
            strength+=rateOfChange;
            rotation+=rotSpeed;
            x+=speed;
            if(x>Display.getWidth()+speed){
                dead = true;
            }
        }
        if(type==ParticleEffectType.FIRE){
            smokeTimer++;
            if(fading&&smoke.isEmpty())dead = true;
            int delay = 5;
            switch(MenuOptionsGraphics.particles){
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
                synchronized(smoke){
                    smoke.add(new double[]{0,0,0,0,0,0});
                }
            }
            synchronized(smoke){
                for(Iterator<double[]> it = smoke.iterator(); it.hasNext();){
                    double[] i = it.next();
                    i[4]++;
                    i[0] = i[4]*3;
                    i[1] = -Math.sqrt(i[4])*10;
                    i[2] = i[4]*10;
                    i[3] = 1-(i[4]/200);
                    if(i[3]<=0){
                        it.remove();
                    }
                }
            }
        }
    }
    /**
     * move sub-particles up.
     * used to move smoke up on falling skyscrapers.
     * @param i how far to move them.
     */
    public void offsetSubParticles(int i){
        for(double[] d : smoke){
            d[5]+=i;
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
        private double opacity = 5;
        public LightningLine(double x1, double y1, double x2, double y2){
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
        public void render(){
            if(first&&goBack){
                opacity-=.375;
            }
            if(nextLine!=null){
                nextLine.opacity = opacity;
            }
            if(branch!=null){
                branch.opacity = opacity;
            }
            GL11.glColor4d(1, 1, .25, opacity*MenuOptionsGraphics.cloudIntensity);
            ImageStash.instance.bindTexture(0);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2d(x1, y1);
            GL11.glVertex2d(x2, y2);
            GL11.glEnd();
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
}