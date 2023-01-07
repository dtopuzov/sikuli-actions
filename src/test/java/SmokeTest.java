import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SmokeTest {
    @Test
    public void testSikuliInit(){
        Screen screen = new Screen();
        var image = screen.capture();
        Assert.assertNotNull(image);
    }
}
