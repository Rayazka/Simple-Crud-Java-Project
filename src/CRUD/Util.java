package CRUD;

import java.util.Scanner;

import static java.lang.System.out;

public class Util {
    static final Scanner scan = new Scanner(System.in);


    //    Function tambahan
    public static void clearConsole(){
        try {
            if(System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            } else{
                out.println("\033\143");
            }
        } catch (Exception e){
            System.err.println(e);
        }
    }
    public static boolean getYes(String m){

        Scanner input = new Scanner(System.in);
        String inputUser;

        out.print("\n" + m +"? [y/n] : ");
        inputUser = input.nextLine();
        out.println();

        while(!inputUser.equalsIgnoreCase("y") && !inputUser.equalsIgnoreCase("n")){
            out.println( "Input yang anda masukkan tidak valid\nHarap masukkan jawaban dengan y atau n\n");
            out.print("\n" + m + "? [y/n] : ");
            inputUser = input.nextLine();
        }

        return inputUser.equalsIgnoreCase("y");
    }

}
