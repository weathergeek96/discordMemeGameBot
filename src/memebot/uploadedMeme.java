package memebot;

import java.util.ArrayList;

public class uploadedMeme implements java.io.Serializable
{
    private String memeID;
    private String botID;
    private int score;
    private int rating;
    private String ownerID;
    private int numRatings;
    ArrayList<String> votedAlready = new ArrayList<>();
    
    uploadedMeme(){
        memeID = "";
        score = 0;
        numRatings = 0;
    }
    
    uploadedMeme(String memeID, String botID, int score, int rating, String ownerID, int numRatings, ArrayList<String> votedAlready){
        this.memeID = memeID;
        this.botID = botID;
        this.score = score;
        this.rating = rating;
        this.ownerID = ownerID;
        this.numRatings = numRatings;
        this.votedAlready = votedAlready;
    }
    
    public boolean checkVoted(String userID) {
        for(int i = 0; i < votedAlready.size(); i++){
            if(votedAlready.get(i).equals(userID)){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
    public void addVoted(String userID) {
        votedAlready.add(userID);
    }
    
    public void removeVoted(String userID) {
        for(int i = 0; i < votedAlready.size(); i++){
            if(votedAlready.get(i).equals(userID)){
                votedAlready.remove(i);
                break;
            }
        }
    }
    
    public void addScore(int score){
        this.score = this.score + score;
        numRatings++;
        calcScore();
    }
    
    public void subtractScore(int score) {
        this.score = this.score - score;
        numRatings--;
        calcScore();
    }
    
    public void setMemeID(String memeID){
        this.memeID = memeID;
    }
    
    public void setBotID(String botID){
        this.botID = botID;
    }
    
    public void setOwnerID(String ownerID){
        this.ownerID = ownerID;
    }
    
    public int getScore(){
        return score;
    }
    
    public String getMemeID(){
        return memeID;
    }
    
    public String getOwnerID(){
        return ownerID;
    }
    
    public int getRating(){
        return rating;
    }
    
    public String getBotID(){
        return botID;
    }
    
    private void calcScore(){
        if(numRatings <=0){
            rating = 0;
        } else {
            rating = score/numRatings;
        }
        System.out.println("rating: " + rating + " numRatings: " + numRatings);
    }
    
    @Override
    public String toString(){
        String values = "";
        
        for(int i = 0; i < votedAlready.size(); i++){
            if(i==0){
                values = votedAlready.get(i);
            } else {
                values = values + "," + votedAlready.get(i);
            }
        }
        
        return memeID + "," + botID + "," + score + "," + rating + "," + ownerID + "," +  numRatings + "," + values + ",EOV";
    }
}

