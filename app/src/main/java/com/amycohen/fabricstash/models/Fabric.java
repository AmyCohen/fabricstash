package com.amycohen.fabricstash.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class Fabric {

    //Where was it purchased
    public String id;
    public String uid;
    public String fabricName;
    public String fabricType;
    public String fiberContent;
    public float quantity;
    public String unit;
    public String imageUrl;

    //What was purchased
    public boolean custom;
    public String company;
    public boolean bst;
    public String bstPerson;

    //Fabric notes
    public boolean washed;
    public String detergeant;
    public float pricePaid;

    //Project notes
    public boolean usedOnProject;
    public String projectName;

    //Empty Constructor
    public Fabric() {}

    //Constructor
    public Fabric (String id, String uid, String fabricName, String fabricType, String fiberContent, float quantity, String unit,  String imageUrl, boolean custom, String company, boolean bst, String bstPerson, boolean washed, String detergeant, float pricePaid, boolean usedOnProject, String projectName) {

        //Where was it purchased
        this.id = id;
        this.uid = uid;
        this.fabricName = fabricName;
        this.fabricType = fabricType;
        this.fiberContent = fiberContent;
        this.quantity = quantity;
        this.unit = unit;
        this.imageUrl = imageUrl;

        //What was purchased
        this.custom = custom;
        this.company = company;
        this.bst = bst;
        this.bstPerson = bstPerson;

        //Fabric notes
        this.washed = washed;
        this.detergeant = detergeant;
        this.pricePaid = pricePaid;

        //Project notes
        this.usedOnProject = usedOnProject;
        this.projectName = projectName;
    }

    public static Fabric fromSnapshot(DataSnapshot snapshot) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        String uid = user.getUid();

        Fabric fabric = new Fabric();
        fabric.id = snapshot.getKey();
        fabric.uid = snapshot.child("uid").getValue(String.class);
        fabric.imageUrl = snapshot.child("imageUrl").getValue(String.class);
        fabric.fabricName = snapshot.child("fabricName").getValue(String.class);
        fabric.company = snapshot.child("fabricCompany").getValue(String.class);
        fabric.fabricType = snapshot.child("fabricType").getValue(String.class);
        return fabric;
    }

    /*
    EXAMPLE:


        public static Errand fromSnapshot(DataSnapshot snapshot) {
        Errand errand = new Errand();
        errand.id = snapshot.getKey();
        errand.description = snapshot.child("description").getValue(String.class);
        errand.isComplete = snapshot.child("isComplete").getValue(boolean.class);

        float startLatitude = snapshot.child("start").child("lat").getValue(float.class);
        float startLongitude = snapshot.child("start").child("long").getValue(float.class);
        errand.start = new LatLng(startLatitude, startLongitude);

        float endLatitude = snapshot.child("end").child("lat").getValue(float.class);
        float endLongitude = snapshot.child("end").child("long").getValue(float.class);
        errand.end = new LatLng(endLatitude, endLongitude);

        return errand;
    }

     */

}
