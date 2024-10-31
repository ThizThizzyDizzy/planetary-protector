package planetaryprotector.menu;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.Framebuffer;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
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
    private float laserSize = 0;
    private boolean laserBig = false;
    private boolean explosion = false;
    public MenuLost(Game game){
        this.game = game;
        Random rand = new Random();
        for(int i = 0; i<1000; i++){
            stars.add(new float[]{rand.nextInt(DizzyEngine.screenSize.x), rand.nextInt(DizzyEngine.screenSize.y), rand.nextInt(105*20), (float)(rand.nextDouble()/4+.2), 0});
        }
    }
    @Override
    public void onMenuOpened(){
        super.onMenuOpened();
        Sounds.playMusic("SadMusic3");
    }
    @Override
    public void onKey(int id, int key, int scancode, int action, int mods){
        super.onKey(id, key, scancode, action, mods);
        if(key==GLFW.GLFW_KEY_ESCAPE){
            new MenuMain(true).open();
        }
    }
    /**
     * x,y,time,alpha
     */
    public ArrayList<float[]> stars = new ArrayList<>();
    @Override//sun appear, slowly move into distance until it dies at around 03:15.75
    public void render(double deltaTime){
        Core.discordDetails = "Game Over";
        Core.discordEndTimestamp = (System.currentTimeMillis()+206000-(Sounds.songTimer()*50))/1000;
        Core.discordLargeImageKey = "planet";
        Core.discordLargeImageText = "Epilogue";
        Core.discordState = "Epilogue";
        tick = Sounds.songTimer();
        if(tick>=20*(60+22)){
            for(float[] star : stars){
                if(tick-(20*(60+22))>star[2]){
                    star[4] += 0.00625;
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
                            radius += 80;
                            super.tick();
                        }
                    });
                }
                game.pushParticles(laserFiring[0], laserFiring[1], DizzyEngine.screenSize.x, 50, 2, Particle.PushCause.EXPLOSION);
            }else{
                game.pushParticles(laserFiring[0], laserFiring[1], DizzyEngine.screenSize.x/8, 4, .2, Particle.PushCause.EXPLOSION);
            }
        }
        Renderer.setColor(0, 0, 0, 1);
        Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, 0);
        Renderer.setColor(1, 1, 1, 1);
        if(tick<20*34){//on the planet
            //<editor-fold defaultstate="collapsed" desc="Planet surface">
            if(gottenFar){
                new MenuMain(true).open();
                return;
            }
            super.render(deltaTime);
            if(tick<20*20){
                float mothershipY = -200+(300*tick/400f);
                float mothershipScale = ((1-(tick/400f))*5+1)*2;
                float mothershipOpacity = tick/200f;
                Renderer.setColor(1, 1, 1, mothershipOpacity);
                GL11.glPushMatrix();
                GL11.glTranslated(DizzyEngine.screenSize.x/2, mothershipY, 0);
                GL11.glScaled(mothershipScale, mothershipScale, mothershipScale);
                Renderer.fillRect(-125, -125, 125, 125, ResourceManager.getTexture("/textures/enemies/mothership 1.png"));
                GL11.glPopMatrix();
                Renderer.setColor(1, 1, 1, 1);
            }else{
                Renderer.setColor(1, 1, 1, 1);
                laserFiring = new int[]{0, 0};
                if(laserFiring!=null){
                    int x = DizzyEngine.screenSize.x/2;
                    int y = 100;
                    if(tick>20*33)laserBig = true;
                    laserSize = (float)(laserBig?50+(tick-20*33)*10:20+Math.sin(tick/40f)*5);
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
                Renderer.fillRect(DizzyEngine.screenSize.x/2-250, 100-250, DizzyEngine.screenSize.x/2+250, 100+250, ResourceManager.getTexture("/textures/enemies/mothership 1.png"));
            }
//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Transitions and planet exploding">
        }else if(tick<20*50){//transition
            if(tick<20*35){//white
                Renderer.setColor(1, 1, 1, 1);
                Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, 0);
            }else if(tick<20*45){//changing
                float x = (tick-20*35)/(20f*10);
                Renderer.setColor(1-x, 1-x, 1-x, 1);
                Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, 0);
            }else{//black
                Renderer.setColor(0, 0, 0, 1);
                Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, 0);
            }
        }else if(tick<20*55){//space image and planet fades in
            float x = (tick-20*50)/(20f*5);
            Renderer.setColor(1, 1, 1, x);
            drawSpace(0);
        }else if(tick<20*60){//planet explodes
            drawSpace(0);
            gottenFar = true;
        }else if(tick<20*65){
            drawSpace(1);
            float x = (tick-20*60)/(20f*5);
            Renderer.setColor(1, 1, 1, 1-x);
            Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, 0);
        }else if(tick<20*(60+18)){
            drawSpace(1);
        }else if(tick<20*(60+22)){//fade to black
            drawSpace(1);
            float x = (tick-20*(60+18))/(20f*5);
            Renderer.setColor(0, 0, 0, x);
            Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, 0);
