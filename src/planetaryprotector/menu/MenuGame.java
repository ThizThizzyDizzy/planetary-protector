package planetaryprotector.menu;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Controls;
import planetaryprotector.Core;
import planetaryprotector.Sounds;
import planetaryprotector.building.Building.Upgrade;
import planetaryprotector.enemy.Asteroid;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.friendly.ShootingStar;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import planetaryprotector.game.Notification;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.menu.component.MenuComponentActionButton;
import planetaryprotector.menu.component.MenuComponentFalling;
import planetaryprotector.menu.component.MenuComponentRising;
import planetaryprotector.menu.ingame.MenuComponentOverlay;
import planetaryprotector.menu.ingame.MenuIngame;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuGame extends Menu{
    public ArrayList<MenuComponentActionButton> actionButtons = new ArrayList<>();
    private static final int actionButtonWidth = 375;
    private static final int actionButtonHeight = 60;
    public int actionButtonOffset = 0;
    public double debugYOffset = 0;
    public MenuComponentOverlay overlay;
    public Game game;
    private MenuComponentPhaseMarker phaseMarker;
    public MenuGame(GUI gui, Game game){
        super(gui, null);
        this.game = game;
    }
    @Override
    public void onGUIOpened(){
        game.onGUIOpened();
    }
    @Override
    public void renderBackground(){
        game.renderBackground();
    }
    @Override
    public void render(int millisSinceLastTick){
        game.render(millisSinceLastTick);
        super.render(millisSinceLastTick);
        //<editor-fold defaultstate="collapsed" desc="Updating Action Buttons">
        if(game.actionUpdateRequired==2){
            game.actionUpdateRequired = 0;
            components.removeAll(actionButtons);
            actionButtons.clear();
            actionButtonOffset = 20;
            for(Action action : game.getActions(this)){
                actionButtonOffset+=action.divider;
                if(!action.isDivider()){
                    actionButtons.add(add(new MenuComponentActionButton(this, game, 0, actionButtons.size()*actionButtonHeight+actionButtonOffset, actionButtonWidth, actionButtonHeight, action)));
                    actionButtonOffset+=action.divider;
                }
            }
        }
        if(game.actionUpdateRequired==1){
            for(MenuComponentActionButton button : actionButtons){
                button.update();
            }
        }
//</editor-fold>
        if(game.isPlayable()){
            drawRect(Display.getWidth()-100, Display.getHeight()-200, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/gui/sidebar.png"));
            if(game.selectedBuilding!=null){
                String upgrades = "";
                for(Upgrade u : game.selectedBuilding.getBoughtUpgrades())upgrades+="*";
                textWithBackground(0, 0, actionButtonWidth, 20, upgrades+" "+game.selectedBuilding.getName());
            }
            if(game.furnaceLevel<game.maxFurnaceLevel&&game.furnaceXP>=Math.pow(20, game.furnaceLevel+1)){
                GL11.glColor4d(0, Math.sin(game.tick/5d)/4+.75, 0, 1);
                Game.drawRegularPolygon(Display.getWidth()-100+5, Display.getHeight()-100+5, 5, 25, 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
            drawRect(Display.getWidth()-100, Display.getHeight()-100, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/furnace "+game.furnaceLevel+".png"));
            if(Mouse.getX()>=Display.getWidth()-100&&Mouse.getY()<=100&&game.furnaceLevel<game.maxFurnaceLevel){
                GL11.glColor4d(0, 1, 0, .25);
                double percent = game.furnaceXP/Math.pow(20, game.furnaceLevel+1);
                drawRect(Display.getWidth()-100,Display.getHeight()-100,Display.getWidth()-100+(100*percent), Display.getHeight(), 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
            GL11.glColor4d(0, 0, 0, 1);
            drawText(Display.getWidth()-90, Display.getHeight()-60, Display.getWidth()-10, Display.getHeight()-40, game.furnaceOre+" Ore");
            drawText(Display.getWidth()-90, Display.getHeight()-40, Display.getWidth()-10, Display.getHeight()-20, game.furnaceCoal+" Coal");
            drawText(Display.getWidth()-90, Display.getHeight()-20, Display.getWidth()-10, Display.getHeight(), game.furnaceLevel>=game.maxFurnaceLevel?"Maxed":"Level "+(game.furnaceLevel+1));
            GL11.glColor4d(1, 1, 1, 1);
            if(game.isPlayable()){
                for(int i = 0; i<game.resources.size(); i++){
                    int I = 1;
                    if(i==0)I = 0;
                    if(i==game.resources.size()-1)I = 2;
                    drawRect(Display.getWidth()-100, i*20, Display.getWidth(), (i+1)*20+(I==2?5:0), ImageStash.instance.getTexture("/textures/gui/sidebar "+I+".png"));
                GL11.glColor4d(0, 0, 0, 1);
                    drawText(Display.getWidth()-80, i*20, Display.getWidth(), (i+1)*20, game.resources.get(i).count+"");
                GL11.glColor4d(1, 1, 1, 1);
                    drawRect(Display.getWidth()-100, i*20, Display.getWidth()-80, (i+1)*20, game.resources.get(i).item.getTexture());
                }
            }
        }
        if(game.won){
            if(game.phase>0){
                centeredTextWithBackground(0, 0, Display.getWidth(), 35, "Congratulations! You have destroyed the alien mothership and saved the planet!");
                if(game.winTimer<20&&Sounds.nowPlaying().equals("VictoryMusic1")){
                    centeredTextWithBackground(0, 35, Display.getWidth(), 85, "Only one problem remains...");
                }
            }
        }else{
            int offset = 0;
            for(Iterator<Notification> it = game.notifications.iterator(); it.hasNext();){
                Notification n = it.next();
                double wide = FontManager.getLengthForStringWithHeight(n.toString(), 20);
                double left = Display.getWidth()/2-(wide/2*n.width);
                double right = Display.getWidth()/2+(wide/2*n.width);
                int y = 20-n.height;
                GL11.glColor4d(0,0,0,.5);
                drawRectWithBounds(Display.getWidth()/2-wide/2, offset-y/2, Display.getWidth()/2+wide/2, offset+20-y/2, left, offset, right, offset+n.height, 0);
                GL11.glColor4d(1,1,1,1);
                drawCenteredTextWithBounds(Display.getWidth()/2-wide/2, offset-y/2, Display.getWidth()/2+wide/2, offset+20-y/2, left, offset, right, offset+n.height, n.toString());
                offset+=n.height;
                if(n.isDead())it.remove();
            }
        }
        if(game.selectedBuilding!=null&&game.selectedBuilding.task!=null){
            for(int i = 0; i<game.selectedBuilding.task.getDetails().length; i++){
                textWithBackground(actionButtonWidth, 30*i, Display.getWidth(), 30*(i+1), game.selectedBuilding.task.getDetails()[i], game.selectedBuilding.task.important);
            }
        }
        if(game.paused){
            drawCenteredText(0, Display.getHeight()/2-50, Display.getWidth(), Display.getHeight()/2+50, "Paused");
        }
        if(Core.debugMode&&game.cheats){
            debugYOffset = 0;
            ArrayList<String> debugData = game.getDebugData();
            double textHeight = Display.getHeight()/debugData.size();
            for(String str : debugData){
                debugText(textHeight, str);
            }
        }
        if(phaseMarker!=null)phaseMarker.render(millisSinceLastTick);
        if(overlay!=null)overlay.render(millisSinceLastTick);
    }
    @Override
    public void renderForeground(){
        GL11.glColor4d(0, 0, 0, game.blackScreenOpacity);
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){
        if(overlay!=null){
            overlay.keyboardEvent(character, key, pressed, repeat);
            return;
        }
        super.keyboardEvent(character, key, pressed, repeat);
        if(!pressed)return;
        if(!repeat){
            switch(key){
                case Controls.hideSkyscrapers:
                    game.hideSkyscrapers = !game.hideSkyscrapers;
                    break;
                case Controls.showPowerNetworks:
                    game.showPowerNetworks = !game.showPowerNetworks;
                    break;
                case Controls.menu:
                    openOverlay(new MenuIngame(this));
                    game.paused = true;
                    break;
                case Controls.pause:
                    game.paused = !game.paused;
                    break;
                case Controls.mute:
                    Sounds.vol = 1-Sounds.vol;
                    if(Sounds.vol<.01){
                        game.notify("Sound ", "Off", 50);
                    }else{
                        game.notify("Sound ", "On", 50);
                    }
                    break;
                case Controls.cheat:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)&&Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&Keyboard.isKeyDown(Keyboard.KEY_LMENU)){
                        game.cheats = !game.cheats;
                    }
                    break;
            }
        }
        if(game.cheats){
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&Keyboard.isKeyDown(Keyboard.KEY_LMENU)&&Keyboard.isKeyDown(Controls.CHEAT_SECRET)){
                if(key>=Keyboard.KEY_1&&key<=Keyboard.KEY_EQUALS){
                    game.notify("Cheat: Secret #"+(key-1));
                    game.secretWaiting = key-2;
                }
            }
            if(!actionButtons.isEmpty()&&game.selectedBuilding!=null){
                if(key>=Keyboard.KEY_1&&key<=Keyboard.KEY_EQUALS){
                    if(actionButtons.size()>key-2){
                        actionButtons.get(key-2).perform();
                        if(game.selectedBuilding.task!=null){
                            game.notify("Cheat: Instant Completion");
                            game.selectedBuilding.task.progress = game.selectedBuilding.task.time-1;
                        }
                    }
                }
            }
            switch(key){
                case Controls.CHEAT_LOSE:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)&&Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&!repeat){
                        game.notify("Cheat: Losing Epilogue");
                        gui.open(new MenuLost(gui, game));
                    }
                    break;
                case Controls.CHEAT_DEBUG:
                    if(!repeat)Core.debugMode = !Core.debugMode;
                    break;
                case Controls.CHEAT_PHASE:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                        int oldPhase = game.phase;
                        game.phase(game.phase+1, false);
                        if(game.phase!=oldPhase){
                            game.notify("Cheat: Advance to phase "+game.phase);
                        }
                        game.paused = false;
                    }
                    break;
                case Controls.CHEAT_RESOURCES:
                    game.notify("Cheat: Resources");
                    for(ItemStack resource : game.resources){
                        resource.count+=100;
                    }
                    break;
                case Controls.CHEAT_CLOUD:
                    game.notify("Cheat: Cloud");
                    game.addCloud(Mouse.getX(), Display.getHeight()-Mouse.getY());
                    break;
                case Controls.CHEAT_FOG:
                    game.notify("Cheat: Fog");
                    game.startFog();
                    break;
                case Controls.CHEAT_WORKER:
                    game.notify("Cheat: Add Worker");
                    game.addWorker();
                    break;
                case Controls.CHEAT_ENEMY:
                    if(Keyboard.isKeyDown(Controls.CHEAT_SECRET)&&Keyboard.isKeyDown(Keyboard.KEY_1)){
                        game.notify("Cheat: Shooting Star");
                        int X = Mouse.getX()-25;
                        int Y = Display.getHeight()-Mouse.getY()-25;
                        game.addShootingStar(new ShootingStar(game, X, Y));
                    }else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                        Enemy.strength++;
                        game.notify("Cheat: Enemy Strength: ", Enemy.strength+"");
                    }else{
                        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                            game.notify("Cheat: Add Enemy");
                            game.addRandomEnemy();
                        }else{
                            game.notify("Cheat: Spawn Asteroid");
                            int X = Mouse.getX()-25;
                            int Y = Display.getHeight()-Mouse.getY()-25;
                            switch(game.rand.nextInt(3)){
                                case 0:
                                    game.addAsteroid(new Asteroid(game, X, Y, AsteroidMaterial.COAL, 1));
                                    break;
                                case 1:
                                    game.addAsteroid(new Asteroid(game, X, Y, AsteroidMaterial.STONE, 1));
                                    break;
                                case 2:
                                    game.addAsteroid(new Asteroid(game, X, Y, AsteroidMaterial.IRON, 1));
                                    break;
                            }
                        }
                    }
                    break;
                case Controls.CHEAT_PEACE:
                    game.notify("Cheat: Disable Meteor shower");
                    for(Enemy e : game.enemies){
                        if(e instanceof EnemyMothership){
                            game.notify("Cheat: Damage Mothership");
                            EnemyMothership m = (EnemyMothership) e;
                            m.health-=m.maxHealth/4;
                        }
                    }
                    game.meteorShower = false;
                    game.meteorShowerTimer += 20*60*60;
                    break;
            }
        }else if(pressed){
            if(!actionButtons.isEmpty()&&game.selectedBuilding!=null){
                if(key>=Keyboard.KEY_1&&key<=Keyboard.KEY_EQUALS){
                    if(actionButtons.size()>key-2){
                        if(actionButtons.get(key-2).enabled&&!actionButtons.get(key-2).action.isImportant())actionButtons.get(key-2).perform();
                    }
                }
            }
        }
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange){
        if(overlay!=null){
            overlay.mouseEvent(button, pressed, x, y, xChange, yChange, wheelChange);
            return;
        }
        if(phaseMarker!=null){
            phaseMarker.mouseEvent(button, pressed, x, y, xChange, yChange, wheelChange);
            return;
        }
        super.mouseEvent(button, pressed, x, y, xChange, yChange, wheelChange);
        for(MenuComponent c : components){
            if(c instanceof MenuComponentButton){
                if(Core.isPointWithinComponent(x, y, c))return;
            }
        }
        game.mouseEvent(button, pressed, x, y, xChange, yChange, wheelChange);
        if(button==0&&pressed&&game.isPlayable()){
            int amount = 1;
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))amount*=100;
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))amount*=10;
            if(isClickWithinBounds(x, y, Display.getWidth()-100, 20, Display.getWidth()-80, 40)){
                game.addCoalToFurnace(amount);
            }
            if(isClickWithinBounds(x, y, Display.getWidth()-100, 40, Display.getWidth()-80, 60)){
                game.addIronToFurnace(amount);
            }
            if(game.furnaceLevel<game.maxFurnaceLevel&&game.furnaceXP>=Math.pow(20, game.furnaceLevel+1)&&Mouse.getX()>=Display.getWidth()-100&&Mouse.getY()<=100){
                game.furnaceLevel++;
            }
        }
    }
    @Override
    public void tick(){
        if(overlay!=null)overlay.tick();
        if(game.updatePhaseMarker){
            game.paused = true;
            game.updatePhaseMarker = false;
            createPhaseMarker();
        }
        if(phaseMarker!=null){
            phaseMarker.tick();
            if(phaseMarker.opacity<0)phaseMarker = null;
        }
        super.tick();
        game.tick();
        if(game.addingIron>0){
            add(new MenuComponentFalling(this, Display.getWidth()-90+game.rand.nextInt(60), Display.getHeight()-180+game.rand.nextInt(50), Item.ironOre));
            game.addingIron--;
        }
        if(game.addingCoal>0){
            add(new MenuComponentFalling(this, Display.getWidth()-90+game.rand.nextInt(60), Display.getHeight()-180+game.rand.nextInt(50), Item.coal));
            game.addingCoal--;
        }
        if(game.smeltingIron>0){
            add(new MenuComponentRising(this, Display.getWidth()-90+game.rand.nextInt(60), Display.getHeight()-20+game.rand.nextInt(10), Item.ironIngot));
        }
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        for(MenuComponentActionButton actionButton : actionButtons){
            if(button==actionButton){
                actionButton.perform();
                game.actionUpdateRequired = 2;
            }
        }
    }
    public String debugText(double textHeight, String text){
        GL11.glColor4d(0, 0, 0, .5);
        drawRect(0, debugYOffset, FontManager.getLengthForStringWithHeight(text, textHeight-1)+1, debugYOffset+textHeight, 0);
        GL11.glColor4d(1, 1, 1, 1);
        text = drawTextWithWrap(1, debugYOffset+1, Display.getWidth()-1, debugYOffset+textHeight-1, text);
        debugYOffset+=textHeight;
        return text;
    }
    private void textWithBackground(double left, double top, double right, double bottom, String str){
        textWithBackground(left, top, right, bottom, str, false);
    }
    private void textWithBackground(double left, double top, double right, double bottom, String str, boolean pulsing){
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, simplelibrary.font.FontManager.getLengthForStringWithHeight(str, bottom-top)+left, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1); 
        if(pulsing){
            GL11.glColor4d(Math.sin(game.tick/5d)/4+.75, 0, 0, 1);
        }
        drawText(left,top,right,bottom, str);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public void centeredTextWithBackground(double left, double top, double right, double bottom, String str) {
        GL11.glColor4d(0, 0, 0, 0.75);
        drawRect(left, top, right, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(left,top,right,bottom, str);
    }
    private void createPhaseMarker(){
        components.remove(phaseMarker = add(new MenuComponentPhaseMarker(this)));
    }
    public void openOverlay(MenuComponentOverlay lay){
        if(overlay!=null)return;
        components.remove(overlay = add(lay));
    }
}