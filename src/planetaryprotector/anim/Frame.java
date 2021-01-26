package planetaryprotector.anim;
import simplelibrary.opengl.ImageStash;
public class Frame{
    private Type type;
    private String path;
    private int texture;
    public Frame(int texture){
        this(Type.TEXTURE);
        this.texture = texture;
    }
    public Frame(String path){
        this(Type.PATH);
        this.path = path;
    }
    private Frame(Type type){
        this.type = type;
    }
    public int getTexture(){
        switch(type){
            case PATH:
                texture = ImageStash.instance.getTexture(path);
                type = Type.TEXTURE;
            case TEXTURE:
                return texture;
            default:
                throw new IllegalArgumentException("Invalid frame type: "+type);
        }
    }
    private static enum Type{
        /**
         * a path to a physical texture
         */
        PATH,
        /**
         * an int representing a loaded texture
         */
        TEXTURE;
    }
}