package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.ui.Menu;
import planetaryprotector.game.Game;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
public class MenuLost extends Menu{
    private int tick;
    private int[] laserFiring;
    private Framebuffer space;
    private boolean gottenFar = false;
    private static final double SPEED = 1.5;//planet break speed modifier
    private final Game game;
    private double laserSize = 0;
    private boolean laserBig = false;
    private boolean explosion = false;
    public MenuLost(Game game){
        this.game = game;
        Random rand = new Random();
        for(int i = 0; i<1000; i++){
            stars.add(new double[]{rand.nextInt(Core.helper.displayWidth()), rand.nextInt(Core.helper.displayHeight()), rand.nextInt(105*20), rand.nextDouble()/4+.2, 0});
        }
    }
    @Override
    public void onGUIOpened(){
        Sounds.playSound("music", "SadMusic3");
    }
    @Override
    public void renderBackground(){
        game.renderWorld(0);
    }
    @Override
    public void keyEvent(int key, int scancode, boolean isPress, boolean isRepeat, int modifiers){
        if(key==GLFW.GLFW_KEY_ESCAPE){
            gui.open(new MenuMain(gui, true));
        }
    }
    @Override
    public void tick(){
        game.tick();
        super.tick();
        Core.discordDetails = "Game Over";
        Core.discordEndTimestamp = (System.currentTimeMillis()+206000-(Sounds.songTimer()*50))/1000;
        Core.discordLargeImageKey = "planet";
        Core.discordLargeImageText = "Epilogue";
        Core.discordState = "Epilogue";
        tick = Sounds.songTimer();
        if(tick>=20*(60+22)){
            for(double[] star : stars){
                if(tick-(20*(60+22))>star[2]){
                    star[4]+=0.00625;
                    star[3] = 1-star[4];
                }
            }
        }
        if(laserFiring!=null){
            if(laserBig){
                if(!explosion){
                    explosion = true;
                    game.addParticleEffect(new Particle(game, laserFiring[0], laserFiring[1], ParticleEffectType.EXPLOSION, 10, false){
                        @Override
                        public void tick(){
                            radius+=80;
                            super.tick();
                        }
                    });
                }
                game.pushParticles(laserFiring[0], laserFiring[1], Core.helper.displayWidth(), 50, 2, Particle.PushCause.EXPLOSION);
            }else{
                game.pushParticles(laserFiring[0], laserFiring[1], Core.helper.displayWidth()/8, 4, .2, Particle.PushCause.EXPLOSION);
            }
        }
    }
    /**
     * x,y,time,alpha
     */
    public ArrayList<double[]> stars = new ArrayList<>();
    @Override//sun appear, slowly move into distance until it dies at around 03:15.75
    public void render(int millisSinceLastTick){
        GL11.glColor4d(0, 0, 0, 1);
        drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
        if(tick<20*34){//on the planet
            //<editor-fold defaultstate="collapsed" desc="Planet surface">
            if(gottenFar){
                gui.open(new MenuMain(gui, true));
                return;
            }
            super.render(millisSinceLastTick);
            if(tick<20*20){
                double mothershipY = -200+(300*tick/400d);
                double mothershipScale = ((1-(tick/400d))*5+1)*2;
                double mothershipOpacity = tick/200d;
                GL11.glColor4d(1, 1, 1, mothershipOpacity);
                GL11.glPushMatrix();
                GL11.glTranslated(Core.helper.displayWidth()/2, mothershipY, 0);
                GL11.glScaled(mothershipScale, mothershipScale, mothershipScale);
                drawRect(-125, -125, 125, 125, ImageStash.instance.getTexture("/textures/enemies/mothership 1.png"));
                GL11.glPopMatrix();
                GL11.glColor4d(1, 1, 1, 1);
            }else{
                GL11.glColor4d(1, 1, 1, 1);
                laserFiring = new int[]{0, 0};
                if(laserFiring!=null){
                    int x = Core.helper.displayWidth()/2;
                    int y = 100;
                    if(tick>20*33)laserBig = true;
                    laserSize = laserBig?50+(tick-20*33)*10:20+Math.sin(tick/40d)*5;
                    double xDiff = laserFiring[0]-x;
                    double yDiff = laserFiring[1]-y;
                    double dist = Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
                    for(int i = 0; i<dist; i++){
                        double percent = i/dist;
                        GL11.glColor4d(1, 0, 0, 1);
                        Game.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), laserSize/2D,10,0);
                    }
                    for(int i = 0; i<dist; i++){
                        double percent = i/dist;
                        GL11.glColor4d(1, .5, 0, 1);
                        Game.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), (laserSize*(2/3D))/2D,10,0);
                    }
                    for(int i = 0; i<dist; i++){
                        double percent = i/dist;
                        GL11.glColor4d(1, 1, 0, 1);
                        Game.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), (laserSize*(1/3D))/2D,10,0);
                        GL11.glColor4d(1, 1, 1, 1);
                    }
                }
                drawRect(Core.helper.displayWidth()/2-250, 100-250, Core.helper.displayWidth()/2+250, 100+250, ImageStash.instance.getTexture("/textures/enemies/mothership 1.png"));
            }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Transitions and planet exploding">
        }else if(tick<20*50){//transition
            if(tick<20*35){//white
                GL11.glColor4d(1, 1, 1, 1);
                drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), 0);
            }else if(tick<20*45){//changing
                double x = (tick-20*35)/(20d*10);
                GL11.glColor4d(1-x, 1-x, 1-x, 1);
                drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), 0);
            }else{//black
                GL11.glColor4d(0, 0, 0, 1);
                drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), 0);
            }
        }else if(tick<20*55){//space image and planet fades in
            double x = (tick-20*50)/(20d*5);
            GL11.glColor4d(1, 1, 1, x);
            drawSpace(0);
        }else if(tick<20*60){//planet explodes
            drawSpace(0);
            gottenFar = true;
        }else if(tick<20*65){
            drawSpace(1);
            double x = (tick-20*60)/(20d*5);
            GL11.glColor4d(1, 1, 1, 1-x);
            drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), 0);
        }else if(tick<20*(60+18)){
            drawSpace(1);
        }else if(tick<20*(60+22)){//fade to black
            drawSpace(1);
            double x = (tick-20*(60+18))/(20d*5);
            GL11.glColor4d(0, 0, 0, x);
            drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), 0);
