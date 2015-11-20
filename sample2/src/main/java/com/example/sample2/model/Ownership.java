
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
public class Ownership implements Parcelable
{

    @SerializedName("service")
    @Expose
    private Service_ service;
    public final static Creator<Ownership> CREATOR = new Creator<Ownership>() {


        public Ownership createFromParcel(Parcel in) {
            Ownership instance = new Ownership();
            instance.service = ((Service_) in.readValue((Service_.class.getClassLoader())));
            return instance;
        }

        public Ownership[] newArray(int size) {
            return (new Ownership[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The service
     */
    public Service_ getService() {
        return service;
    }

    /**
     * 
     * @param service
     *     The service
     */
    public void setService(Service_ service) {
        this.service = service;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(service);
    }

    public int describeContents() {
        return  0;
    }

}
