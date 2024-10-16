public class CourseScheduler {
    public static void insertionSort(int [][] courses){
        int length = courses.length;
        for (int i = 1; i <length ; i++) {
            int key[] = courses[i];
            int index = i-1;
            while(index>=0 && courses[index][1]>=key[1])
            {
                if(courses[index][1]==key[1] && courses[index][0]<=key[0] )
                {
                        break;
                }
                courses[index+1]=courses[index];
                index--;
            }
            courses[index+1] = key;
        }
    }
    public static int maxNonOverlappingCourses(int[][] courses)
    {

        int length = courses.length;
        if(length<=1) return length;
        insertionSort(courses);
        int prev[] = {courses[0][0],courses[0][1]};
        int counter=1;
        for (int i = 1; i < length; i++) {
            if(prev[1]<=courses[i][0])
            {
                prev = courses[i];
                counter++;
            }
        }
    //    for(var el: courses){
    //        System.out.printf("[ %d,%d] \n",el[0],el[1]);
    //    }
        return counter;
    }

    public static void main(String[] args) {
        int course[][] = new int[][]{{19, 22}, {17, 19}, {9, 12}, {9, 11}, {15, 17}, {15, 17}};
        System.out.println(maxNonOverlappingCourses(course));
        System.out.println(maxNonOverlappingCourses(new int[][]{{9, 11}, {10, 12}, {11, 13}, {15, 16}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{19, 22}}));
        System.out.println(maxNonOverlappingCourses(new int[][]{{13, 15}, {13, 17}, {11, 17}}));
    }
}
