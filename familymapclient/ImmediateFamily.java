package com.jacob.family.map.project.anotherone.Helpers;


import com.jacob.family.map.project.anotherone.DataCache;

import java.util.LinkedList;

import Model.Person;

//gets each of the relationships for the given person
public class ImmediateFamily {

    public LinkedList<String[]> getImmediateFamily(Person person) {
        LinkedList<String[]> immediateFamily = new LinkedList<>();

        if(person.getFatherID() != null) {
            Person father = DataCache.getPersonById(person.getFatherID());
            String[] fatherInfo = {father.getPersonID(), father.getFirstName(), father.getLastName(), "Father", father.getGender()};
            immediateFamily.add( fatherInfo);
        }
        if(person.getMotherID() != null) {
            Person mother = DataCache.getPersonById(person.getMotherID());
            String[] motherInfo = {mother.getPersonID(), mother.getFirstName(), mother.getLastName(), "Mother", mother.getGender()};
            immediateFamily.add(motherInfo);
        }
        if(person.getSpouseID() != null) {
            Person spouse = DataCache.getPersonById(person.getSpouseID());
            String[] spouseInfo = {spouse.getPersonID(), spouse.getFirstName(), spouse.getLastName(), "Spouse", spouse.getGender()};
            immediateFamily.add( spouseInfo);
        }

        if(!DataCache.getChildren(person).isEmpty()) {
            Person child = DataCache.getChildren(person).elementAt(0);
            String[] childInfo = {child.getPersonID(), child.getFirstName(), child.getLastName(), "Child", child.getGender()};
            immediateFamily.add( childInfo);
        }
        return immediateFamily;
    }
}
