import org.sikuli.script.Image;
import org.sikuli.script.OCR;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

public class SmokeTest {
    @Test
    public void testSikuliInit(){
        Screen screen = new Screen();
        var image = screen.capture();
        Assert.assertTrue(image.w >= 1024);
        Assert.assertTrue(image.h >= 768);
    }

    @Test
    public void testClick(){
        Screen screen = new Screen();
        screen.mouseMove(1,1);
        screen.click();
    }

    @Test
    public void testOCRFromFile(){
        File file = new File("fonts.png");
        Image img = Image.create(file.getAbsolutePath());
        String text = img.getSub(0,0,img.w, img.h).text();
        Assert.assertTrue(text.contains("Arial"), "Actual text: \n" +  text);
    }

    @Test
    public void testOCRFromScreen(){
        Region region = new Region(0, 0, 200, 200);
        region.highlight(1);
        String actualText = OCR.readText(region);
        Assert.assertNotNull(actualText);
    }
}
