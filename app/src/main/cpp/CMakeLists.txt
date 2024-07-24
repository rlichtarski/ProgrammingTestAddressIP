# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

cmake_minimum_required(VERSION 3.22.1)

project("programmingipaddresstest")

add_library(address_ip SHARED address_ip.cpp)

find_library(log-lib log)

target_link_libraries(address_ip ${log-lib})