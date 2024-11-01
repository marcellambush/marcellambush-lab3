
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WordCounter {
    public static int processText(StringBuffer text, String stopword) throws InvalidStopwordException, TooSmallText{
        Pattern regrex = Pattern.compile("[A-Za-z0-9']+");
        Matcher regrexMatcher = regrex.matcher(text);
        int count = 0;
        boolean check = false;
        int index = 0;
        while(regrexMatcher.find()){
            String word = regrexMatcher.group();
            count++;
            if (stopword != null && word.equals(stopword)){
                check = true;
                index = count;
            }
        }
        if (count < 5 || count == 0){
            throw new TooSmallText("Only found " + count + " words.");
        }
        if ((check == false && stopword != null)){
            throw new InvalidStopwordException("Couldn't find stopword: " + stopword);
        }
        if (stopword != null && check == true){
            return index;
        }
        return count;
    }
    public static StringBuffer processFile(String path) throws EmptyFileException{
        StringBuffer text = new StringBuffer();
        boolean found = false;
        while (!found){
            try{
                System.out.println("Opening file " + path);
                BufferedReader reader = new BufferedReader(new FileReader(path));
                found = true;
                String line = reader.readLine();
                while(line != null){
                    text.append(line);
                    line = reader.readLine();                  
                }

            }
            catch(IOException x){
                System.out.println("File not found - Please re-enter path");
                Scanner response = new Scanner(System.in);
                path = response.nextLine();
            }
        }
        if (text.length() == 0 ){
            throw new EmptyFileException(path + " was empty");
        }
        return text;
    
    }
    public static void main(String args[]){
        int decision = 0;
        Scanner inputs = new Scanner(System.in);
        while (decision != 1 && decision != 2) {
            System.out.println("Option 1 for file or Option 2 for text");
            if (inputs.hasNextInt()) {
                decision = inputs.nextInt();
                if (decision != 1 && decision != 2) {
                    System.out.println("Invalid Option");
                    inputs = new Scanner(System.in);
                }
            }
        }
        
        String stopword = null;
        if (args.length > 1) {
            stopword = args[1];
        }
        
        try {
            StringBuffer text;
            if (decision == 1) {
                String path = args[0];
                text = processFile(path);
                int wordCount = processText(text, stopword);
                System.out.println("Found " + wordCount + " words.");
            } else {
                System.out.println("Enter text:");
                inputs.nextLine();
                String inputText = inputs.nextLine();
                text = new StringBuffer(inputText);
                int wordCount = processText(text, stopword);
                System.out.println("Found " + wordCount + " words.");
            }
        } catch (InvalidStopwordException e) {
            System.out.println(e);
            System.out.println("Please Re-Enter Stopword");
            Scanner in = new Scanner(System.in);
            stopword = in.nextLine();
        } catch (TooSmallText e) {
            System.out.println(e);
        } catch (EmptyFileException e) {
            try {
                processText(new StringBuffer(), stopword);
            } catch (TooSmallText x){
                System.out.println(x);
            } catch (InvalidStopwordException x) {
                System.out.println(x);
            }
        }
    }
    
}