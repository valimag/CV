import java.awt.geom.Rectangle2D;

public abstract class FractalGenerator {

    /**
     * Эта статическая вспомогательная функция принимает целочисленную координату и преобразует ее
     * в значение двойной точности, соответствующее определенному диапазону. Это
     * используется для преобразования координат пикселей в значения двойной точности для
     * вычисление фракталов и т. д.
     *
     * @param rangeMin минимальное значение диапазона с плавающей точкой
     * @param rangeMax максимальное значение диапазона с плавающей точкой
     *
     * @param size размер измерения, из которого берется координата пикселя.
     * Например, это может быть ширина изображения или высота изображения.
     *
     * @param coord координата для вычисления значения двойной точности.
     * Координата должна находиться в диапазоне [0, размер].
     */
    public static double getCoord(double rangeMin, double rangeMax,
        int size, int coord) {

        assert size > 0;
        assert coord >= 0 && coord < size;

        double range = rangeMax - rangeMin;
        return rangeMin + (range * (double) coord / (double) size);
    }


    /** позволяет указать генератору фрактала, какая часть комплектной области "интересна" для вычисления фрактала */
    public abstract void getInitialRange(Rectangle2D.Double range);


    /**
     * Обновляет текущий диапазон для центрирования в заданных координатах,
     * и быть увеличенным или уменьшенным с помощью заданного коэффициента масштабирования.
     */
    public void recenterAndZoomRange(Rectangle2D.Double range,
        double centerX, double centerY, double scale) {

        double newWidth = range.width * scale;
        double newHeight = range.height * scale;

        range.x = centerX - newWidth / 2;
        range.y = centerY - newHeight / 2;
        range.width = newWidth;
        range.height = newHeight;
    }


    /**
     * Даны координаты тегом <em>х</ем> + <ЭМ>ий</EM> в комплексной плоскости,
     * вычисляет и возвращает количество итераций перед фракталом
     * функция экранирует ограничивающую область для этой точки. Которая
     * не выключается до тех пор, пока не будет достигнут предел итерации.
     * с результатом -1.
     */
    public abstract int numIterations(double x, double y);
}

