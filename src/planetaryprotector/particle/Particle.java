package planetaryprotector.particle;
import planetaryprotector.Core;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import planetaryprotector.game.Game;
import planetaryprotector.menu.component.GameObjectAnimated;
import planetaryprotector.structure.Structure;
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
    private ArrayList<double[]> snow = new ArrayList<>();
    public Particle(Game game, double x, double y, ParticleEffectType type){
        this(game, x, y, type, 1);
    }
    public Particle(Game game, double x, double y, ParticleEffectType type, int size){
        this(game, x, y, type, size, false);
    }
    public Particle(Game game, double x, double y, ParticleEffectType type, int size, boolean air){
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
    public Particle(Game game, double x, double y, double cloudStrength, double cloudRateOfChange, double speed){
        this(game, x, y, ParticleEffectType.CLOUD);
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
        switch(type){
            case EXPLOSION:
                //smoke
                for(int j = 0; j<5; j++){
                    int radius = this.radius+5*j;
                    int c = 90*(MenuOptionsGraphics.particles+1);
                    for(int i = 0; i<c; i++){
                        double X = Math.cos(Math.toRadians(i*(360d/c)))*radius+x-width/2;
                        double Y = Math.sin(Math.toRadians(i*(360d/c)))*radius+y-height/2;
                        GL11.glColor4d(0, 0, 0, opacity);
                        drawRect(X, Y, X+width, Y+height, ImageStash.instance.getTexture(ParticleEffectType.SMOKE.images[0]));
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
                        drawRect(X, Y, X+width, Y+height, ImageStash.instance.getTexture(images[0]));
                        GL11.glColor4d(1, 1, 1, 1);
                    }
                }
                return;
            case SMOKE:
                GL11.glColor4d(0, 0, 0, opacity);
                GL11.glPushMatrix();
                GL11.glTranslated(x+width/2, y+height/2, 0);
                GL11.glRotated(rotation, 0, 0, 1);
                drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ImageStash.instance.getTexture(images[0]));
                GL11.glPopMatrix();
                GL11.glColor4d(1, 1, 1, 1);
                return;
            case CLOUD:
                if(strength>lightningThreshold){
                    //add lightning
                    if(lightning==null&&game.rand.nextDouble()*strength>lightningThreshold&&game.rand.nextDouble()<lightningChance){
                        lightning = new Lightning();
                        double lastX = 0;
                        double lastY = 0;
                        double Y = 0;
                        double X = 0;
                        while(Y<400){
                            Y = lastY+game.rand.nextInt(40)+10;
                            X = lastX+(game.rand.nextDouble()-.5)*30;
                            lightning.addLine(x+width/2+lastX, y+height/2+lastY, x+width/2+X, y+height/2+Y);
                            if(game.rand.nextBoolean()){
                                double YY = lastY+game.rand.nextInt(40)+10;
                                double XX = lastX+(game.rand.nextDouble()-.5)*30;
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
                    if(Game.theme==Game.Theme.SNOWY){
                        GL11.glColor4d(1, 1, 1, .875*MenuOptionsGraphics.cloudIntensity);
                        ImageStash.instance.bindTexture(0);
                        GL11.glBegin(GL11.GL_POINTS);
                        for(double[] i : snow){
                            GL11.glVertex2d(x+i[0]+width/2, y+i[1]+i[2]+height/2);
                        }
                        GL11.glEnd();
                    }else{
                        GL11.glColor4d(0, 0, 1, .75*MenuOptionsGraphics.cloudIntensity);
                        for(int i = 0; i<strength; i++){
                            double X = game.rand.nextInt((int)width)-width/2;
                            double Y = game.rand.nextInt((int)(height+500))-height/2;
                            drawRect(x+width/2+X, y+height/2+Y, x+width/2+X+1, y+height/2+Y+5, 0);
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
                opacity = Math.log10(strength/(rainThreshold/4))*.75;
                double lightness = 1-((strength-(rainThreshold*(3/4d)))/50);
                if(Game.theme==Game.Theme.SNOWY)lightness = Math.sqrt(lightness);
                GL11.glColor4d(lightness, lightness, lightness, opacity*MenuOptionsGraphics.cloudIntensity);
                GL11.glPushMatrix();
                GL11.glTranslated(x+width/2, y+height/2, 0);
                GL11.glRotated(rotation, 0, 0, 1);
                drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ImageStash.instance.getTexture(images[0]));
                GL11.glPopMatrix();
                GL11.glColor4d(1, 1, 1, 1);
                return;
            case FIRE:
                for(double[] i : smoke){
                    double r = Math.max(0,Math.min(1,(10-i[4])/10));
                    double g = Math.max(0,Math.min(1,(10-i[4])/20));
                    GL11.glColor4d(r, g, 0, i[3]);
                    GL11.glPushMatrix();
                    GL11.glTranslated(x+i[0]+width/2, y+i[1]-i[5]+height/2, 0);
                    GL11.glRotated(i[2], 0, 0, 1);
                    drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ImageStash.instance.getTexture(images[0]));
                    GL11.glPopMatrix();
                }
                GL11.glColor4d(1, 1, 1, 1);
                if(fading)return;
                GL11.glColor4d(1, .5, 0, opacity);
                GL11.glPushMatrix();
                GL11.glTranslated(x+width/2, y+height/2, 0);
                GL11.glRotated(rotation, 0, 0, 1);
                drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ImageStash.instance.getTexture(images[0]));
                GL11.glPopMatrix();
                GL11.glColor4d(1, 1, 1, 1);
                return;
            default:
                GL11.glColor4d(1, 1, 1, opacity);
                GL11.glPushMatrix();
                GL11.glTranslated(x+width/2, y+height/2, 0);
                GL11.glRotated(rotation, 0, 0, 1);
                drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ImageStash.instance.getTexture(images[0]));
                GL11.glPopMatrix();
                GL11.glColor4d(1, 1, 1, 1);
        }
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
            game.pushParticles(x+width/2, y+height/2, radius, (5+.5*((11-size)))*Math.min(1, opacity*5), PushCause.EXPLOSION);
            if(size>=10){
                for(Structure structure : game.structures){
                    if(Core.distance(structure, x, y)<=radius&&!structure.isBackgroundStructure()){
                        structure.onHit(structure.x-25, structure.y+structure.height-25);
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
            if(x>Core.helper.displayWidth()){
                dead = true;
            }
            x+=speed;
            for (Iterator<double[]> it = snow.iterator(); it.hasNext();) {
                double[] i = it.next();
                if(game.rand.nextBoolean()){
                    i[0]+=game.rand.nextInt(3)-1;
                }
                i[0]-=speed*.1;
                i[2]++;
                if(i[2]>=500){
                    it.remove();
                }
            }
            for(int i = 0; i<strength/100; i++){
                double X = game.rand.nextInt((int)width)-width/2;
                double Y = game.rand.nextInt((int)height)-height/2;
                snow.add(new double[]{X,Y,0});
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
                smoke.add(new double[]{0,0,0,0,0,0});
            }
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
    public void push(double x, double y, PushCause cause){
        if(type==ParticleEffectType.EXPLOSION)return;
        if(type==ParticleEffectType.SMOKE){
            if(x>1||y>1){
                x/=Math.max(x,y);
                y/=Math.max(x,y);
            }
        }
        if(type==ParticleEffectType.FIRE){
            if(cause==PushCause.ASTEROID)return;
            for(double[] d : smoke){
                d[1]+=x;
                d[2]+=y;
            }
            return;
        }
        if(type==ParticleEffectType.CLOUD){
            for(double[] d : snow){
                d[0]-=x;
                d[1]-=y;
            }
        }
        this.x+=x;
        this.y+=y;
    }
    public void fade(double amount){
        if(this instanceof ParticleFog){
            ((ParticleFog)this).opacity-=amount/10;
        }
        if(type==ParticleEffectType.CLOUD){
            strength-=amount;
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
    public static enum PushCause{
        ASTEROID,EXPLOSION,LASER, SHEILD_BLAST;
    }
}