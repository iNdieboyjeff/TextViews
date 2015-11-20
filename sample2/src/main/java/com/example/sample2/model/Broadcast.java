
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
public class Broadcast implements Parcelable
{

    public boolean expanded = false;

    @SerializedName("is_repeat")
    @Expose
    private boolean isRepeat;
    @SerializedName("is_blanked")
    @Expose
    private boolean isBlanked;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("programme")
    @Expose
    private Programme programme;
    public final static Creator<Broadcast> CREATOR = new Creator<Broadcast>() {


        public Broadcast createFromParcel(Parcel in) {
            Broadcast instance = new Broadcast();
            instance.isRepeat = ((boolean) in.readValue((boolean.class.getClassLoader())));
            instance.isBlanked = ((boolean) in.readValue((boolean.class.getClassLoader())));
            instance.pid = ((String) in.readValue((String.class.getClassLoader())));
            instance.start = ((String) in.readValue((String.class.getClassLoader())));
            instance.end = ((String) in.readValue((String.class.getClassLoader())));
            instance.duration = ((int) in.readValue((int.class.getClassLoader())));
            instance.programme = ((Programme) in.readValue((Programme.class.getClassLoader())));
            return instance;
        }

        public Broadcast[] newArray(int size) {
            return (new Broadcast[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The isRepeat
     */
    public boolean isIsRepeat() {
        return isRepeat;
    }

    /**
     * 
     * @param isRepeat
     *     The is_repeat
     */
    public void setIsRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    /**
     * 
     * @return
     *     The isBlanked
     */
    public boolean isIsBlanked() {
        return isBlanked;
    }

    /**
     * 
     * @param isBlanked
     *     The is_blanked
     */
    public void setIsBlanked(boolean isBlanked) {
        this.isBlanked = isBlanked;
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
     *     The start
     */
    public String getStart() {
        return start;
    }

    /**
     * 
     * @param start
     *     The start
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * 
     * @return
     *     The end
     */
    public String getEnd() {
        return end;
    }

    /**
     * 
     * @param end
     *     The end
     */
    public void setEnd(String end) {
        this.end = end;
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
     *     The programme
     */
    public Programme getProgramme() {
        return programme;
    }

    /**
     * 
     * @param programme
     *     The programme
     */
    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(isRepeat);
        dest.writeValue(isBlanked);
        dest.writeValue(pid);
        dest.writeValue(start);
        dest.writeValue(end);
        dest.writeValue(duration);
        dest.writeValue(programme);
    }

    public int describeContents() {
        return  0;
    }

}
