import java.util.Arrays;

public class TextJustifier
{
    public static String[] appendToRow(String[] array,String text)
    {
        array= Arrays.copyOf(array,array.length+1);
        array[array.length-1] = text;
        return array;
    }
    public static String compressRow(StringBuilder row,int maxWidth,boolean isLastRow)
    {
        if(!row.toString().contains(" "))
        {
            isLastRow=true;// in case there is only 1 word
        }
        int j=1;
        while(maxWidth>row.length() && !isLastRow)
        {
            if(j>=row.length())
            {
                j=1;
            }
            if(j+1<row.length() && row.charAt(j)==' ' && row.charAt(j+1)!=' ')
            {
                row.insert(j,' ');
                j++;
            }
            j++;
        }
        while(maxWidth>row.length())
        {
            row.append(' ');
        }
        return row.toString();
    }
    public static String[] justifyText(String[] words,int maxWidth)
    {
        String[] result = new String[0];
        int length= words.length;
        if(length<1) return new String[]{};
        int currentWidth=0;
        StringBuilder currentRow = new StringBuilder();

        for (int i = 0; i <length ; i++) {
           currentWidth=words[i].length();
           if(currentWidth+ currentRow.length()-1>=maxWidth)
           {
               currentRow.deleteCharAt(currentRow.length()-1);
               String textToPush = compressRow(currentRow,maxWidth,false);
               result = appendToRow(result,textToPush);
               currentRow.delete(0,currentRow.length());
           }
            currentRow.append(words[i]).append(' ');
        }
        currentRow.deleteCharAt(currentRow.length()-1);
        String textToPush = compressRow(currentRow,maxWidth,true);
        result = appendToRow(result,textToPush);
        return result;
    }
    public static void print(String[] words){
        for(var el: words)
        {
            System.out.printf("[%s]\n",el);
        }
    }
    public static void main(String[] args) {
        String[] example1 = {"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog."};//11
        String[] example2 = {"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."};//20
        String[] example3={"Science", "is", "what"};//15
        String[] words = {"LongWord1", "LongWord2", "LongWord3", "LongWord4"};
        int maxWidth = 50;
        String[] result0=justifyText(words,maxWidth);
        print(result0);
        String[] result1 = justifyText(example1,12);
        print(result1);
        String[] result2 = justifyText(example2,7);
        print(result2);
    }
}
