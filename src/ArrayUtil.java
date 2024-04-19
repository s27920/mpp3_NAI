public class ArrayUtil {
    static public LangFile[] mergeArrays(LangFile[] arr1, LangFile[] arr2){
        LangFile[] toReturn = new LangFile[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, toReturn, 0, arr1.length);
        System.arraycopy(arr2, 0, toReturn, arr1.length, arr2.length);
        return toReturn;
    }


    static public int findMaxIndex(float[] arr){
        float max = Float.NEGATIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max){
                max = arr[i];
                index = i;
            }
        }
        return index;
    }

    static public float[] normalize(float[] arr){
        float max = arr[findMaxIndex(arr)];
        for (int i = 0; i < arr.length; i++) {
            arr[i] /=max;
        }
        return arr;
    }
}
