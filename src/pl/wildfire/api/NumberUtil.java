package pl.wildfire.api;

import org.apache.commons.lang.Validate;

import java.util.Random;

/**
 * Created by WildFire © 2015
 */
public class NumberUtil {

    public static Integer getInt(String s){
        try{
            return Integer.parseInt(s);
        }catch(NumberFormatException e){

        }
        return 0;
    }

    public static boolean isInt(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(NumberFormatException e){

        }
        return false;
    }

    public static boolean isDouble(String s){
        try{
            Double.parseDouble(s);
            return true;
        }catch(NumberFormatException e){

        }
        return false;
    }

    public static double getDouble(String s){
        try{
            return Double.parseDouble(s);
        }catch(NumberFormatException e){

        }
        return 0.0;
    }

    private static final Random rand = new Random();

    public static int getRandInt(int min, int max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return rand.nextInt(max - min + 1) + min;
    }

    public static double getRandDouble(double min, double max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return rand.nextDouble() * (max - min) + min;
    }

    public static float getRandFloat(float min, float max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return rand.nextFloat() * (max - min) + min;
    }

    public static boolean getChance(double chance) {
        return (chance >= 100.0) || (chance >= getRandDouble(0.0, 100.0));
    }
}
