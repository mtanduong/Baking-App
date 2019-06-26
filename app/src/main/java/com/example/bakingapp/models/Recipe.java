package com.example.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe implements Parcelable
{
    //recipe id
    @SerializedName("id")
    private int mId;

    //recipe name
    @SerializedName("name")
    private String mName;

    //recipe ingredients,will map an array of ingredients
    @SerializedName("ingredients")
    private List<Ingredient> mIngredients;

    //recipe steps,will map an array of steps to make the recipe
    @SerializedName("steps")
    private List<Steps> mSteps;

    //number of servings
    @SerializedName("servings")
    private int mServing;

    //image url
    @SerializedName("image")
    private String mImage;



    protected Recipe(Parcel in)
    {
        mId = in.readInt();
        mName = in.readString();
        mServing = in.readInt();
        mImage = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>()
    {
        @Override
        public Recipe createFromParcel(Parcel in)
        {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size)
        {
            return new Recipe[size];
        }
    };

    public int getmId()
    {
        return mId;
    }

    public String getmName()
    {
        return mName;
    }

    public List<Ingredient> getmIngredients()
    {
        return mIngredients;
    }

    public List<Steps> getmSteps()
    {
        return mSteps;
    }

    public int getmServings()
    {
        return mServing;
    }

    public String getmImage()
    {
        return mImage;
    }

    @Override
    public String toString()
    {
        return "Recipe{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mIngredients=" + mIngredients +
                ", mSteps=" + mSteps +
                ", mServing=" + mServing +
                ", mImage='" + mImage + '\'' +
                '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeInt(mServing);
        dest.writeString(mImage);
    }
}
