package com.example.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Steps implements Parcelable
{
    @SerializedName("id")
    private String mId;
    @SerializedName("shortDescription")
    private String mShortDescription;
    @SerializedName("description")
    private String mRecipeDescription;
    @SerializedName("videoURL")
    private String mVideoURL;
    @SerializedName("thumbnailURL")
    private String mThumbnailURL;

    protected Steps(Parcel in)
    {
        mId = in.readString();
        mShortDescription = in.readString();
        mRecipeDescription = in.readString();
        mVideoURL = in.readString();
        mThumbnailURL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>()
    {
        @Override
        public Steps createFromParcel(Parcel in)
        {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size)
        {
            return new Steps[size];
        }
    };

    @Override
    public String toString()
    {
        return "Steps{" +
                "mId='" + mId + '\'' +
                ", mShortDescription='" + mShortDescription + '\'' +
                ", mRecipeDescription='" + mRecipeDescription + '\'' +
                ", mVideoURL='" + mVideoURL + '\'' +
                ", mThumbnailURL='" + mThumbnailURL + '\'' +
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
        dest.writeString(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mRecipeDescription);
        dest.writeString(mVideoURL);
        dest.writeString(mThumbnailURL);
    }

    public String getmId()
    {
        return mId;
    }

    public String getmShortDescription()
    {
        return mShortDescription;
    }

    public String getmRecipeDescription()
    {
        return mRecipeDescription;
    }

    public String getmVideoURL()
    {
        return mVideoURL;
    }

    public String getmThumbnailURL()
    {
        return mThumbnailURL;
    }
}
