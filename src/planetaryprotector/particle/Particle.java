package planetaryprotector.particle;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.collision.AxisAlignedBoundingBox;
import com.thizthizzydizzy.dizzyengine.graphics.Material;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.graphics.batch.Instanceable;
import com.thizthizzydizzy.dizzyengine.graphics.image.Color;
import com.thizthizzydizzy.dizzyengine.graphics.mesh.Mesh;
import com.thizthizzydizzy.dizzyengine.graphics.mesh.builder.AxialQuadMeshBuilder;
import com.thizthizzydizzy.dizzyengine.world.object.SizedWorldObject;
import com.thizthizzydizzy.dizzyengine.world.object.WorldObject;
import java.util.ArrayList;
import java.util.Iterator;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Options;
import planetaryprotector.game.Game;
import planetaryprotector.structure.Structure;
public class Particle extends SizedWorldObject implements Instanceable{
    public final ParticleEffectType type;
    protected float rotation;
    public float opacity = 1;
    protected int radius;
    private final int size;
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
        setPosition(new Vector3f(x, y, 25));
        setSize(new Vector3f(50));
        setMaterial(new Material(null, ResourceManager.getTexture(type.images[0])));
        this.type = type;
        this.size = size;
        if(type==ParticleEffectType.CLOUD){
            rotation = game.rand.nextInt(360);
            strength = game.rand.nextInt(25);
            rateOfChange = (game.rand.nextDouble()-.25)/100;
        }
    }
    @Deprecated
    public Particle(Game game, int x, int y, ParticleEffectType type, int size, boolean air){
        this(game, x, y, type, size);
    }
    public Particle(Game game, int x, int y, float cloudStrength, double cloudRateOfChange, double speed){
        this(game, x, y, ParticleEffectType.CLOUD);
        strength = cloudStrength;
        rateOfChange = cloudRateOfChange;
        this.speed = speed;
    }
