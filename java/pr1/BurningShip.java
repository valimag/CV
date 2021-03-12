import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator {
    /**
     * Максимальное число итераций перед объявлением точки в
     * Набор BurningShip.
     **/
    public static final int MAX_ITERATIONS = 2000;
    
    /** Укажите интересующую область комплексной плоскости для фрактала. **/
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }
    
    /**
     * Эта функция вычисляет необходимое количество итераций
     * для C = a + bi бежать (т. е. иметь величину 2 или больше).
     * Если точка не экранируется в пределах MAX_ITERATIONS, то возвращается значение -1
     * указать как таковой. Для набора BurningShip итерационная функция является
     * z_n = (z_{n-1})^2 + c.
     **/
    public int numIterations(double a, double b) {
        /** Квадрат величины С. **/
        double magSq;
        /** действительная часть z_i. **/
        double re = a;
        /** мнимая часть z_i. **/
        double im = b;
        /** действительная часть z_{i+1}. **/
        double nextRe;
        /** мнимая часть z_{i+1}. **/
        double nextIm;
        /** Переменная для подсчета итераций. **/
        int i = 0;
        
        while (i < MAX_ITERATIONS) {
            i += 1;
            nextRe = a + Math.abs(re) * Math.abs(re) 
            		   - Math.abs(im) * Math.abs(im);
            nextIm = b + 2 * Math.abs(re) * Math.abs(im);
            re = nextRe;
            im = nextIm;
            magSq = re * re + im * im;
            if (magSq > 4) {
                return i;
            }
        }
        return -1;
    }
    
    public String toString() {
    	return "Горящий корабль";
    }
}