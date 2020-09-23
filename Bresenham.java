package bresenham;

/*author Janet Muhia*/

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;  
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Bresenham {

    static int x0 = 0;
    static int y0 = 0;
    static int x1 = 0;
    static int y1 = 0;
    static int i = 0;
    
    public static BufferedImage gradientSetRaster(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        WritableRaster raster = img.getRaster();
        int[] pixel = new int[3]; //RGB

            for (int y = 0; y < height; y++) {
                int val = (int) (y * 255f / height);
                for (int shift = 1; shift < 3; shift++) {
                    pixel[shift] = val;
                }

                for (int x = 0; x < width; x++) {
                    raster.setPixel(x, y, pixel);
                }
            }

        return img;
    }

    public static void main(String... args) {
        Frame w = new Frame("Raster");  //window
        final int imageWidth = 500;
        final int imageHeight = 500;

        w.setSize(imageWidth,imageHeight);
        w.setLocation(100,100);
        w.setVisible(true);

        Graphics g = w.getGraphics();

        BufferedImage img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        gradientSetRaster(img);
        g.drawImage(img, 0, 0, (img1, infoflags, x, y, width, height) -> true);  //draw the image. You can think of this as the display method.


        w.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                  int[] pixel = {255,255,255};
                  img.getRaster().setPixel(e.getX(),e.getY(),pixel);
                  g.drawImage(img, 0, 0, (img1, infoflags, x, y, width, height) -> true);
                  if(is_first()){
                	  x0 = e.getX();
                	  y0 = e.getY();
                	  i = 1;
                  }
                  else{
                	  x1 = e.getX();
                	  y1 = e.getY();
                	  i = 0;
                	  draw_line(img, g, x0, y0, x1, y1);
                  }
                }
            }
            
            public boolean is_first(){
                return i == 0;
            }
            
            public void draw_line(BufferedImage img, Graphics g, int x0, int y0, int x1, int y1){
            	int d = 0;
                int[] pixel = {255,255,255};
            	int dx = Math.abs(x1 - x0);
            	int dy = Math.abs(y1 - y0);
            	
            	//use for determining the sign of the slope
            	int ix = x0 < x1 ? 1 : -1; 
            	int iy = y0 < y1 ? 1 : -1;
            	
            	int x = x0;
            	int y = y0;
            	
            	//when slope < 1
            	if(dx >= dy){
            		while(true){
            			img.getRaster().setPixel(x, y, pixel);
            			if(x == x1)
            				break;
            			x += ix;
            			d += dy*2;
            			if(d > dx){
            				y += iy;
            				d -= dx*2;
            			}
            		}
            	}
            	//when slope > 1
            	else{
            		while(true){
            			img.getRaster().setPixel(x, y, pixel);
            			if(y == y1)
            				break;
            			y += iy;
            			d += dx*2;
            			if(d > dy){
            				x += ix;
            				d -= dy*2;
            			}
            		}
            	}
            g.drawImage(img, 0, 0, (img1, infoflags, a, b, width, height) -> true);
            }
        });
        

        w.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                w.dispose();
                g.dispose();
                System.exit(0);
            }
        });
    }
}
