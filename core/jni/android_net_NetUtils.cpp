/*
 * Copyright 2008, The Android Open Source Project
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

#define LOG_TAG "NetUtils"

#include "jni.h"
#include <utils/misc.h>
#include <android_runtime/AndroidRuntime.h>
#include <utils/Log.h>
#include <arpa/inet.h>
<<<<<<< HEAD
#include <cutils/properties.h>

extern "C" {
int ifc_enable(const char *ifname);
int ifc_disable(const char *ifname);
int ifc_reset_connections(const char *ifname, int reset_mask);

int dhcp_do_request(const char *ifname,
                    const char *ipaddr,
                    const char *gateway,
                    uint32_t *prefixLength,
                    const char *dns1,
                    const char *dns2,
                    const char *server,
                    uint32_t *lease,
                    const char *vendorInfo);

int dhcp_do_request_renew(const char *ifname,
                    const char *ipaddr,
                    const char *gateway,
                    uint32_t *prefixLength,
                    const char *dns1,
                    const char *dns2,
                    const char *server,
                    uint32_t *lease,
                    const char *vendorInfo);

int dhcp_stop(const char *ifname);
int dhcp_release_lease(const char *ifname);
=======

extern "C" {
int ifc_disable(const char *ifname);
int ifc_add_host_route(const char *ifname, uint32_t addr);
int ifc_remove_host_routes(const char *ifname);
int ifc_set_default_route(const char *ifname, uint32_t gateway);
int ifc_get_default_route(const char *ifname);
int ifc_remove_default_route(const char *ifname);
int ifc_reset_connections(const char *ifname);
int ifc_configure(const char *ifname, in_addr_t ipaddr, in_addr_t netmask, in_addr_t gateway, in_addr_t dns1, in_addr_t dns2);

int dhcp_do_request(const char *ifname,
                    in_addr_t *ipaddr,
                    in_addr_t *gateway,
                    in_addr_t *mask,
                    in_addr_t *dns1,
                    in_addr_t *dns2,
                    in_addr_t *server,
                    uint32_t  *lease);
int dhcp_stop(const char *ifname);
>>>>>>> 54b6cfa... Initial Contribution
char *dhcp_get_errmsg();
}

#define NETUTILS_PKG_NAME "android/net/NetworkUtils"

namespace android {

/*
 * The following remembers the jfieldID's of the fields
 * of the DhcpInfo Java object, so that we don't have
 * to look them up every time.
 */
static struct fieldIds {
<<<<<<< HEAD
    jmethodID constructorId;
    jfieldID ipaddress;
    jfieldID prefixLength;
=======
    jclass dhcpInfoClass;
    jmethodID constructorId;
    jfieldID ipaddress;
    jfieldID gateway;
    jfieldID netmask;
>>>>>>> 54b6cfa... Initial Contribution
    jfieldID dns1;
    jfieldID dns2;
    jfieldID serverAddress;
    jfieldID leaseDuration;
<<<<<<< HEAD
    jfieldID vendorInfo;
} dhcpInfoInternalFieldIds;

static jint android_net_utils_enableInterface(JNIEnv* env, jobject clazz, jstring ifname)
=======
} dhcpInfoFieldIds;

static jint android_net_utils_disableInterface(JNIEnv* env, jobject clazz, jstring ifname)
>>>>>>> 54b6cfa... Initial Contribution
{
    int result;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
<<<<<<< HEAD
    result = ::ifc_enable(nameStr);
=======
    result = ::ifc_disable(nameStr);
>>>>>>> 54b6cfa... Initial Contribution
    env->ReleaseStringUTFChars(ifname, nameStr);
    return (jint)result;
}

<<<<<<< HEAD
static jint android_net_utils_disableInterface(JNIEnv* env, jobject clazz, jstring ifname)
=======
static jint android_net_utils_addHostRoute(JNIEnv* env, jobject clazz, jstring ifname, jint addr)
>>>>>>> 54b6cfa... Initial Contribution
{
    int result;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
<<<<<<< HEAD
    result = ::ifc_disable(nameStr);
=======
    result = ::ifc_add_host_route(nameStr, addr);
>>>>>>> 54b6cfa... Initial Contribution
    env->ReleaseStringUTFChars(ifname, nameStr);
    return (jint)result;
}

<<<<<<< HEAD
static jint android_net_utils_resetConnections(JNIEnv* env, jobject clazz,
      jstring ifname, jint mask)
