import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    /**
     * Максимальное число итераций перед объявлением точки в
     * множество Мандельброта.
     **/
    public static final int MAX_ITERATIONS = 2000;

    /** Указывает интересующую область комплексной плоскости для фрактала. **/
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    /**
     * Эта функция вычисляет необходимое количество итераций
     * для C = a + bi
     * Если точка не экранируется в пределах MAX_ITERATIONS, то возвращается значение -1
     * указать как таковой. Для множества Мандельброта итерационная функция имеет вид
     * z_n = (z_{n-1})^2 + c.
     **/
    public int numIterations(double a, double b) {
        /** Квадрат величины С. **/
        double magSq;
        /** Вещественная часть z_i. **/
        double re = a;
        /** Мнимая часть  z_i. **/
        double im = b;
        /** Вещественная часть  z_{i+1}. **/
        double nextRe;
        /** Мнимая часть z_{i+1}. **/
        double nextIm;
        /** Переменная для подсчета итераций. **/
        int i = 0;
        
        while (i < MAX_ITERATIONS) {
            i += 1;
            nextRe = a + re * re - im * im;
            nextIm = b + 2 * re * im;
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
    	return "Мандельброт";
    } //окошко для выбора фрактала
}
