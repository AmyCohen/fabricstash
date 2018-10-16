package com.amycohen.fabricstash.models;

public class Fabric {

    //Where was it purchased
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
    public Fabric (String fabricName, String fabricType, String fiberContent, float quantity, String unit,  String imageUrl, boolean custom, String company, boolean bst, String bstPerson, boolean washed, String detergeant, float pricePaid, boolean usedOnProject, String projectName) {

        //Where was it purchased
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

}
