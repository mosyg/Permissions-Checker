// IRemoteScanner.aidl
package edu.uiuc.permissionscheckernoui;


interface IRemoteScanner {
    //the only thing it does
    List<String> scan(String datain);
}

