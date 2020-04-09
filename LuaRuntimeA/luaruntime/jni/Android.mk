# C/C++11 not support with Android SDK 20 or early
# Lua 5.1.5 not support with Android SDK 20 or early
# NDK version for SDK 19 doesn't implement the whole C++11 standard in the STL.
# 'locale.h' header has stubs for 'localeconv()' method,
# but the library doesn't implement it.
# The closest Android SDK that implement 'localeconv()' is SDK 21.
# This is implicitly stated in the header '<locale.h>'

LOCAL_PATH              := $(call my-dir)
LOCAL_SHORT_COMMANDS    := true

# ------------------ Building Lua ------------------------------
include $(CLEAR_VARS)
LOCAL_MODULE        := lua
LOCAL_C_INCLUDES    := $(LOCAL_PATH)/lua

LOCAL_CPPFLAGS      := -O2 -std=c++11
LOCAL_CFLAGS        := -O2 -std=c11

LOCAL_SRC_FILES := lua/lapi.c \
                   lua/lcode.c \
                   lua/ldebug.c \
                   lua/ldo.c \
                   lua/ldump.c \
                   lua/lfunc.c \
                   lua/lgc.c \
                   lua/llex.c \
                   lua/lmem.c \
                   lua/lobject.c \
                   lua/lopcodes.c \
                   lua/lparser.c \
                   lua/lstate.c \
                   lua/lstring.c \
                   lua/ltable.c \
                   lua/ltm.c \
                   lua/lundump.c \
                   lua/lvm.c \
                   lua/lzio.c \
                   lua/lauxlib.c \
                   lua/lbaselib.c \
                   lua/ldblib.c \
                   lua/liolib.c \
                   lua/lmathlib.c \
                   lua/loslib.c \
                   lua/ltablib.c \
                   lua/lstrlib.c \
                   lua/loadlib.c \
                   lua/linit.c

include $(BUILD_STATIC_LIBRARY)

# ------------------ Building Lua Runtime ------------------------------
include $(CLEAR_VARS)
LOCAL_PRELINK_MODULE    := false

LOCAL_MODULE            := luaruntime

LOCAL_C_INCLUDES        := $(LOCAL_PATH)/lua
LOCAL_C_INCLUDES        += $(LOCAL_PATH)/util
LOCAL_CFLAGS            := -O2 -std=c11 -DANDROID_NDK
LOCAL_CPPFLAGS          := -O2 -std=c++11 -DANDROID_NDK
#LOCAL_LDLIBS 	        := -landroid -ldl
LOCAL_STATIC_LIBRARIES  := lua

LOCAL_SRC_FILES         := util/tolua.c \
					       util/int64.c \
					       util/uint64.c

include $(BUILD_SHARED_LIBRARY)