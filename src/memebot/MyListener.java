package memebot;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MyListener extends ListenerAdapter 
{

    static ArrayList<memeUser> userList  = new ArrayList<>();
    static ArrayList<uploadedMeme> memeList  = new ArrayList<>();
    
    public static void save() {
        backend.save(userList, memeList);
    }

    public static void load() {
        try {
            memeList = backend.loadMemeList();
            userList = backend.loadUserList();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void newUser (String ID) {
        memeUser newUser = new memeUser();
        newUser.setUserID(ID);
        userList.add(newUser);
        System.out.println("Created User: " + ID);
    }
    
    public void newMeme (String ID) {
        uploadedMeme newMeme = new uploadedMeme();
        newMeme.setMemeID(ID);
        memeList.add(newMeme);
        System.out.println("Created Meme: " + ID);
    }
    
    public memeUser checkForUser(String userID){
        //declare vars
        boolean isUser = false;
        int count = 0;
        
        //checks if user exists
        System.out.println("Finding user...");
        while (count < userList.size()) {
            if (userID.equals(userList.get(count).getUserID())) {
                isUser = true;
                break;
            }
            count = count + 1;
        }
        
        //creates or does not create user depending if isUser is true or false
        if (!isUser) {
            newUser(userID);
            memeUser localUser = userList.get(count);
            return localUser;
        } else {
            memeUser localUser = userList.get(count);
            System.out.println("Found User: " + userID);
            return localUser;
        }
    }
    
    public uploadedMeme checkForMeme(String memeID){
        //declare vars
        boolean isMeme = false;
        int count = 0;
        //checks if meme exists
        System.out.println("Searching for meme...");
        while (count < memeList.size()) {
            if (memeID.equals(memeList.get(count).getMemeID())) {
                isMeme = true;
                break;
            }
            count = count + 1;
        }
        
        //creates or does not create meme depending if isUser is true or false
        if (!isMeme) {
            newMeme(memeID);
            uploadedMeme currentMeme = memeList.get(count); //gets current meme from memeList
            return currentMeme;
        } else {
            uploadedMeme currentMeme = memeList.get(count); //gets current meme from list
            System.out.println("Found Meme: " + currentMeme.getMemeID());
            return currentMeme;
        }
    }
    
    public int getValue(ReactionEmote emoji){
        String emojiName = emoji.getName();
        switch(emojiName) {
            case "1⃣": return 1;
            case "2⃣": return 2;
            case "3⃣": return 3;
            case "4⃣": return 4;
            case "5⃣": return 5;
            case "6⃣": return 6;
            case "7⃣": return 7;
            case "8⃣": return 8;
            case "9⃣": return 9;
            case "🔟": return 10;
            default: return 0;
        }
    }
        
    //-----Detects when a message is reacted to
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        
        ReactionEmote emote = event.getReactionEmote(); //creates object emote for the reaction emote that was used
        String messageID = event.getMessageId();//gets the message ID
        MessageChannel channel = event.getChannel(); //creates channel object for the channel the reaction was placed in
        boolean isIgnored = false; // For switch statement
        String channelID = event.getChannel().getId();
        User reactedUser = event.getUser(); //creates object reactedUser for the user who reacted
        String userID = reactedUser.getId(); // gets the discord ID for the user who reacted
        
        memeUser localUser = checkForUser(userID); //Checks if the user exists. If not creates the user. Then returns the user object
        
        uploadedMeme currentMeme = checkForMeme(messageID);
        
        //Checks if the channel should be ignored or not
        switch (channelID) {
            case "368142544548397068": isIgnored = true; break; //leaderboard channel
            case "368156446736646145": isIgnored = true; break; //mods chat
            case "379263169618771969": isIgnored = true; break; //general chat
            case "354624428144984065": isIgnored = true; break; //announcements
            case "378341574612090881": isIgnored = true; break; //info-and-rules
            case "379121238984884224": isIgnored = true; break;
            default: isIgnored = false; break;
        }
        
        if (reactedUser.isBot() || isIgnored ){ //Makes sure the user is not a bot, nor in a non meme channel, nor is not a meme.
            System.out.println("User is a bot/or the channel is ignored/or the message is not a meme");
        } else {
            
            if(currentMeme.checkVoted(userID)) { //checks to see if the user already voted
                MessageReaction reaction = event.getReaction();
                reaction.removeReaction(reactedUser).queue();
            } else {
                localUser.addReactedXP();
            }
            //----------------------------
            //  Doing the rating math
            //----------------------------
            if(!reactedUser.isBot()) {
                int toAdd = getValue(emote);
                String botID = currentMeme.getBotID();

                currentMeme.addVoted(userID);

                if(toAdd>0) {
                    currentMeme.addScore(toAdd);
                    channel.editMessageById(botID, "Dankness: "+ currentMeme.getRating() + " / 10").queue();
                }
            }
            save();
        }
    }
    
        //-----Detects when a message is reacted to
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event)
    {
        ReactionEmote emote = event.getReactionEmote(); //creates object emote for the reaction emote that was used
        
        String messageID = event.getMessageId();//gets the message ID
        MessageChannel channel = event.getChannel(); //creates channel object for the channel the reaction was placed in
        String channelID = event.getChannel().getId();
        User reactedUser = event.getUser(); //creates object reactedUser for the user who reacted
        String userID = reactedUser.getId(); // gets the discord ID for the user who reacted
        boolean isIgnored = false;
        
        memeUser localUser = checkForUser(userID); //Checks if the user exists. If not creates the user. Then returns the user object
        
        //Checks if the channel should be ignored or not
        switch (channelID) {
            case "368142544548397068": isIgnored = true; break; //leaderboard channel
            case "368156446736646145": isIgnored = true; break; //mods chat
            case "379263169618771969": isIgnored = true; break; //general chat
            case "354624428144984065": isIgnored = true; break; //announcements
            case "378341574612090881": isIgnored = true; break; //info-and-rules
            case "379121238984884224": isIgnored = true; break;
            case "388422380680380417": isIgnored = true; break; //dev channel
            default: isIgnored = false; break;
        }
        
        if (reactedUser.isBot() || isIgnored ){ //Makes sure the user is not a bot, nor in a non meme channel, nor is not a meme.
            System.out.println("User is a bot/or the channel is ignored/or the message is not a meme");
        } else {
            uploadedMeme currentMeme = checkForMeme(messageID);

            //----------------------------
            //  Doing the rating math
            //----------------------------

            int toSubtract = getValue(emote);
            String botID = currentMeme.getBotID();


            if(toSubtract>0) {
                currentMeme.subtractScore(toSubtract);
                channel.editMessageById(botID, "Dankness: "+ currentMeme.getRating() + " / 10").queue();
            }
            currentMeme.removeVoted(userID);
            save();
        }
    }
    
    //------ Detecting when meme is sent --------
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        String messageID = event.getMessageId();
        String userID = event.getAuthor().getId();
        String channelID = event.getChannel().getId();
        boolean isIgnored = false;
        String content = event.getMessage().getContent();
        MessageChannel channel = event.getChannel(); //creates channel object for the channel the reaction was placed in
        memeUser localUser = checkForUser(userID); //Checks if the user exists. If not creates the user. Then returns the user object
        
        
        //Checks if the channel should be ignored or not
        switch (channelID) {
            case "368142544548397068": isIgnored = true; break; //leaderboard channel
            case "368156446736646145": isIgnored = true; break; //mods chat
            case "379263169618771969": isIgnored = true; break; //general chat
            case "354624428144984065": isIgnored = true; break; //announcements
            case "378341574612090881": isIgnored = true; break; //info-and-rules
            case "379121238984884224": isIgnored = true; break;
            case "388422380680380417": isIgnored = true; break; //dev channel9
            default: isIgnored = false; break;
        }
        
        if (event.getAuthor().isBot() || isIgnored || (content.startsWith("[") && content.endsWith("]"))){ //Makes sure the user is not a bot, nor in a non meme channel, nor is not a meme.
            System.out.println("User is a bot/or the channel is ignored/or the message is not a meme");
        } else if(event.getMessage().getContent().equals("!getXP")) {
            int xp = localUser.getXP(memeList);
            channel.sendMessage("Your XP is: " + xp).queue();
        } else {

            uploadedMeme currentMeme = checkForMeme(messageID);

            currentMeme.setOwnerID(localUser.getUserID());//sets the owner of new meme object
            
            //create reaction buttons
            event.getMessage().addReaction("1⃣").queue();
            event.getMessage().addReaction("2⃣").queue();
            event.getMessage().addReaction("3⃣").queue();
            event.getMessage().addReaction("4⃣").queue();
            event.getMessage().addReaction("5⃣").queue();
            event.getMessage().addReaction("6⃣").queue();
            event.getMessage().addReaction("7⃣").queue();
            event.getMessage().addReaction("8⃣").queue();
            event.getMessage().addReaction("9⃣").queue();
            event.getMessage().addReaction("🔟").queue();
            
            
            //----------------------------
            // Creating the rating message
            //----------------------------
            
             channel.sendMessage("Dankness: 0 / 10").queue((Message t) -> {
                 currentMeme.setBotID(t.getId());
            });
            
            save();
        }
    }
}