//</editor-fold>
        }else{//fade to space, sun activity in space
            drawStars();
            //<editor-fold defaultstate="collapsed" desc="(Disabled) Sun on screen, moving away then explodes-- I don't think this fits in, but I don't want to delete it">
//            int startTick = 20*(60+22);
//            int boomTick = 20*(60*3+15);
//            double percent = Math.min(1,(tick-startTick)/(double)(boomTick-startTick));
//            int sizeMin = 3;
//            int sizeMax = Core.helper.displayHeight()/20;
//            int sizeDiff = sizeMax-sizeMin;
//            double size = (1-percent)*sizeDiff+sizeMin;
//            double opacity = tick<boomTick?1:1-((tick-boomTick)/(20*5d));//5 seconds to fade
//            double boomRadius = (1-opacity)*Core.helper.displayHeight()/2;
//            int I = 25;
//            for(int i = 0; i<=I; i++){
//                double radius = size/2+i*(1-percent);
//                double instOpacity = (I-i)/(double)I;
//                GL11.glColor4d(1, 1, .75, opacity*instOpacity);
//                MenuGame.drawRegularPolygon(Core.helper.displayWidth()/2, Core.helper.displayHeight()/2, radius, 100, 0);
//            }
//            if(tick>boomTick){
//                //smoke
//                for(int j = 0; j<5; j++){
//                    double radius = boomRadius+2*j;
//                    int c = 180;
//                    for(int i = 0; i<c; i++){
//                        double X = Math.cos(Math.toRadians(i*(360d/c)))*radius+Core.helper.displayWidth()/2;
//                        double Y = Math.sin(Math.toRadians(i*(360d/c)))*radius+Core.helper.displayHeight()/2;
//                        GL11.glColor4d(1, 1, .9, opacity);
//                        drawRect(X-5, Y-5, X+5, Y+5, ParticleEffectType.SMOKE.images[0]);
//                        GL11.glColor4d(1, 1, 1, 1);
//                    }
//                }
//                //explosion
//                for(int j = 0; j<10; j++){
//                    double radius = boomRadius/10D+j;
//                    int c = 45;
//                    for(int i = 0; i<c; i++){
//                        double X = Math.cos(Math.toRadians(i*(360d/c)))*radius+Core.helper.displayWidth()/2;
//                        double Y = Math.sin(Math.toRadians(i*(360d/c)))*radius+Core.helper.displayHeight()/2;
//                        GL11.glColor4d(1, 1, .9, opacity);
//                        drawRect(X-5, Y-5, X+5, Y+5, ParticleEffectType.EXPLOSION.images[0]);
//                        GL11.glColor4d(1, 1, 1, 1);
//                    }
//                }
//            }
//</editor-fold>
            double x = (tick-20*(60+22))/(20d*5);
            GL11.glColor4d(0, 0, 0, 1-x);
            drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), 0);
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    private void drawStars(){
        GL11.glBegin(GL11.GL_POINTS);
        for(double[] star : stars){
            GL11.glColor4d(1, 1, 1, star[3]);
            GL11.glVertex2d(star[0], star[1]);
        }
        GL11.glEnd();
    }
    public double[][] planetParts = new double[3][2];
    private void drawSpace(int boom){//0 for no, 1 for yes
        if(space==null||boom>0){
            boolean d = false;
            if(space==null){
                d = true;
                space = new Framebuffer(Core.helper, "Space"+tick, Core.helper.displayWidth(), Core.helper.displayHeight());
                space.bindRenderTarget2D();
            }
            double planetSize = Core.helper.displayHeight()*.65;
            GL11.glColor4d(0, 0, 0, 1);
            double left = Core.helper.displayWidth()/2-planetSize/2;
            double top = Core.helper.displayHeight()/2-planetSize/2;
            double right = Core.helper.displayWidth()/2+planetSize/2;
            double bottom = Core.helper.displayHeight()/2+planetSize/2;
            drawStars();
            GL11.glColor4d(1, 1, 1, 1);
            if(boom==1){
                int j = 0;
                for(double[] is : planetParts){
                    j++;
                    drawRect(left+is[0], top+is[1], right+is[0], bottom+is[1], ImageStash.instance.getTexture("/textures/planet"+j+".png"));
                    switch(j){
                        case 1:
                            is[0]-=0.05*SPEED;
                            is[1]-=0.075*SPEED;
                            break;
                        case 2:
                            is[0]+=0.1*SPEED;
                            break;
                        case 3:
                            is[0]-=0.055*SPEED;
                            is[1]+=0.07*SPEED;
                            break;
                    }
                }
            }else{
                drawRect(left, top, right, bottom, ImageStash.instance.getTexture("/textures/planet.png"));
            }
            if(d){
                space.releaseRenderTarget();
            }
        }else{
            drawRect(0, 0, Core.helper.displayWidth(), Core.helper.displayHeight(), space.getTexture());
        }
    }
}