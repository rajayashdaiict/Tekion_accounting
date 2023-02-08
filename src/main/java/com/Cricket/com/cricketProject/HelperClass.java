package com.Cricket.com.cricketProject;

public class HelperClass {
    public static void swapTwoStrings(String a,String b){
        String c=a;
        a=b;
        b=c;
    }
    public static int generateOutcome(){
        int outcome;
        outcome=(int)Math.ceil(Math.random()*8-1);
        return outcome;
    }
//    public static void swapTwoObjects(WrapperClass obj1,WrapperClass obj2){
//        WrapperClass objTemp=obj1;
//        obj1=obj2;
//        obj2=objTemp;
//    }
}