//    @Override
//    public void draw(){
//        switch(type){
//            case CLOUD:
//                if(strength>lightningThreshold){
//                    //add lightning
//                    if(lightning==null&&game.rand.nextDouble()*strength>lightningThreshold&&game.rand.nextDouble()<lightningChance){
//                        lightning = new Lightning();
//                        float lastX = 0;
//                        float lastY = 0;
//                        float Y = 0;
//                        float X = 0;
//                        while(Y<400){
//                            Y = lastY+game.rand.nextInt(40)+10;
//                            X = (float)(lastX+(game.rand.nextDouble()-.5)*30);
//                            lightning.addLine(x+width/2+lastX, y+height/2+lastY, x+width/2+X, y+height/2+Y);
//                            if(game.rand.nextBoolean()){
//                                float YY = lastY+game.rand.nextInt(40)+10;
//                                float XX = (float)(lastX+(game.rand.nextDouble()-.5)*30);
//                                lightning.addBranch(x+width/2+lastX, y+height/2+lastY, x+width/2+XX, y+height/2+YY);
//                            }
//                            lastX = X;
//                            lastY = Y;
//                        }
//                        Renderer.fillRect(x+width/2+X, y+height/2+Y, x+width/2+X+1, y+height/2+Y+1);
//                    }
//                }
//                if(strength>rainThreshold){
//                    //draw rain
//                    if(Game.theme==Game.Theme.SNOWY){
//                        Renderer.setColor(1, 1, 1, .875f*Options.options.cloudIntensity);
//                        for(float[] i : snow){
//                            Renderer.fillRect(x+i[0]+width/2, y+i[1]+i[2]+height/2, x+i[0]+width/2+1, y+i[1]+i[2]+height/2+1);
//                        }
//                    }else{
//                        Renderer.setColor(0, 0, 1, .75f*Options.options.cloudIntensity);
//                        for(int i = 0; i<strength; i++){
//                            float X = game.rand.nextInt((int)width)-width/2;
//                            float Y = game.rand.nextInt((int)(height+500))-height/2;
//                            Renderer.fillRect(x+width/2+X, y+height/2+Y, x+width/2+X+1, y+height/2+Y+5, 0);
//                        }
//                    }
//                }
//                if(lightning!=null){
//                    //draw lightning
//                    lightning.render();
//                    if(lightning.dead){
//                        lightning = null;
//                    }
//                }
//                opacity = (float)(Math.log10(strength/(rainThreshold/4))*.75f);
//                float lightness = 1-((strength-(rainThreshold*(3/4f)))/50);
//                if(Game.theme==Game.Theme.SNOWY)
//                    lightness = (float)Math.sqrt(lightness);
//                Renderer.setColor(lightness, lightness, lightness, opacity*Options.options.cloudIntensity);
//                Renderer.pushModel(new Matrix4f().translate(x+width/2, y+height/2, 0).rotate((float)Math.toRadians(rotation), 0, 0, 1));
//                Renderer.fillRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ResourceManager.getTexture(images[0]));
//                Renderer.popModel();
//                Renderer.setColor(1, 1, 1, 1);
//                return;
//            case FIRE:
//                for(float[] i : smoke){
//                    float r = Math.max(0, Math.min(1, (10-i[4])/10));
//                    float g = Math.max(0, Math.min(1, (10-i[4])/20));
//                    Renderer.setColor(r, g, 0, i[3]);
//                    Renderer.pushModel(new Matrix4f().translate(x+i[0]+width/2, y+i[1]-i[5]+height/2, 0).rotate((float)Math.toRadians(i[2]), 0, 0, 1));
//                    Renderer.fillRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ResourceManager.getTexture(images[0]));
//                    Renderer.popModel();
//                }
//                Renderer.setColor(1, 1, 1, 1);
//                if(fading)return;
//                Renderer.setColor(1, .5f, 0, opacity);
//                Renderer.pushModel(new Matrix4f().translate(x+width/2, y+height/2, 0).rotate((float)Math.toRadians(rotation), 0, 0, 1));
//                Renderer.fillRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ResourceManager.getTexture(images[0]));
//                Renderer.popModel();
//                Renderer.setColor(1, 1, 1, 1);
//                return;
//        }
//    }
    private int smokeTimer;
    private ArrayList<float[]> smoke = new ArrayList<>();
    public void tick(){
        if(opacity<=0){
            remove();
            return;
        }
        var game = DizzyEngine.getLayer(Game.class);
        if(type==ParticleEffectType.EXPLOSION){
            //smoke
            for(int j = 0; j<5; j++){
                int radius = this.radius+5*j;
                int c = 90*(Options.options.particles+1);
                for(int i = 0; i<c; i++){
                    float X = (float)(Math.cos(Math.toRadians(i*(360d/c)))*radius+getX());
                    float Y = (float)(Math.sin(Math.toRadians(i*(360d/c)))*radius+getY());
                    game.addParticleEffect(new Particle(game, (int)X, (int)Y, ParticleEffectType.SMOKE));
                }
            }
//            //explosion
//            for(int j = 0; j<10; j++){
//                float radius = this.radius/10f+2*j;
//                int c = 22*(Options.options.particles+1);
//                for(int i = 0; i<45; i++){
//                    float X = (float)(Math.cos(Math.toRadians(i*(360d/c)))*radius+x-width/2);
//                    float Y = (float)(Math.sin(Math.toRadians(i*(360d/c)))*radius+y-height/2);
//                    Renderer.setColor(1, opacity, 0, opacity);
//                    Renderer.fillRect(X, Y, X+width, Y+height, ResourceManager.getTexture(images[0]));
//                    Renderer.setColor(1, 1, 1, 1);
//                }
//            }
//            return;

            opacity -= 0.005*(11-size);
            radius += 5+0.5*((11-size));
            game.pushParticles((int)getPosition().x, (int)getPosition().y, radius, (5+.5*((11-size)))*Math.min(1, opacity*5), PushCause.EXPLOSION);
            if(size>=10){
                for(Structure structure : game.structures){
                    if(structure.getPosition().distance(getPosition())<=radius&&!structure.type.isBackgroundStructure()){
                        structure.onHit(-10000, -10000); // make the damage out of view
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
            if(getX()>game.getCityBoundingBox().max.x+game.getXGamePadding()){
                remove();
            }
            setPosition(getPosition().add((float)speed, 0, 0));
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
                float X = game.rand.nextInt(50)-25;
                float Y = game.rand.nextInt(50)-25;
                snow.add(new float[]{X, Y, 0});
            }
        }
        if(type==ParticleEffectType.FIRE){
            smokeTimer++;
            if(fading&&smoke.isEmpty())remove();
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
        setPosition(getPosition().add((float)x, (float)y, 0));
    }
    public void fade(double amount){
        if(this instanceof ParticleFog){
            ((ParticleFog)this).opacity -= amount/10;
        }
        if(type==ParticleEffectType.CLOUD){
            strength -= amount;
        }
    }
    @Override
    public AxisAlignedBoundingBox getAxisAlignedBoundingBox(){
        AxisAlignedBoundingBox bbox = new AxisAlignedBoundingBox();
        bbox.min.set(getSize()).mul(-.5f).add(getPosition());
        bbox.max.set(getSize()).mul(.5f).add(getPosition());
        return bbox;
    }
    @Override
    public boolean canInstance(WorldObject other){
        return other instanceof Particle p&&p.type==type&&p.opacity==opacity;
    }
    private Mesh mesh;
    @Override
    public Mesh getMesh(){
        if(mesh==null)mesh = generateMesh();
        return mesh;
    }
    protected Mesh generateMesh(){
        var builder = new AxialQuadMeshBuilder();
        builder.quadXY(-getSize().x/2, -getSize().y/2, getSize().x/2, getSize().y/2, 0, true, 0, 0, 1, 1);
        return builder.build();
    }
    @Override
    public void preRender(){
        GL11.glDepthMask(false);
        switch(type){
            case SMOKE -> Renderer.setColor(Color.BLACK, opacity);
            case CLOUD -> {
                opacity = (float)(Math.log10(strength/(rainThreshold/4))*.75f);
                float lightness = 1-((strength-(rainThreshold*(3/4f)))/50);
                if(Game.theme==Game.Theme.SNOWY)
                    lightness = (float)Math.sqrt(lightness);
                Renderer.setColor(lightness, lightness, lightness, opacity*Options.options.cloudIntensity);
            }
            default -> Renderer.setColor(Color.WHITE, opacity);
        }
    }
    @Override
    public void postRender(){
        GL11.glDepthMask(true);
    }
    @Override
    public Matrix4f getModelMatrix(){
        return super.getModelMatrix().rotate((float)Math.toRadians(rotation), 0, 0, 1);
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
        return getPosition().x;
    }
    /**
     * @return the CENTER of the particle
     */
    public double getY(){
        return getPosition().y;
    }
    public static enum PushCause{
        ASTEROID, EXPLOSION, LASER, SHEILD_BLAST;
    }
}
