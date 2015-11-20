
/*
 *  Copyright (c) 2015 Jeff Sutton
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.sample2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Programme implements Parcelable
{

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("short_synopsis")
    @Expose
    private String shortSynopsis;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("display_titles")
    @Expose
    private DisplayTitles displayTitles;
    @SerializedName("first_broadcast_date")
    @Expose
    private String firstBroadcastDate;
    @SerializedName("ownership")
    @Expose
    private Ownership ownership;
    @SerializedName("programme")
    @Expose
    private Programme_ programme;
    @SerializedName("is_available_mediaset_pc_sd")
    @Expose
    private boolean isAvailableMediasetPcSd;
    @SerializedName("is_legacy_media")
    @Expose
    private boolean isLegacyMedia;
    public final static Creator<Programme> CREATOR = new Creator<Programme>() {


        public Programme createFromParcel(Parcel in) {
            Programme instance = new Programme();
            instance.type = ((String) in.readValue((String.class.getClassLoader())));
            instance.pid = ((String) in.readValue((String.class.getClassLoader())));
            instance.title = ((String) in.readValue((String.class.getClassLoader())));
            instance.shortSynopsis = ((String) in.readValue((String.class.getClassLoader())));
            instance.mediaType = ((String) in.readValue((String.class.getClassLoader())));
            instance.duration = ((int) in.readValue((int.class.getClassLoader())));
            instance.image = ((Image) in.readValue((Image.class.getClassLoader())));
            instance.displayTitles = ((DisplayTitles) in.readValue((DisplayTitles.class.getClassLoader())));
            instance.firstBroadcastDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.ownership = ((Ownership) in.readValue((Ownership.class.getClassLoader())));
            instance.programme = ((Programme_) in.readValue((Programme_.class.getClassLoader())));
            instance.isAvailableMediasetPcSd = ((boolean) in.readValue((boolean.class.getClassLoader())));
            instance.isLegacyMedia = ((boolean) in.readValue((boolean.class.getClassLoader())));
            return instance;
        }

        public Programme[] newArray(int size) {
            return (new Programme[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * 
     * @param pid
     *     The pid
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The shortSynopsis
     */
    public String getShortSynopsis() {
        return shortSynopsis;
    }

    /**
     * 
     * @param shortSynopsis
     *     The short_synopsis
     */
    public void setShortSynopsis(String shortSynopsis) {
        this.shortSynopsis = shortSynopsis;
    }

    /**
     * 
     * @return
     *     The mediaType
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * 
     * @param mediaType
     *     The media_type
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * 
     * @return
     *     The duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The image
     */
    public Image getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The displayTitles
     */
    public DisplayTitles getDisplayTitles() {
        return displayTitles;
    }

    /**
     * 
     * @param displayTitles
     *     The display_titles
     */
    public void setDisplayTitles(DisplayTitles displayTitles) {
        this.displayTitles = displayTitles;
    }

    /**
     * 
     * @return
     *     The firstBroadcastDate
     */
    public String getFirstBroadcastDate() {
        return firstBroadcastDate;
    }

    /**
     * 
     * @param firstBroadcastDate
     *     The first_broadcast_date
     */
    public void setFirstBroadcastDate(String firstBroadcastDate) {
        this.firstBroadcastDate = firstBroadcastDate;
    }

    /**
     * 
     * @return
     *     The ownership
     */
    public Ownership getOwnership() {
        return ownership;
    }

    /**
     * 
     * @param ownership
     *     The ownership
     */
    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

    /**
     * 
     * @return
     *     The programme
     */
    public Programme_ getProgramme() {
        return programme;
    }

    /**
     * 
     * @param programme
     *     The programme
     */
    public void setProgramme(Programme_ programme) {
        this.programme = programme;
    }

    /**
     * 
     * @return
     *     The isAvailableMediasetPcSd
     */
    public boolean isIsAvailableMediasetPcSd() {
        return isAvailableMediasetPcSd;
    }

    /**
     * 
     * @param isAvailableMediasetPcSd
     *     The is_available_mediaset_pc_sd
     */
    public void setIsAvailableMediasetPcSd(boolean isAvailableMediasetPcSd) {
        this.isAvailableMediasetPcSd = isAvailableMediasetPcSd;
    }

    /**
     * 
     * @return
     *     The isLegacyMedia
     */
    public boolean isIsLegacyMedia() {
        return isLegacyMedia;
    }

    /**
     * 
     * @param isLegacyMedia
     *     The is_legacy_media
     */
    public void setIsLegacyMedia(boolean isLegacyMedia) {
        this.isLegacyMedia = isLegacyMedia;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(type);
        dest.writeValue(pid);
        dest.writeValue(title);
        dest.writeValue(shortSynopsis);
        dest.writeValue(mediaType);
        dest.writeValue(duration);
        dest.writeValue(image);
        dest.writeValue(displayTitles);
        dest.writeValue(firstBroadcastDate);
        dest.writeValue(ownership);
        dest.writeValue(programme);
        dest.writeValue(isAvailableMediasetPcSd);
        dest.writeValue(isLegacyMedia);
    }

    public int describeContents() {
        return  0;
    }

}
