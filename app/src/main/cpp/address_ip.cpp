#include <jni.h>
#include <string>
#include <ifaddrs.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <android/log.h>

extern "C" __attribute__((unused)) JNIEXPORT jstring JNICALL
Java_com_example_programmingipaddresstest_MainActivity_getAddressIP(JNIEnv* env, jobject) {
    struct ifaddrs *ifaddr, *ifa;
    char ip[INET6_ADDRSTRLEN];
    std::string result;

    if (getifaddrs(&ifaddr) == -1) {
        perror("getifaddrs");
        return env->NewStringUTF("");
    }

    for (ifa = ifaddr; ifa != nullptr; ifa = ifa->ifa_next) {
        if (ifa->ifa_addr == nullptr) continue;

        int family = ifa->ifa_addr->sa_family;
        if (family == AF_INET) {
            struct sockaddr_in *addr = (struct sockaddr_in *)ifa->ifa_addr;
            if (inet_ntop(family, &addr->sin_addr, ip, sizeof(ip))) {
                std::string address(ip);
                if (address.find("192.168.") != 0 && address.find("10.") != 0 && address.find("172.16.") != 0) {
                    result = address;
                    break;
                }
            }
        } else if (family == AF_INET6) {
            struct sockaddr_in6 *addr = (struct sockaddr_in6 *)ifa->ifa_addr;
            if (inet_ntop(family, &addr->sin6_addr, ip, sizeof(ip))) {
                if (addr->sin6_addr.s6_addr[0] == 0x20) {
                    result = ip;
                    break;
                }
            }
        }
    }

    freeifaddrs(ifaddr);

    if (result.empty()) {
        for (ifa = ifaddr; ifa != nullptr; ifa = ifa->ifa_next) {
            if (ifa->ifa_addr == nullptr) continue;

            int family = ifa->ifa_addr->sa_family;
            if (family == AF_INET) {
                struct sockaddr_in *addr = (struct sockaddr_in *)ifa->ifa_addr;
                if (inet_ntop(family, &addr->sin_addr, ip, sizeof(ip))) {
                    result = ip;
                    break;
                }
            }
        }
    }

    return env->NewStringUTF(result.c_str());
}
