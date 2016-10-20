package fr.p8.m2ise.contactsqlitedb;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by nizar on 12/10/2016.
 */

public class Contact implements Comparable<Contact>{
    String name;
    Long id ;
    int phoneNumber ;
    byte[] thumbnail;

    public Contact(){
    }
    public Contact(String n, int phoneNumber,Long id){
        this.name = n;
        this.phoneNumber = phoneNumber ;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String toString(){
        String toto  =
        "contact -> " + "nom: " + name + "\n id: " + id + "\ntelephone: " + phoneNumber;
        if(this.thumbnail != null){
            toto += "\n il y a une image";
        }
        return toto;
    }


    @Override
    public int compareTo(Contact contact) {
        return contact.getName().compareTo(this.name);
    }
}
