package com.jacob.family.map.project.anotherone.Helpers;

//another singleton class for each of the settings objects
public class Setting {
    private static Setting instance = new Setting();

    private Setting() {}
    private static Setting getInstance() {
        return instance;
    }
    private static boolean lifeStoryLines = true;
    private static boolean familyTreeLines = true;
    private static boolean spouseLines = true;
    private static boolean fatherSide = true;
    private static boolean mothersSide = true;
    private static boolean male = true;
    private static boolean female = true;
    private static boolean change = false;

    public static void resetSettings() {
        setLifeStoryLines(true);
        setFamilyTreeLines(true);
        setSpouseLines(true);
        setFatherSide(true);
        setMothersSide(true);
        setMale(true);
        setFemale(true);
        setChange(false);
    }

    public static void setInstance(Setting instance) {
        Setting.instance = instance;
    }

    public static boolean isLifeStoryLines() {
        return lifeStoryLines;
    }

    public static void setLifeStoryLines(boolean lifeStoryLines) {
        Setting.lifeStoryLines = lifeStoryLines;
    }

    public static boolean isFamilyTreeLines() {
        return familyTreeLines;
    }

    public static void setFamilyTreeLines(boolean familyTreeLines) {
        Setting.familyTreeLines = familyTreeLines;
    }

    public static boolean isSpouseLines() {
        return spouseLines;
    }

    public static void setSpouseLines(boolean spouseLines) {
        Setting.spouseLines = spouseLines;
    }

    public static boolean isFatherSide() {
        return fatherSide;
    }

    public static void setFatherSide(boolean fatherSide) {
        Setting.fatherSide = fatherSide;
    }

    public static boolean isMothersSide() {
        return mothersSide;
    }

    public static void setMothersSide(boolean mothersSide) {
        Setting.mothersSide = mothersSide;
    }

    public static boolean isMale() {
        return male;
    }

    public static void setMale(boolean male) {
        Setting.male = male;
    }

    public static boolean isFemale() {
        return female;
    }

    public static void setFemale(boolean female) {
        Setting.female = female;
    }

    public static boolean isChange() {
        return change;
    }

    public static void setChange(boolean change) {
        Setting.change = change;
    }
}
