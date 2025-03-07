package themes;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public class Theme1 extends FlatMacLightLaf {
    public static boolean setup() {
        return setup(new Theme1());
    }
    @Override
    public String getName() {
        return "Theme1";
    }
}