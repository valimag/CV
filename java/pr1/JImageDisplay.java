import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;


public class JImageDisplay extends JComponent {

    private BufferedImage bufferedImg; /** класс для управления изображением */

    //получает целые ширину и высоту инициализирует поле BufferedImage ссылкой на новое изображение заданной ширины и высоты.
    public JImageDisplay(int width, int height) {
        bufferedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Dimension dim = new Dimension(width, height);
        super.setPreferredSize(dim);        
    }

    //отображение изображения
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImg, 0, 0, bufferedImg.getWidth(), bufferedImg.getHeight(), null);
    }

    //окрашивает каждый пиксель изображения  в черный
    public void clearImage() {
        for (int i = 0; i < bufferedImg.getWidth(); i++) {
            for (int j = 0; j < bufferedImg.getHeight(); j++) {
                bufferedImg.setRGB(i, j, 0);
            }
        }
    }

    //окрашивает пиксели в цвета
    public void drawPixel(int x, int y, int rgbColor) {
        bufferedImg.setRGB(x, y, rgbColor);
    }
    
    /** Получаем экземпляр BufferedImage. **/
    public BufferedImage getBufferedImage() {
    	return bufferedImg;
    }
    
    
}
