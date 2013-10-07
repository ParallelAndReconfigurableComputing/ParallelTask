package pt.examples.ParaImagePro;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/* Utils.java is used by FileChooserDemo2.java. */
public class Utils {
    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    static Image getImg(String imgFileName) {
    	Image img = null;
		try {
			img = ImageIO.read(Utils.class.getResourceAsStream("images/" + imgFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return img;
    }
}
