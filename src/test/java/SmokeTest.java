import org.sikuli.script.Image;
import org.sikuli.script.OCR;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    public void testOCRFromFile() throws Exception {
        File file = new File("fonts.png");
        Image img = Image.create(file.getAbsolutePath());
        String text = img.getSub(0,0,img.w, img.h).text();
        Assert.assertTrue(text.contains("Arial"), "Actual text: \n" +  text);
    }

    @Test
    public void testFindAndClickImage() throws Exception {
        // Load and display the image on screen
        File imageFile = new File("fonts.png");
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        
        JFrame frame = new JFrame("Test Image");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        
        JLabel label = new JLabel(new ImageIcon(bufferedImage));
        frame.getContentPane().add(label);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Wait for the window to be fully rendered
        Thread.sleep(1000);
        
        // Use Sikuli to find and click the image
        Screen screen = new Screen();
        Pattern pattern = new Pattern(imageFile.getAbsolutePath()).similar(0.7);
        
        org.sikuli.script.Match match = screen.find(pattern);
        System.out.println("Found image at: " + match.getTarget());
        System.out.println("Match score: " + match.getScore());
        
        // Hover over the center of the matched region
        match.hover();
        Thread.sleep(500);
        
        // Hover with offset (10 pixels right, 10 pixels down from center)
        match.hover(match.getTarget().offset(10, 10));
        Thread.sleep(500);
        
        // Right click on the match
        match.rightClick();
        Thread.sleep(300);
        
        // Double click on the match
        match.doubleClick();
        Thread.sleep(300);
        
        // Click at a specific offset from the center
        match.click(match.getTarget().offset(-20, -20));
        Thread.sleep(300);
        
        // Highlight the matched region for 2 seconds
        match.highlight(2);
        
        // Wait a bit to see the result
        Thread.sleep(1000);
        
        // Close the frame
        frame.dispose();
        
        Assert.assertNotNull(match, "Image should be found on screen");
    }

    @Test
    public void testOCRFromScreen(){
        Region region = new Region(0, 0, 200, 200);
        region.highlight(1);
        String actualText = OCR.readText(region);
        System.out.println("OCR Text from screen: " + actualText);
        Assert.assertNotNull(actualText);
    }
}
