/**
 * Copyright (c) 2008, The Android Open Source Project
 *
<<<<<<< HEAD
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> 54b6cfa... Initial Contribution
 * limitations under the License.
 */

package android.net.wifi;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.ScanResult;
import android.net.DhcpInfo;

<<<<<<< HEAD
import android.os.Messenger;
import android.os.WorkSource;

=======
>>>>>>> 54b6cfa... Initial Contribution
/**
 * Interface that allows controlling and querying Wi-Fi connectivity.
 *
 * {@hide}
 */
interface IWifiManager
{
    List<WifiConfiguration> getConfiguredNetworks();
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    int addOrUpdateNetwork(in WifiConfiguration config);

    boolean removeNetwork(int netId);

    boolean enableNetwork(int netId, boolean disableOthers);

    boolean disableNetwork(int netId);

    boolean pingSupplicant();

<<<<<<< HEAD
    void startScan(boolean forceActive);

    List<ScanResult> getScanResults();

    void disconnect();

    void reconnect();

    void reassociate();
=======
    boolean startScan();

    List<ScanResult> getScanResults();

    boolean disconnect();

    boolean reconnect();
    
    boolean reassociate();
>>>>>>> 54b6cfa... Initial Contribution

    WifiInfo getConnectionInfo();

    boolean setWifiEnabled(boolean enable);

<<<<<<< HEAD
    int getWifiEnabledState();

    void setCountryCode(String country, boolean persist);

    void setFrequencyBand(int band, boolean persist);

    int getFrequencyBand();

    boolean isDualBandSupported();

=======
    int getWifiState();
    
>>>>>>> 54b6cfa... Initial Contribution
    boolean saveConfiguration();

    DhcpInfo getDhcpInfo();

<<<<<<< HEAD
    boolean acquireWifiLock(IBinder lock, int lockType, String tag, in WorkSource ws);

    void updateWifiLockWorkSource(IBinder lock, in WorkSource ws);

    boolean releaseWifiLock(IBinder lock);

    void initializeMulticastFiltering();

    boolean isMulticastEnabled();

    void acquireMulticastLock(IBinder binder, String tag);

    void releaseMulticastLock();

    void setWifiApEnabled(in WifiConfiguration wifiConfig, boolean enable);

    int getWifiApEnabledState();

    WifiConfiguration getWifiApConfiguration();

    void setWifiApConfiguration(in WifiConfiguration wifiConfig);

    void startWifi();

    void stopWifi();

    void addToBlacklist(String bssid);

    void clearBlacklist();

    Messenger getWifiServiceMessenger();

    Messenger getWifiStateMachineMessenger();

    String getConfigFile();
=======
    boolean acquireWifiLock(IBinder lock, String tag);

    boolean releaseWifiLock(IBinder lock);
>>>>>>> 54b6cfa... Initial Contribution
}

