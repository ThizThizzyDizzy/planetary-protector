package planetaryprotector.menu.ingame;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Controls;
import planetaryprotector.Core;
import planetaryprotector.structure.PowerStorage;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.component.MenuComponentCheckbox;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentSlider;
public class MenuPowerStorageConfiguration extends MenuComponentOverlayStructure{
    private static final int spacing = 3;
    private static final int textHeight = 40;
    private static final int dividerHeight = textHeight/2;
    private double Y = 20;
    private final PowerStorage powerStorage;
    private final MenuComponentSlider daylightThreshold;
    private final MenuComponentCheckbox automaticControl;
    private final MenuComponentCheckbox daylightControl;
    private final MenuComponentCheckbox daylightRecharge;
    private final MenuComponentCheckbox daylightDischarge;
    private final MenuComponentCheckbox nightRecharge;
    private final MenuComponentCheckbox nightDischarge;
    private final MenuComponentCheckbox neutralRecharge;
    private final MenuComponentCheckbox neutralDischarge;
    private final MenuComponentCheckbox meteorOverride;
    private final MenuComponentCheckbox meteorRecharge;
    private final MenuComponentCheckbox meteorDischarge;
    private final MenuComponentSlider rechargeRate;
    private final MenuComponentSlider dischargeRate;
    public MenuPowerStorageConfiguration(MenuGame menu, PowerStorage powerStorage){
        super(menu, powerStorage);
        automaticControl = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.automaticControl));
        daylightThreshold = add(new MenuComponentSlider(0, 0, 50*textHeight/8, textHeight, 0, 100, powerStorage.daylightThreshold, true));
        daylightControl = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.daylightControl));
        daylightRecharge = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.daylightRecharge));
        daylightDischarge = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.daylightDischarge));
        nightRecharge = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.nightRecharge));
        nightDischarge = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.nightDischarge));
        neutralRecharge = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.neutralRecharge));
        neutralDischarge = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.neutralDischarge));
        meteorOverride = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.meteorOverride));
        meteorRecharge = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.meteorRecharge));
        meteorDischarge = add(new MenuComponentCheckbox(0, 0, textHeight, textHeight, true, powerStorage.meteorDischarge));
        rechargeRate = add(new MenuComponentSlider(0, 0, 50*textHeight/8, textHeight, 0, 100, powerStorage.rechargeRate, true));
        dischargeRate = add(new MenuComponentSlider(0, 0, 50*textHeight/8, textHeight, 0, 100, powerStorage.dischargeRate, true));
        this.powerStorage = powerStorage;
    }
    @Override
    public void renderBackground(){
        super.renderBackground();
    }
    @Override
    public void render(){
        GL11.glColor4d(1, 1, 1, 1);
        Y = 20;
        for(MenuComponent c : components){
            c.y = -c.height;
        }
        powerStorage.automaticControl = automaticControl.isChecked;
        powerStorage.daylightThreshold = (int) daylightThreshold.getValue();
        powerStorage.daylightControl = daylightControl.isChecked;
        powerStorage.daylightRecharge = daylightRecharge.isChecked;
        powerStorage.daylightDischarge = daylightDischarge.isChecked;
        powerStorage.nightRecharge = nightRecharge.isChecked;
        powerStorage.nightDischarge = nightDischarge.isChecked;
        powerStorage.neutralRecharge = neutralRecharge.isChecked;
        powerStorage.neutralDischarge = neutralDischarge.isChecked;
        powerStorage.meteorOverride = meteorOverride.isChecked;
        powerStorage.meteorRecharge = meteorRecharge.isChecked;
        powerStorage.meteorDischarge = meteorDischarge.isChecked;
        powerStorage.rechargeRate = (int) rechargeRate.getValue();
        powerStorage.dischargeRate = (int) dischargeRate.getValue();
        automaticControl.y = Y+spacing;
        automaticControl.x = text("Enable automatic control");
        divider();
        if(powerStorage.automaticControl){
            daylightControl.y = Y+spacing;
            daylightControl.x = text("Enable Daylight Control");
            if(powerStorage.daylightControl){
                daylightThreshold.y = Y+spacing;
                daylightThreshold.x = text("Daylight Threshold:");
                int X = drawText(spacing, Y+spacing, Core.helper.displayWidth(), Y+spacing+textHeight*2+spacing*2, "During daylight:", textHeight);
                daylightRecharge.y = Y+spacing;
                daylightRecharge.x = X+text(X+spacing, "Recharge:");
                daylightDischarge.y = Y+spacing;
                daylightDischarge.x = X+text(X+spacing, "Discharge:");
                X = drawText(spacing, Y+spacing, Core.helper.displayWidth(), Y+spacing+textHeight*2+spacing*2, "During night:", textHeight);
                nightRecharge.y = Y+spacing;
                nightRecharge.x = X+text(X+spacing, "Recharge:");
                nightDischarge.y = Y+spacing;
                nightDischarge.x = X+text(X+spacing, "Discharge:");
            }else{
                int X = drawText(spacing, Y+spacing, Core.helper.displayWidth(), Y+spacing+textHeight*2+spacing*2, "Neutral settings:", textHeight);
                neutralRecharge.y = Y+spacing;
                neutralRecharge.x = X+text(X+spacing, "Recharge:");
                neutralDischarge.y = Y+spacing;
                neutralDischarge.x = X+text(X+spacing, "Discharge:");
            }
            divider();
            meteorOverride.y = Y+spacing;
            meteorOverride.x = text("Meteor Shower Override");
            if(powerStorage.meteorOverride){
                meteorRecharge.y = Y+spacing;
                meteorRecharge.x = text("Recharge:");
                meteorDischarge.y = Y+spacing;
                meteorDischarge.x = text("Discharge:");
            }
        }
        rechargeRate.y = Y+spacing;
        rechargeRate.x = text("Recharge rate");
        dischargeRate.y = Y+spacing;
        dischargeRate.x = text("Discharge rate");
        for(MenuComponent c : components){
            c.x+=dividerHeight;
        }
    }
    @Override
    public void keyEvent(int key, int scancode, boolean isPress, boolean isRepeat, int modifiers){
        if(key==Controls.menu&&isPress&&!isRepeat){
            close();
        }
    }
    private int text(String text){
        return text(0, text);
    }
    private int text(int X, String text){
        return text(X, text, textHeight);
    }
    private int text(String text, double textHeight){
        return text(0, text, textHeight);
    }
    private int text(int X, String text, double textHeight){
        drawText(spacing+X, Y+spacing, Core.helper.displayWidth(), Y+spacing+textHeight, text);
        Y+=spacing*2+textHeight;
        return (int) Math.round(FontManager.getLengthForStringWithHeight(text, textHeight));
    }
    private int drawText(double left, double top, double right, double bottom, String text, double height){
        double center = (top+bottom)/2;
        top = center-height/2;
        bottom = center+height/2;
        drawText(left, top, right, bottom, text);
        return (int) Math.round(FontManager.getLengthForStringWithHeight(text, height));
    }
    private void divider(){
        Y+=dividerHeight;
    }
}