=======
static jint android_net_utils_removeHostRoutes(JNIEnv* env, jobject clazz, jstring ifname)
>>>>>>> 54b6cfa... Initial Contribution
{
    int result;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
<<<<<<< HEAD

    ALOGD("android_net_utils_resetConnections in env=%p clazz=%p iface=%s mask=0x%x\n",
          env, clazz, nameStr, mask);

    result = ::ifc_reset_connections(nameStr, mask);
=======
    result = ::ifc_remove_host_routes(nameStr);
    env->ReleaseStringUTFChars(ifname, nameStr);
    return (jint)result;
}

static jint android_net_utils_setDefaultRoute(JNIEnv* env, jobject clazz, jstring ifname, jint gateway)
{
    int result;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
    result = ::ifc_set_default_route(nameStr, gateway);
>>>>>>> 54b6cfa... Initial Contribution
    env->ReleaseStringUTFChars(ifname, nameStr);
    return (jint)result;
}

<<<<<<< HEAD
static jboolean android_net_utils_runDhcpCommon(JNIEnv* env, jobject clazz, jstring ifname,
        jobject info, bool renew)
{
    int result;
    char  ipaddr[PROPERTY_VALUE_MAX];
    uint32_t prefixLength;
    char gateway[PROPERTY_VALUE_MAX];
    char    dns1[PROPERTY_VALUE_MAX];
    char    dns2[PROPERTY_VALUE_MAX];
    char  server[PROPERTY_VALUE_MAX];
    uint32_t lease;
    char vendorInfo[PROPERTY_VALUE_MAX];

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
    if (nameStr == NULL) return (jboolean)false;

    if (renew) {
        result = ::dhcp_do_request_renew(nameStr, ipaddr, gateway, &prefixLength,
                dns1, dns2, server, &lease, vendorInfo);
    } else {
        result = ::dhcp_do_request(nameStr, ipaddr, gateway, &prefixLength,
                dns1, dns2, server, &lease, vendorInfo);
    }

    env->ReleaseStringUTFChars(ifname, nameStr);
    if (result == 0) {
        env->SetObjectField(info, dhcpInfoInternalFieldIds.ipaddress, env->NewStringUTF(ipaddr));

        // set the gateway
        jclass cls = env->FindClass("java/net/InetAddress");
        jmethodID method = env->GetStaticMethodID(cls, "getByName",
                "(Ljava/lang/String;)Ljava/net/InetAddress;");
        jvalue args[1];
        args[0].l = env->NewStringUTF(gateway);
        jobject inetAddressObject = env->CallStaticObjectMethodA(cls, method, args);

        if (!env->ExceptionOccurred()) {
            cls = env->FindClass("android/net/RouteInfo");
            method = env->GetMethodID(cls, "<init>", "(Ljava/net/InetAddress;)V");
            args[0].l = inetAddressObject;
            jobject routeInfoObject = env->NewObjectA(cls, method, args);

            cls = env->FindClass("android/net/DhcpInfoInternal");
            method = env->GetMethodID(cls, "addRoute", "(Landroid/net/RouteInfo;)V");
            args[0].l = routeInfoObject;
            env->CallVoidMethodA(info, method, args);
        } else {
            // if we have an exception (host not found perhaps), just don't add the route
            env->ExceptionClear();
        }

        env->SetIntField(info, dhcpInfoInternalFieldIds.prefixLength, prefixLength);
        env->SetObjectField(info, dhcpInfoInternalFieldIds.dns1, env->NewStringUTF(dns1));
        env->SetObjectField(info, dhcpInfoInternalFieldIds.dns2, env->NewStringUTF(dns2));
        env->SetObjectField(info, dhcpInfoInternalFieldIds.serverAddress,
                env->NewStringUTF(server));
        env->SetIntField(info, dhcpInfoInternalFieldIds.leaseDuration, lease);
        env->SetObjectField(info, dhcpInfoInternalFieldIds.vendorInfo, env->NewStringUTF(vendorInfo));
    }
    return (jboolean)(result == 0);
}

static jboolean android_net_utils_runDhcp(JNIEnv* env, jobject clazz, jstring ifname, jobject info)
{
    return android_net_utils_runDhcpCommon(env, clazz, ifname, info, false);
}

static jboolean android_net_utils_runDhcpRenew(JNIEnv* env, jobject clazz, jstring ifname, jobject info)
{
    return android_net_utils_runDhcpCommon(env, clazz, ifname, info, true);
}


static jboolean android_net_utils_stopDhcp(JNIEnv* env, jobject clazz, jstring ifname)
{
    int result;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
    result = ::dhcp_stop(nameStr);
    env->ReleaseStringUTFChars(ifname, nameStr);
    return (jboolean)(result == 0);
}