//</editor-fold>
        }else{//fade to space, sun activity in space
            drawStars();
            //<editor-fold defaultstate="collapsed" desc="(Disabled) Sun on screen, moving away then explodes-- I don't think this fits in, but I don't want to delete it">
//            int startTick = 20*(60+22);
//            int boomTick = 20*(60*3+15);
//            double percent = Math.min(1,(tick-startTick)/(double)(boomTick-startTick));
//            int sizeMin = 3;
//            int sizeMax = DizzyEngine.screenSize.y/20;
//            int sizeDiff = sizeMax-sizeMin;
//            double size = (1-percent)*sizeDiff+sizeMin;
//            double opacity = tick<boomTick?1:1-((tick-boomTick)/(20*5d));//5 seconds to fade
//            double boomRadius = (1-opacity)*DizzyEngine.screenSize.y/2;
//            int I = 25;
//            for(int i = 0; i<=I; i++){
//                double radius = size/2+i*(1-percent);
//                double instOpacity = (I-i)/(double)I;
//                Renderer.setColor(1, 1, .75, opacity*instOpacity);
//                MenuRenderer.fillRegularPolygon(DizzyEngine.screenSize.x/2, DizzyEngine.screenSize.y/2, radius, 100, 0);
//            }
//            if(tick>boomTick){
//                //smoke
//                for(int j = 0; j<5; j++){
//                    double radius = boomRadius+2*j;
//                    int c = 180;
//                    for(int i = 0; i<c; i++){
//                        double X = Math.cos(Math.toRadians(i*(360d/c)))*radius+DizzyEngine.screenSize.x/2;
//                        double Y = Math.sin(Math.toRadians(i*(360d/c)))*radius+DizzyEngine.screenSize.y/2;
//                        Renderer.setColor(1, 1, .9, opacity);
//                        Renderer.fillRect(X-5, Y-5, X+5, Y+5, ParticleEffectType.SMOKE.images[0]);
//                        Renderer.setColor(1, 1, 1, 1);
//                    }
//                }
//                //explosion
//                for(int j = 0; j<10; j++){
//                    double radius = boomRadius/10D+j;
//                    int c = 45;
//                    for(int i = 0; i<c; i++){
//                        double X = Math.cos(Math.toRadians(i*(360d/c)))*radius+DizzyEngine.screenSize.x/2;
//                        double Y = Math.sin(Math.toRadians(i*(360d/c)))*radius+DizzyEngine.screenSize.y/2;
//                        Renderer.setColor(1, 1, .9, opacity);
//                        Renderer.fillRect(X-5, Y-5, X+5, Y+5, ParticleEffectType.EXPLOSION.images[0]);
//                        Renderer.setColor(1, 1, 1, 1);
//                    }
//                }
//            }
//</editor-fold>
            float x = (tick-20*(60+22))/(20f*5);
            Renderer.setColor(0, 0, 0, 1-x);
            Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, 0);
        }
        Renderer.setColor(1, 1, 1, 1);
    }
    private void drawStars(){
        for(float[] star : stars){
            Renderer.setColor(1, 1, 1, star[3]);
            Renderer.fillRect(star[0], star[1], star[0]+1, star[1]+1, 0);
        }
    }
    public float[][] planetParts = new float[3][2];
    private void drawSpace(int boom){//0 for no, 1 for yes
        if(space==null||boom>0){
            boolean d = false;
            if(space==null){
                d = true;
                space = new Framebuffer(DizzyEngine.screenSize.x, DizzyEngine.screenSize.y);
                space.bind();
            }
            float planetSize = DizzyEngine.screenSize.y*.65f;
            Renderer.setColor(0, 0, 0, 1);
            float left = DizzyEngine.screenSize.x/2-planetSize/2;
            float top = DizzyEngine.screenSize.y/2-planetSize/2;
            float right = DizzyEngine.screenSize.x/2+planetSize/2;
            float bottom = DizzyEngine.screenSize.y/2+planetSize/2;
            drawStars();
            Renderer.setColor(1, 1, 1, 1);
            if(boom==1){
                int j = 0;
                for(float[] is : planetParts){
                    j++;
                    Renderer.fillRect(left+is[0], top+is[1], right+is[0], bottom+is[1], ResourceManager.getTexture("/textures/planet"+j+".png"));
                    switch(j){
                        case 1:
                            is[0] -= 0.05*SPEED;
                            is[1] -= 0.075*SPEED;
                            break;
                        case 2:
                            is[0] += 0.1*SPEED;
                            break;
                        case 3:
                            is[0] -= 0.055*SPEED;
                            is[1] += 0.07*SPEED;
                            break;
                    }
                }
            }else{
                Renderer.fillRect(left, top, right, bottom, ResourceManager.getTexture("/textures/planet.png"));
            }
            if(d){
                //TODO relase framebuffer?
            }
        }else{
            Renderer.fillRect(0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y, space.texture);
        }
    }
}
