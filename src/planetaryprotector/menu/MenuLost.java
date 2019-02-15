package planetaryprotector.menu;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import simplelibrary.game.Framebuffer;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
public class MenuLost extends Menu{
    private int tick;
    private double[] laserFiring;
    private Framebuffer space;
    private boolean gottenFar = false;
    private static final double SPEED = 1.5;//planet break speed modifier
    private final MenuGame game;
    private double laserSize = 0;
    public MenuLost(GUI gui, MenuGame game){
        super(gui, game);
        this.game = game;
        Random rand = new Random();
        for(int i = 0; i<1000; i++){
            stars.add(new double[]{rand.nextInt(Display.getWidth()), rand.nextInt(Display.getHeight()), rand.nextInt(95*20), rand.nextDouble()/4+.2, 0});
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
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){
        if(key==Keyboard.KEY_ESCAPE){
            gui.open(new MenuMain(gui));
        }
    }
    @Override
    public void tick(){
        parent.tick();
        super.tick();
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
            game.pushParticles(laserFiring[0], laserFiring[1], Display.getWidth(), laserSize/50d);
        }
    }
    public ArrayList<double[]> stars = new ArrayList<>();
    @Override
    public void render(int millisSinceLastTick){
        GL11.glColor4d(0, 0, 0, 1);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
        if(tick<20*34){//on the planet
            if(gottenFar){
                gui.open(new MenuMain(gui));
                return;
            }
            super.render(millisSinceLastTick);
            if(tick<20*20){
                double mothershipY = -200+(300*tick/400d);
                double mothershipScale = ((1-(tick/400d))*5+1)*2;
                double mothershipOpacity = tick/200d;
                GL11.glColor4d(1, 1, 1, mothershipOpacity);
                GL11.glPushMatrix();
                GL11.glTranslated(Display.getWidth()/2, mothershipY, 0);
                GL11.glScaled(mothershipScale, mothershipScale, mothershipScale);
                drawRect(-125, -175/2, 2+125, 175/2, ImageStash.instance.getTexture("/textures/enemies/mothership 1.png"));
                GL11.glPopMatrix();
                GL11.glColor4d(1, 1, 1, 1);
            }else{
                GL11.glColor4d(1, 1, 1, 1);
                laserFiring = new double[]{Display.getWidth()/2,Display.getHeight()/2};
                if(laserFiring!=null){
                    int x = Display.getWidth()/2;
                    int y = 100;
                    laserSize = tick>20*33?50+(tick-20*33)*10:20+Math.sin(tick/40d)*5;
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
                drawRect(Display.getWidth()/2-250, 100-175, Display.getWidth()/2+250, 100+175, ImageStash.instance.getTexture("/textures/enemies/mothership 1.png"));
            }
        }else if(tick<20*50){//transition
            if(tick<20*35){//white
                GL11.glColor4d(1, 1, 1, 1);
                drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
            }else if(tick<20*45){//changing
                double x = (tick-20*35)/(20d*10);
                GL11.glColor4d(1-x, 1-x, 1-x, 1);
                drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
            }else{//black
                GL11.glColor4d(0, 0, 0, 1);
                drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
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
            drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        }else if(tick<20*(60+18)){
            drawSpace(1);
        }else if(tick<20*(60+22)){//fade to black
            drawSpace(1);
            double x = (tick-20*(60+18))/(20d*5);
            GL11.glColor4d(0, 0, 0, x);
            drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        }else{//fade to space
            drawStars();
            double x = (tick-20*(60+22))/(20d*5);
            GL11.glColor4d(0, 0, 0, 1-x);
            drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
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
                space = new Framebuffer(Core.helper, "Space"+tick, Display.getWidth(), Display.getHeight());
                space.bindRenderTarget2D();
            }
            double planetSize = Display.getHeight()*.65;
            GL11.glColor4d(0, 0, 0, 1);
            double left = Display.getWidth()/2-planetSize/2;
            double top = Display.getHeight()/2-planetSize/2;
            double right = Display.getWidth()/2+planetSize/2;
            double bottom = Display.getHeight()/2+planetSize/2;
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
            drawRect(0, 0, Display.getWidth(), Display.getHeight(), space.getTexture());
        }
    }
}