package com.example.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe implements Parcelable
{
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("ingredients")
    private List<Ingredient> mIngredients;
    @SerializedName("steps")
    private List<Steps> mSteps;
    @SerializedName("servings")
    private int mServing;
    @SerializedName("image")
    private String mImage;

    protected Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mServing = in.readInt();
        mImage = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
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
    public String toString() {
        return "Recipe{" + "mId=" + mId + ", mName='" + mName + '\'' + ", mIngredients=" + mIngredients +
                ", mSteps=" + mSteps + ", mServing=" + mServing + ", mImage='" + mImage + '\'' + '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeInt(mServing);
        parcel.writeString(mImage);
    }
}
