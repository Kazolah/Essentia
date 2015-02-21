package com.essentia.hrm;

/**
 * Created by kyawzinlatt94 on 2/15/15.
 */
public class HRDeviceReference {
    public final String provider;
    public final String deviceName;
    public final String deviceAddress;

    private HRDeviceReference(final String provider, final String name, final String address) {
        this.provider = provider;
        this.deviceName = name;
        this.deviceAddress = address;
    }

    public static HRDeviceReference create(String providerName, String deviceName, String deviceAddress) {
        return new HRDeviceReference(providerName, deviceName, deviceAddress);
    }

    public String getProvider() {
        return provider;
    }

    public String getName() {
        if (deviceName != null && !"".contentEquals(deviceName))
            return deviceName;
        return deviceAddress;
    }

    public String getAddress() {
        return deviceAddress;
    }
}
