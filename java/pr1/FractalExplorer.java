import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Этот класс предоставляет графический интерфейс для отображения фракталов. В нем также есть дома
 * основная функция, которая запускает приложение.
 */
public class FractalExplorer {
    /** Боковая длина нашей области дисплея. **/
    private int dispSize;
    /** Область изображения для нашего фрактала. **/
    private JImageDisplay img;
    /** Используется для генерации фракталов заданного вида. **/
    private FractalGenerator fGen;
    /** Текущая область просмотра на нашем изображении. **/
    private Rectangle2D.Double range;

    /** Различные компоненты графического интерфейса. **/
    JFrame frame;
    JButton resetButton;
    JButton saveButton;
    JLabel label;
    JComboBox<FractalGenerator> fractalCBox;
    JPanel cbPanel;
    JPanel buttonPanel;
    /** Количество строк, которые все еще рисуются. **/
    int rowsRemaining;

    /** Базовый конструктор. Инициализирует отображение изображения, фрактальный генератор,
     * и начальная зона просмотра.
     */
    public FractalExplorer(int dispSize) {
        this.dispSize = dispSize;
        this.fGen = new Mandelbrot();
        this.range = new Rectangle2D.Double(0, 0, 0, 0);
        fGen.getInitialRange(this.range);
        
    }

    /**
     * Настройка и отображение графического интерфейса пользователя.
     */
    public void createAndShowGUI() {
        /** Создайте компоненты графического интерфейса. **/
        frame = new JFrame("Fractal Explorer");
        img = new JImageDisplay(dispSize, dispSize);
        resetButton = new JButton("Обновить");
        resetButton.setActionCommand("reset");
        saveButton = new JButton("Сохранить изображение");
        saveButton.setActionCommand("save");
        label = new JLabel("Фрактал: ");
        fractalCBox	= new JComboBox<FractalGenerator>();
        cbPanel = new JPanel();
        cbPanel.add(label);
        cbPanel.add(fractalCBox);
        buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        fractalCBox.addItem(new Mandelbrot());
        fractalCBox.addItem(new BurningShip());
        fractalCBox.addItem(new Tricorn());
        
        /** Помещает все компоненты в рамку.  **/
        ActionHandler aHandler = new ActionHandler();
        MouseHandler mHandler = new MouseHandler();
        resetButton.addActionListener(aHandler);
        saveButton.addActionListener(aHandler);
        img.addMouseListener(mHandler);
        fractalCBox.addActionListener(aHandler);
        
        /** Помещает все компоненты в рамку.  **/
        frame.setLayout(new java.awt.BorderLayout());
        frame.add(img, java.awt.BorderLayout.CENTER);
        frame.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        frame.add(cbPanel, java.awt.BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /** Отображает изображение. **/
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    
    /** Включите или отключите интерактивность пользовательского интерфейса. **/
    public void enableUI(boolean val) {
    	saveButton.setEnabled(val);
    	resetButton.setEnabled(val);
    	fractalCBox.setEnabled(val);
    }

    /** Использует генератор фракталов, чтобы нарисовать фрактал пиксель за пикселем. **/

    private void drawFractal() {
    	/** Отключите интерактивность пользовательского интерфейса во время отрисовки. **/
    	enableUI(false);
    	// Draw the fractal line by line.
    	rowsRemaining = dispSize;
        for (int i = 0; i < dispSize; i++) {
            FractalWorker rowDrawer = new FractalWorker(i);
            rowDrawer.execute();
        }
    }
    
    /** Запуск приложения **/
    public static void main(String[] args) {
        FractalExplorer fracExp = new FractalExplorer(800);
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }
    
    /** 
     * Обработчик для сброса камеры, изменения фрактального типа,
     * и сохранение изображений
     **/
    public class ActionHandler implements ActionListener {
    	/** Выполняйте различные действия. **/
        public void actionPerformed(ActionEvent e) {    
        	/** Если нажата кнопка сброса, сбросьте вид камеры. **/
        	if (e.getActionCommand() == "reset") {
                fGen.getInitialRange(range);
                drawFractal();
        	}
        	/** Если вы нажмете кнопку Сохранить, сохраните изображение. **/
        	else if (e.getActionCommand() == "save") {
        		JFileChooser fileChooser = new JFileChooser();
        		FileFilter filter 
        			= new FileNameExtensionFilter("PNG Images", "png");
        		fileChooser.setFileFilter(filter);
        		fileChooser.setAcceptAllFileFilterUsed(false);
        		int res = fileChooser.showSaveDialog(img);
        		
        		if (res == JFileChooser.APPROVE_OPTION) {
        			try {
						javax.imageio.ImageIO.write(img.getBufferedImage(),
								"png", fileChooser.getSelectedFile());
					} catch (NullPointerException | IOException e1) {
						javax.swing.JOptionPane.showMessageDialog(img,
								e1.getMessage(), "Cannot Save Image",
								JOptionPane.ERROR_MESSAGE);
					}
        		}
        		else {
        			return;
        		}
        	}

        	else if (e.getSource() == (Object) fractalCBox) {
        		fGen = (FractalGenerator) fractalCBox.getSelectedItem();
                fGen.getInitialRange(range);
                drawFractal();
        	}
        }
    }
    
    
    /** Простой обработчик для увеличения масштаба нажатого пикселя.  **/
    class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
        	// If rows are being drawn, ignore mouse input.
        	if (rowsRemaining != 0) {
        		return;
        	}
            double xCoord = FractalGenerator.getCoord(range.x, 
                    range.x + range.width, dispSize, e.getX());
            double yCoord = FractalGenerator.getCoord(range.y, 
                    range.y + range.width, dispSize, e.getY());
            fGen.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
    
    /** Этот класс отрисовывает одну строку фрактала.**/
    private class FractalWorker extends SwingWorker<Object, Object> {
    	/** Y-координата для строки **/
    	int rowY;
    	/** Массив значений rgb для пикселей в этой строке. **/
    	int[] rgbVals;
    	
    	/** Конструктор **/
    	public FractalWorker(int yCoord) {
    		rowY = yCoord;
    	}
    	
    	/**
    	 *  Вычисляет значения rgb для строки и сохраняет их в памяти
         *  массив.
    	 **/
    	public Object doInBackground() {
    		rgbVals = new int[dispSize];
		    double yCoord = FractalGenerator.getCoord(range.y, 
		            range.y + range.width, dispSize, rowY);
		    
		    for (int i = 0; i < dispSize; i++) {
		    	double xCoord = FractalGenerator.getCoord(range.x, 
	                    range.x + range.width, dispSize, i);
			    double numIters = fGen.numIterations(xCoord, yCoord);
			    
			    if (numIters == -1) {
			        /** Красит пиксель в черный цвет если нет в наборе **/
			        rgbVals[i] = 0;
			    }
			    else {
			        /** Пиксель находится во фрактальном множестве.
                     * Раскрасьте пиксель в зависимости от количества итераций
			         */
			        float hue = 0.7f + (float) numIters / 200f;
			        int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
			        rgbVals[i] = rgbColor;
			    }
		    }
		    return null;
    	}
    	
    	/**
    	 * Выводит значения rgb на экран.
    	 */
    	public void done() {
    		for (int i = 0; i < dispSize; i++) {
    			img.drawPixel(i, rowY, rgbVals[i]);
    		}
    		img.repaint(0, 0, rowY, dispSize, 1);
    		// Decrement the number of rows still being drawn.
    		rowsRemaining -= 1;
    		// If all rows are done, reenable the UI.
    		if (rowsRemaining == 0) {
    			enableUI(true);
    		}
    	}
    }
}
