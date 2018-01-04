package memebot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class backend {
    
    public static void save(ArrayList userList, ArrayList memeList) {
        try {
            saveList(memeList, "memelist.txt");
            saveList(userList, "userlist.txt");
        } catch (IOException ex) {
            Logger.getLogger(backend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void saveList(ArrayList list, String dest) throws IOException {
        FileWriter fileWriter = new FileWriter(dest);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(int i = 0; i < list.size(); i++) {
            printWriter.println(list.get(i).toString());
        }
        printWriter.close();
        System.out.println("List Saved: " + dest);
    }
    
    public static ArrayList<uploadedMeme> loadMemeList() throws FileNotFoundException {
        //Creating both scanner objects
        Scanner fileScanner, lineScanner;
        
        //inputs the file
        fileScanner = new Scanner (new File("memelist.txt"));
        
        ArrayList<uploadedMeme> memeList  = new ArrayList<>();
        
        while (fileScanner.hasNext()) {
            
            //reads next line
            String fileLine = fileScanner.nextLine();
            
            //takes the next line and assigns lineScanner to it
            lineScanner = new Scanner(fileLine);
            lineScanner.useDelimiter(",");
            
            //Gets everything from the line and puts them into variables
            String memeID = lineScanner.next();
            String botID = lineScanner.next();
            int score = lineScanner.nextInt();
            int rating = lineScanner.nextInt();
            String ownerID = lineScanner.next();
            int numRatings = lineScanner.nextInt();
            ArrayList<String> votedAlready = new ArrayList<>();
            while (lineScanner.hasNext()) {
                String nextLine = lineScanner.next();
                if(nextLine.equals("EOV")) {
                    System.out.println("Found EOV, BREAKING");
                    break;
                } else {
                    votedAlready.add(nextLine);
                }
            }
            
            uploadedMeme tempMeme = new uploadedMeme(memeID, botID, score, rating, ownerID, numRatings, votedAlready);
            System.out.println("Temp meme add: " + tempMeme.toString());
            memeList.add(tempMeme);
            
        }
        return memeList;
    }
    
        public static ArrayList<memeUser> loadUserList() throws FileNotFoundException {
        //Creating both scanner objects
        Scanner fileScanner, lineScanner;
        
        //inputs the file
        fileScanner = new Scanner (new File("userlist.txt"));
        
        ArrayList<memeUser> userList  = new ArrayList<>();
        
        while (fileScanner.hasNext()) {
            
            //reads next line
            String fileLine = fileScanner.nextLine();
            
            //takes the next line and assigns lineScanner to it
            lineScanner = new Scanner(fileLine);
            lineScanner.useDelimiter(",");
            
            //Gets everything from the line and puts them into variables
            String userID = lineScanner.next();
            String userName = lineScanner.next();
            int xp = lineScanner.nextInt();
            int level = lineScanner.nextInt();
            
            
            memeUser tempUser = new memeUser(userID, userName, xp, level);
            System.out.println("Temp user add: " + tempUser.toString());
            userList.add(tempUser);
            
        }
        return userList;
    }
        
}