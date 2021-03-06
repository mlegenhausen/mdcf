/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniluebeck.itm.mdcf.service;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * A simple object for retrieving the results of a DHCP request.
 */
public class DhcpInfo implements Parcelable {
    public int ipAddress;
    public int gateway;
    public int netmask;

    public int dns1;
    public int dns2;

    public int serverAddress;
    public int leaseDuration;

    public DhcpInfo() {
        super();
    }

    public String toString() {
        StringBuffer str = new StringBuffer();

        str.append("ipaddr "); putAddress(str, ipAddress);
        str.append(" gateway "); putAddress(str, gateway);
        str.append(" netmask "); putAddress(str, netmask);
        str.append(" dns1 "); putAddress(str, dns1);
        str.append(" dns2 "); putAddress(str, dns2);
        str.append(" DHCP server "); putAddress(str, serverAddress);
        str.append(" lease ").append(leaseDuration).append(" seconds");

        return str.toString();
    }

    private static void putAddress(StringBuffer buf, int addr) {
        buf.append(addr  & 0xff).append('.').
            append((addr >>>= 8) & 0xff).append('.').
            append((addr >>>= 8) & 0xff).append('.').
            append((addr >>>= 8) & 0xff);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ipAddress);
        dest.writeInt(gateway);
        dest.writeInt(netmask);
        dest.writeInt(dns1);
        dest.writeInt(dns2);
        dest.writeInt(serverAddress);
        dest.writeInt(leaseDuration);
    }

    public static final Creator<DhcpInfo> CREATOR =
        new Creator<DhcpInfo>() {
            public DhcpInfo createFromParcel(Parcel in) {
                DhcpInfo info = new DhcpInfo();
                info.ipAddress = in.readInt();
                info.gateway = in.readInt();
                info.netmask = in.readInt();
                info.dns1 = in.readInt();
                info.dns2 = in.readInt();
                info.serverAddress = in.readInt();
                info.leaseDuration = in.readInt();
                return info;
            }

            public DhcpInfo[] newArray(int size) {
                return new DhcpInfo[size];
            }
        };
}