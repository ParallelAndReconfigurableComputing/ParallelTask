


public class FutureArrayTest {
    public static void main(String[] args) {
        int n = 10;
        int[] array = new int[n];
        for (int i = 0 ; i < 5 ; i++) {
            array[i] = i * 10;
        }
        array[5] = 12;
        array[6] = 3;
        for (int i = 7 ; i < 10 ; i++) {
            array[i] = i * 15;
        }
        for (int i = 0 ; i < (array.length) ; i++) {
            System.out.print(array[i]);
            if (i != ((array.length) - 1))
                System.out.print(", ");
            
        }
        int[] array2 = new int[3];
    }
    
}

