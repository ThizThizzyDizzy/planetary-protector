package planetaryprotector.sound;
import com.thizthizzydizzy.dizzyengine.sound.Sound;
import java.io.InputStream;
import java.util.function.Supplier;
public class NamedSound extends Sound{
    public final String name;
    public NamedSound(String path){
        this(path, path);
    }
    public NamedSound(String name, String path){
        super(path);
        this.name = name;
    }
    public NamedSound(Supplier<InputStream> soundSupplier){
        super(soundSupplier);
        this.name = null;
    }
}
