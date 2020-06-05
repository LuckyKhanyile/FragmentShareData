package co.za.spanbi.fragmentsharedata.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class DogBreed {
    @ColumnInfo(name = "breed_id")
    @SerializedName("id")
    public String dreedId;

    @ColumnInfo(name = "dog_name")
    @SerializedName("name")
    public String dogBreed;

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    public String lifeSpan;

    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    public String breedGroup;

    @ColumnInfo(name = "bred_for")
    @SerializedName("bred_for")
    public String bredFor;

    @SerializedName("temperament")
    public String Temperament;

    @SerializedName("url")
    public String imageUrl;

    @PrimaryKey(autoGenerate = true)
    public int uuid;

    public DogBreed(String dreedId, String dogBreed, String lifeSpan, String breedGroup, String bredFor, String temperament, String imageUrl) {
        this.dreedId = dreedId;
        this.dogBreed = dogBreed;
        this.lifeSpan = lifeSpan;
        this.breedGroup = breedGroup;
        this.bredFor = bredFor;
        Temperament = temperament;
        this.imageUrl = imageUrl;

    }
    public DogBreed(){}
}
//https://raw.githubusercontent.com/DevTides/DogsApi/master/dogs.json