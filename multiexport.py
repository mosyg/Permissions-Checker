import os
import sys




def run():
    os.system("cp AndroidManifest.xml AndroidManifest.xml.backup") #backup for now
    manifest = open("AndroidManifest.xml", "rw")
    manifest_str = manifest.read()
    permissions = getPermissions(manifest_str)
    buildPartialManifest(manifest_str, [])


    manifest.close()
    #os.system("cp AndroidManifest.xml.backup AndroidManifest.xml") #backup for now


def getPermissions(manifest_str):
    permissions = []
    isreading = False
    for line in manifest_str.split("\n"):
        if line == "<!-- END-INDIVIDUAL-PERMISSION-CHECKING -->":
            isreading = False
        if isreading:
            name = line.split("android:name=\"")[1].split("\"/>")[0]
            name = name.replace("android.permission.","")
            name = name.replace("com.android.browser.permission.","")
            perm_entry = {}
            perm_entry["name"] = name
            perm_entry["line"] = line
            permissions.append(perm_entry)
        if line == "<!-- START-INDIVIDUAL-PERMISSION-CHECKING -->":
            isreading = True
    print permissions
    return permissions


def buildPartialManifest(manifest_str, permissions):
    lines = manifest_str.split("\n")
    out = []
    inindividual = False
    inremove = False
    for line in lines:
       if inremove:
          if line == "<!-- END-NOT-FOR-INDIVIDUAL-PACKAGES -->":
             inremove = False
       elif line == "<!-- START-NOT-FOR-INDIVIDUAL-PACKAGES -->":
          inremove = True
       elif inindividual:
          if line == "<!-- END-INDIVIDUAL-PERMISSION-CHECKING -->":
             inindividual = False
             for permission in permissions:
               out.append(permission["line"])
       elif line == "<!-- START-INDIVIDUAL-PERMISSION-CHECKING -->":
          inindividual = True
       else:
          out.append(line)
    outstr = "\n".join(out)
    print outstr





def build(name):
   os.system("ant release") 
   moveResult(name)


def makeExportFolder():
    #make sure we have this folder
    os.system("mkdir multiperms")





def moveResult(name):
    os.system("mv bin/PermissionsCheckerActivity-release-unsigned.apk multiperms/permissionschecker-%s.apk"%name)



if __name__ == "__main__":
    run()
