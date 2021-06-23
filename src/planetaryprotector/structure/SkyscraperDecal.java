package planetaryprotector.structure;
import org.lwjgl.opengl.GL11;
import planetaryprotector.game.Game;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.*;
public class SkyscraperDecal{
    private final int x;
    public final int y;
    final Type type;
    private final int variant;
    public SkyscraperDecal(int x, int y, Type type, int variant){
        if(variant>=type.variants||variant<0)throw new IllegalArgumentException("Invalid skyscraper decal variant: "+type.toString()+" "+variant);
        this.x = x;
        this.y = y;
        this.type = type;
        this.variant = variant;
    }
    public Config save(Config config){
        config.set("x", x);
        config.set("y", y);
        config.set("type", type.name());
        config.set("variant", variant);
        return config;
    }
    public static SkyscraperDecal load(Config config){
        return new SkyscraperDecal(config.getInt("x"), config.getInt("y"), Type.valueOf(config.getString("type")), config.getInt("variant"));
    }
    public boolean conflictsWith(SkyscraperDecal decal){
        switch(type){
            case DUST:
                return false;
            case TOP:
                return decal.type==Type.TOP;
            case LEFT:
                if(decal.type==Type.LEFT){
                    return Math.abs(y-decal.y)<50;
                }
                if(decal.type==Type.WINDOW){
                    if(decal.y<y)return false;
                    if(decal.y>y+50)return false;
                    return decal.x<35;
                }
                return false;
            case RIGHT:
                if(decal.type==Type.RIGHT){
                    return Math.abs(y-decal.y)<50;
                }
                if(decal.type==Type.WINDOW){
                    if(decal.y<y)return false;
                    if(decal.y>y+50)return false;
                    return decal.x>65;
                }
                return false;
            case WINDOW:
                if(decal.type==Type.WINDOW)return decal.x==x&&decal.y==y;
                return decal.conflictsWith(this);//easier this way
        }
        return false;
    }
    boolean isValid(Skyscraper sky){
        switch(type){
            case DUST:
                return true;
            case LEFT:
                if(y<100)return false;
                if(y>100+(sky.floorCount-15)*Skyscraper.floorHeight)return false;
                return x==0;
            case RIGHT:
                if(y<100)return false;
                if(y>100+(sky.floorCount-15)*Skyscraper.floorHeight)return false;
                return x==50;
            case TOP:
                return x==0&&y==0;
            case WINDOW:
                if(y<100)return false;
                if(y>100+(sky.floorCount-1)*Skyscraper.floorHeight)return false;
                return true;
        }
        return true;
    }
    public void render(Skyscraper sky){
        type.render(this, sky);
    }
    public static enum Type{
        LEFT("left", 1) {
            @Override
            void render(SkyscraperDecal decal, Skyscraper sky){
                GL11.glColor4d(1, 1, 1, sky.game.hideSkyscrapers?.2:1);
                drawRectWithBounds(sky.x+decal.x, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y, sky.x+decal.x+50, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y+50, sky.x, sky.y-(Skyscraper.floorHeight*sky.floorCount), sky.x+sky.width, sky.y+sky.height-sky.fallen, ImageStash.instance.getTexture("/textures/structures/skyscraper/decals/"+Game.theme.tex()+"/"+tex+" "+(decal.variant+1)+".png"));
            }
        },
        RIGHT("right", 1) {
            @Override
            void render(SkyscraperDecal decal, Skyscraper sky){
                GL11.glColor4d(1, 1, 1, sky.game.hideSkyscrapers?.2:1);
                drawRectWithBounds(sky.x+decal.x, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y, sky.x+decal.x+50, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y+50, sky.x, sky.y-(Skyscraper.floorHeight*sky.floorCount), sky.x+sky.width, sky.y+sky.height-sky.fallen, ImageStash.instance.getTexture("/textures/structures/skyscraper/decals/"+Game.theme.tex()+"/"+tex+" "+(decal.variant+1)+".png"));
            }
        },
        TOP("top", 2) {
            @Override
            void render(SkyscraperDecal decal, Skyscraper sky){
                GL11.glColor4d(1, 1, 1, sky.game.hideSkyscrapers?.2:1);
                drawRectWithBounds(sky.x+decal.x, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y, sky.x+decal.x+100, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y+100, sky.x, sky.y-(Skyscraper.floorHeight*sky.floorCount), sky.x+sky.width, sky.y+sky.height-sky.fallen, ImageStash.instance.getTexture("/textures/structures/skyscraper/decals/"+Game.theme.tex()+"/"+tex+" "+(decal.variant+1)+".png"));
            }
        },
        DUST("dust", 10) {
            @Override
            void render(SkyscraperDecal decal, Skyscraper sky){
                GL11.glColor4d(0, 0, 0, 0.02*(sky.game.hideSkyscrapers?.2:1));
                drawRectWithBounds(sky.x+decal.x, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y, sky.x+decal.x+50, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y+50, sky.x, sky.y-(Skyscraper.floorHeight*sky.floorCount), sky.x+sky.width, sky.y+sky.height-sky.fallen, ImageStash.instance.getTexture("/textures/structures/damage/"+(decal.variant+1)+".png"));
            }
        },
        WINDOW("window", 30) {
            @Override
            void render(SkyscraperDecal decal, Skyscraper sky){
                GL11.glColor4d(1, 1, 1, sky.game.hideSkyscrapers?.1:1);
                int X = decal.variant;
                int Y = X/10;
                X-=Y*10;
                drawRectWithBounds(sky.x+decal.x, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y, sky.x+decal.x+8, sky.y-(Skyscraper.floorHeight*sky.floorCount)+decal.y+10, sky.x, sky.y-(Skyscraper.floorHeight*sky.floorCount), sky.x+sky.width, sky.y+sky.height-sky.fallen, ImageStash.instance.getTexture("/textures/structures/skyscraper/decals/"+Game.theme.tex()+"/windows.png"), X/10d, Y/3d, (X+1)/10d, (Y+1)/3d);
            }
        };
        public final String tex;
        public final int variants;
        private Type(String tex, int variants){
            this.tex = tex;
            this.variants = variants;
        }
        abstract void render(SkyscraperDecal decal, Skyscraper sky);
    }
}