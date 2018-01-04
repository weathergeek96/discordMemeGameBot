/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memebot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.JDA;

private static JDA jda;

public class MemeBot {
   
   public static void main(String[] arguments) throws Exception {
       
       JDA jda = new JDABuilder(AccountType.BOT).setToken("MzU0NzUyNTkwMjc1MjgwODk3.DJC0pA.9FGlH-EZTShKwnfGGfMcyonmy8I").buildAsync();
       jda.setAutoReconnect(true);
       jda.addEventListener(new MyListener());
       MyListener.load();
   }
}
