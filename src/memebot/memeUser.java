package memebot;

import java.util.ArrayList;
import static memebot.MyListener.memeList;

public class memeUser implements java.io.Serializable 
{
   //declaring variables
    private String userID;
    private String userName;
    private int xp;
    private int level;
    private int totalReactions;
    
    //main constructor
    memeUser() {
        userID = "1234567890";
        userName = "user";
        xp = 0;
        level = 1;
        totalReactions = 0;
    }
    
    //ver 0.1
    memeUser(String userID, String userName, int xp, int level) {
        this.userID = userID;
        this.userName = userName;
        this.xp = xp;
        this.level = level;
        this.totalReactions = 0;
    }
    
    //ver 0.1.1
    memeUser(String userID, String userName, int xp, int level, int totalReactions) {
        this.userID = userID;
        this.userName = userName;
        this.xp = xp;
        this.level = level;
        this.totalReactions = totalReactions;
    }
    
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public int getXP(ArrayList memeList) {
        xpManager(memeList);
        return xp;
    }
    
    public int getLevel() {
        return level;
    }
    
    @Override
    public String toString() {
        return userID + "," + userName + "," + xp + "," + level + "," + totalReactions;
    }
    
    private void levelUp() {
        if(xp >= 1000){
            level = 1;
            
        } else if(xp >=5000) {
            level = 2;
        } else if(xp >=10000) {
            level = 3;
        } else if(xp >=25000) {
            level = 4;
        } else if(xp >=50000) {
            level = 5;
        }
    }
    
    public void addReactedXP() {
        totalReactions += 1;
    }
    
    private void xpManager(ArrayList<uploadedMeme> memeList) {
        int count = 0;
        int numMemes = 0;
        int totalVotes = 0;
        
        while (count < memeList.size()) {
            uploadedMeme currentMeme = memeList.get(count);
            if (userID.equals(currentMeme.getOwnerID())) {
                numMemes++;
                totalVotes += currentMeme.getRating();
            }
            count++;
        }
        
        xp = totalReactions + numMemes + ((totalVotes / numMemes) * 10); // Super secret xp algorithm
        levelUp();
    }
}