static jboolean android_net_utils_releaseDhcpLease(JNIEnv* env, jobject clazz, jstring ifname)
=======
static jint android_net_utils_getDefaultRoute(JNIEnv* env, jobject clazz, jstring ifname)
{
    int result;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
    result = ::ifc_get_default_route(nameStr);
    env->ReleaseStringUTFChars(ifname, nameStr);
    return (jint)result;
}

static jint android_net_utils_removeDefaultRoute(JNIEnv* env, jobject clazz, jstring ifname)
{
    int result;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
    result = ::ifc_remove_default_route(nameStr);
    env->ReleaseStringUTFChars(ifname, nameStr);
    return (jint)result;
}

static jint android_net_utils_resetConnections(JNIEnv* env, jobject clazz, jstring ifname)
{
    int result;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
    result = ::ifc_reset_connections(nameStr);
    env->ReleaseStringUTFChars(ifname, nameStr);
    return (jint)result;
}

static jboolean android_net_utils_runDhcp(JNIEnv* env, jobject clazz, jstring ifname, jobject info)
{
    int result;
    in_addr_t ipaddr, gateway, mask, dns1, dns2, server;
    uint32_t lease;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
    result = ::dhcp_do_request(nameStr, &ipaddr, &gateway, &mask,
                                        &dns1, &dns2, &server, &lease);
    env->ReleaseStringUTFChars(ifname, nameStr);
    if (result == 0 && dhcpInfoFieldIds.dhcpInfoClass != NULL) {
        env->SetIntField(info, dhcpInfoFieldIds.ipaddress, ipaddr);
        env->SetIntField(info, dhcpInfoFieldIds.gateway, gateway);
        env->SetIntField(info, dhcpInfoFieldIds.netmask, mask);
        env->SetIntField(info, dhcpInfoFieldIds.dns1, dns1);
        env->SetIntField(info, dhcpInfoFieldIds.dns2, dns2);
        env->SetIntField(info, dhcpInfoFieldIds.serverAddress, server);
        env->SetIntField(info, dhcpInfoFieldIds.leaseDuration, lease);
    }
    return (jboolean)(result == 0);
}

static jboolean android_net_utils_stopDhcp(JNIEnv* env, jobject clazz, jstring ifname, jobject info)
>>>>>>> 54b6cfa... Initial Contribution
{
    int result;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
<<<<<<< HEAD
    result = ::dhcp_release_lease(nameStr);
=======
    result = ::dhcp_stop(nameStr);
>>>>>>> 54b6cfa... Initial Contribution
    env->ReleaseStringUTFChars(ifname, nameStr);
    return (jboolean)(result == 0);
}

static jstring android_net_utils_getDhcpError(JNIEnv* env, jobject clazz)
{
    return env->NewStringUTF(::dhcp_get_errmsg());
}

<<<<<<< HEAD
=======
static jboolean android_net_utils_configureInterface(JNIEnv* env,
        jobject clazz,
        jstring ifname,
        jint ipaddr,
        jint mask,
        jint gateway,
        jint dns1,
        jint dns2)
{
    int result;
    uint32_t lease;

    const char *nameStr = env->GetStringUTFChars(ifname, NULL);
    result = ::ifc_configure(nameStr, ipaddr, mask, gateway, dns1, dns2);
    env->ReleaseStringUTFChars(ifname, nameStr);
    return (jboolean)(result == 0);
}

>>>>>>> 54b6cfa... Initial Contribution
// ----------------------------------------------------------------------------

/*
 * JNI registration.
 */
static JNINativeMethod gNetworkUtilMethods[] = {
    /* name, signature, funcPtr */

<<<<<<< HEAD
    { "enableInterface", "(Ljava/lang/String;)I",  (void *)android_net_utils_enableInterface },
    { "disableInterface", "(Ljava/lang/String;)I",  (void *)android_net_utils_disableInterface },
    { "resetConnections", "(Ljava/lang/String;I)I",  (void *)android_net_utils_resetConnections },
    { "runDhcp", "(Ljava/lang/String;Landroid/net/DhcpInfoInternal;)Z",  (void *)android_net_utils_runDhcp },
    { "runDhcpRenew", "(Ljava/lang/String;Landroid/net/DhcpInfoInternal;)Z",  (void *)android_net_utils_runDhcpRenew },
    { "stopDhcp", "(Ljava/lang/String;)Z",  (void *)android_net_utils_stopDhcp },
    { "releaseDhcpLease", "(Ljava/lang/String;)Z",  (void *)android_net_utils_releaseDhcpLease },
=======
    { "disableInterface", "(Ljava/lang/String;)I",  (void *)android_net_utils_disableInterface },
    { "addHostRoute", "(Ljava/lang/String;I)I",  (void *)android_net_utils_addHostRoute },
    { "removeHostRoutes", "(Ljava/lang/String;)I",  (void *)android_net_utils_removeHostRoutes },
    { "setDefaultRoute", "(Ljava/lang/String;I)I",  (void *)android_net_utils_setDefaultRoute },
    { "getDefaultRoute", "(Ljava/lang/String;)I",  (void *)android_net_utils_getDefaultRoute },
    { "removeDefaultRoute", "(Ljava/lang/String;)I",  (void *)android_net_utils_removeDefaultRoute },
    { "resetConnections", "(Ljava/lang/String;)I",  (void *)android_net_utils_resetConnections },
    { "runDhcp", "(Ljava/lang/String;Landroid/net/DhcpInfo;)Z",  (void *)android_net_utils_runDhcp },
    { "stopDhcp", "(Ljava/lang/String;)Z",  (void *)android_net_utils_stopDhcp },
    { "configureNative", "(Ljava/lang/String;IIIII)Z",  (void *)android_net_utils_configureInterface },
>>>>>>> 54b6cfa... Initial Contribution
    { "getDhcpError", "()Ljava/lang/String;", (void*) android_net_utils_getDhcpError },
};

int register_android_net_NetworkUtils(JNIEnv* env)
{
<<<<<<< HEAD
    jclass dhcpInfoInternalClass = env->FindClass("android/net/DhcpInfoInternal");
    LOG_FATAL_IF(dhcpInfoInternalClass == NULL, "Unable to find class android/net/DhcpInfoInternal");
    dhcpInfoInternalFieldIds.constructorId = env->GetMethodID(dhcpInfoInternalClass, "<init>", "()V");
    dhcpInfoInternalFieldIds.ipaddress = env->GetFieldID(dhcpInfoInternalClass, "ipAddress", "Ljava/lang/String;");
    dhcpInfoInternalFieldIds.prefixLength = env->GetFieldID(dhcpInfoInternalClass, "prefixLength", "I");
    dhcpInfoInternalFieldIds.dns1 = env->GetFieldID(dhcpInfoInternalClass, "dns1", "Ljava/lang/String;");
    dhcpInfoInternalFieldIds.dns2 = env->GetFieldID(dhcpInfoInternalClass, "dns2", "Ljava/lang/String;");
    dhcpInfoInternalFieldIds.serverAddress = env->GetFieldID(dhcpInfoInternalClass, "serverAddress", "Ljava/lang/String;");
    dhcpInfoInternalFieldIds.leaseDuration = env->GetFieldID(dhcpInfoInternalClass, "leaseDuration", "I");
    dhcpInfoInternalFieldIds.vendorInfo = env->GetFieldID(dhcpInfoInternalClass, "vendorInfo", "Ljava/lang/String;");
=======
    jclass netutils = env->FindClass(NETUTILS_PKG_NAME);
    LOG_FATAL_IF(netutils == NULL, "Unable to find class " NETUTILS_PKG_NAME);

    dhcpInfoFieldIds.dhcpInfoClass = env->FindClass("android/net/DhcpInfo");
    if (dhcpInfoFieldIds.dhcpInfoClass != NULL) {
        dhcpInfoFieldIds.constructorId = env->GetMethodID(dhcpInfoFieldIds.dhcpInfoClass, "<init>", "()V");
        dhcpInfoFieldIds.ipaddress = env->GetFieldID(dhcpInfoFieldIds.dhcpInfoClass, "ipAddress", "I");
        dhcpInfoFieldIds.gateway = env->GetFieldID(dhcpInfoFieldIds.dhcpInfoClass, "gateway", "I");
        dhcpInfoFieldIds.netmask = env->GetFieldID(dhcpInfoFieldIds.dhcpInfoClass, "netmask", "I");
        dhcpInfoFieldIds.dns1 = env->GetFieldID(dhcpInfoFieldIds.dhcpInfoClass, "dns1", "I");
        dhcpInfoFieldIds.dns2 = env->GetFieldID(dhcpInfoFieldIds.dhcpInfoClass, "dns2", "I");
        dhcpInfoFieldIds.serverAddress = env->GetFieldID(dhcpInfoFieldIds.dhcpInfoClass, "serverAddress", "I");
        dhcpInfoFieldIds.leaseDuration = env->GetFieldID(dhcpInfoFieldIds.dhcpInfoClass, "leaseDuration", "I");
    }
>>>>>>> 54b6cfa... Initial Contribution

    return AndroidRuntime::registerNativeMethods(env,
            NETUTILS_PKG_NAME, gNetworkUtilMethods, NELEM(gNetworkUtilMethods));
}

}; // namespace android
