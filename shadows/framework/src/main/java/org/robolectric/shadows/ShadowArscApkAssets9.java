package org.robolectric.shadows;

import static org.robolectric.res.android.Errors.NO_ERROR;
import static org.robolectric.res.android.Util.ATRACE_NAME;
import static org.robolectric.res.android.Util.JNI_TRUE;

import android.os.Build;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.res.android.ApkAssets;
import org.robolectric.res.android.Asset;
import org.robolectric.res.android.ResXMLTree;
import org.robolectric.shadows.ShadowApkAssets.Picker;

// transliterated from
// https://android.googlesource.com/platform/frameworks/base/+/android-9.0.0_r3/core/jni/android_content_res_ApkAssets.cpp

@Implements(value = android.content.res.ApkAssets.class, minSdk = Build.VERSION_CODES.P,
    shadowPicker = Picker.class, isInAndroidSdk = false)
public class ShadowArscApkAssets9 extends ShadowApkAssets {
// #define ATRACE_TAG ATRACE_TAG_RESOURCES
//
// #include "android-base/macros.h"
// #include "android-base/stringprintf.h"
// #include "android-base/unique_fd.h"
// #include "androidfw/ApkAssets.h"
// #include "utils/misc.h"
// #include "utils/Trace.h"
//
// #include "core_jni_helpers.h"
// #include "jni.h"
// #include "nativehelper/ScopedUtfChars.h"
//
// using ::android::base::unique_fd;
//
// namespace android {

// static jlong NativeLoad(JNIEnv* env, jclass /*clazz*/, jstring java_path, jboolean system,
//                         jboolean force_shared_lib, jboolean overlay) {
  @Implementation
  protected static long nativeLoad(String java_path, boolean system,
      boolean force_shared_lib, boolean overlay) throws IOException {
    String path = java_path;
    if (path == null) {
      return 0;
    }

    ATRACE_NAME(String.format("LoadApkAssets(%s)", path));

    ApkAssets apk_assets;
    if (overlay) {
      apk_assets = ApkAssets.LoadOverlay(path, system);
    } else if (force_shared_lib) {
      apk_assets =
          ApkAssets.LoadAsSharedLibrary(path, system);
    } else {
      apk_assets = ApkAssets.Load(path, system);
    }

    if (apk_assets == null) {
      String error_msg = String.format("Failed to load asset path %s", path);
      throw new IOException(error_msg);
    }
    return ShadowArscAssetManager9.NATIVE_APK_ASSETS_REGISTRY.getNativeObjectId(apk_assets);
  }

  // static jlong NativeLoadFromFd(JNIEnv* env, jclass /*clazz*/, jobject file_descriptor,
//                               jstring friendly_name, jboolean system, jboolean force_shared_lib) {
  @Implementation
  protected static long nativeLoadFromFd(FileDescriptor file_descriptor,
      String friendly_name, boolean system, boolean force_shared_lib) {
    String friendly_name_utf8 = friendly_name;
    if (friendly_name_utf8 == null) {
      return 0;
    }

    throw new UnsupportedOperationException();
    // ATRACE_NAME(String.format("LoadApkAssetsFd(%s)", friendly_name_utf8));
    //
    // int fd = jniGetFDFromFileDescriptor(env, file_descriptor);
    // if (fd < 0) {
    //   throw new IllegalArgumentException("Bad FileDescriptor");
    // }
    //
    // unique_fd dup_fd(.dup(fd));
    // if (dup_fd < 0) {
    //   throw new IOException(errno);
    //   return 0;
    // }
    //
    // ApkAssets apk_assets = ApkAssets.LoadFromFd(std.move(dup_fd),
    //                                                                     friendly_name_utf8,
    //                                                                     system, force_shared_lib);
    // if (apk_assets == null) {
    //   String error_msg = String.format("Failed to load asset path %s from fd %d",
    //                                              friendly_name_utf8, dup_fd.get());
    //   throw new IOException(error_msg);
    //   return 0;
    // }
    // return ShadowArscAssetManager9.NATIVE_APK_ASSETS_REGISTRY.getNativeObjectId(apk_assets);
  }

  // static void NativeDestroy(JNIEnv* /*env*/, jclass /*clazz*/, jlong ptr) {
  @Implementation
  protected static void nativeDestroy(long ptr) {
    // delete reinterpret_cast<ApkAssets>(ptr);
    ShadowArscAssetManager9.NATIVE_APK_ASSETS_REGISTRY.unregister(ptr);
  }

  // static jstring NativeGetAssetPath(JNIEnv* env, jclass /*clazz*/, jlong ptr) {
  @Implementation
  protected static String nativeGetAssetPath(long ptr) {
    ApkAssets apk_assets = ShadowArscAssetManager9.NATIVE_APK_ASSETS_REGISTRY.getNativeObject(ptr);
    return apk_assets.GetPath();
  }

  // static jlong NativeGetStringBlock(JNIEnv* /*env*/, jclass /*clazz*/, jlong ptr) {
  @Implementation
  protected static long nativeGetStringBlock(long ptr) {
    ApkAssets apk_assets = ShadowArscAssetManager9.NATIVE_APK_ASSETS_REGISTRY.getNativeObject(ptr);
    return ShadowStringBlock.getNativePointer(apk_assets.GetLoadedArsc().GetStringPool());
  }

  // static jboolean NativeIsUpToDate(JNIEnv* /*env*/, jclass /*clazz*/, jlong ptr) {
  @Implementation
  protected static boolean nativeIsUpToDate(long ptr) {
    ApkAssets apk_assets = ShadowArscAssetManager9.NATIVE_APK_ASSETS_REGISTRY.getNativeObject(ptr);
    // (void)apk_assets;
    return JNI_TRUE;
  }

  // static jlong NativeOpenXml(JNIEnv* env, jclass /*clazz*/, jlong ptr, jstring file_name) {
  @Implementation
  protected static long nativeOpenXml(long ptr, String file_name) throws FileNotFoundException {
    String path_utf8 = file_name;
    if (path_utf8 == null) {
      return 0;
    }

    ApkAssets apk_assets =
        ShadowArscAssetManager9.NATIVE_APK_ASSETS_REGISTRY.getNativeObject(ptr);
    Asset asset = apk_assets.Open(path_utf8,
        Asset.AccessMode.ACCESS_RANDOM);
    if (asset == null) {
      throw new FileNotFoundException(path_utf8);
    }

    // DynamicRefTable is only needed when looking up resource references. Opening an XML file
    // directly from an ApkAssets has no notion of proper resource references.
    ResXMLTree xml_tree = new ResXMLTree(null); // util.make_unique<ResXMLTree>(nullptr /*dynamicRefTable*/);
    int err = xml_tree.setTo(asset.getBuffer(true), (int) asset.getLength(), true);
    // asset.reset();

    if (err != NO_ERROR) {
      throw new FileNotFoundException("Corrupt XML binary file");
    }
    return ShadowXmlBlock.NATIVE_RES_XML_TREES.getNativeObjectId(xml_tree); // reinterpret_cast<jlong>(xml_tree.release());
  }

// // JNI registration.
// static const JNINativeMethod gApkAssetsMethods[] = {
//     {"nativeLoad", "(Ljava/lang/String;ZZZ)J", (void*)NativeLoad},
//     {"nativeLoadFromFd", "(Ljava/io/FileDescriptor;Ljava/lang/String;ZZ)J",
//         (void*)NativeLoadFromFd},
//     {"nativeDestroy", "(J)V", (void*)NativeDestroy},
//     {"nativeGetAssetPath", "(J)Ljava/lang/String;", (void*)NativeGetAssetPath},
//     {"nativeGetStringBlock", "(J)J", (void*)NativeGetStringBlock},
//     {"nativeIsUpToDate", "(J)Z", (void*)NativeIsUpToDate},
//     {"nativeOpenXml", "(JLjava/lang/String;)J", (void*)NativeOpenXml},
// };
//
// int register_android_content_res_ApkAssets(JNIEnv* env) {
//   return RegisterMethodsOrDie(env, "android/content/res/ApkAssets", gApkAssetsMethods,
//                               arraysize(gApkAssetsMethods));
// }
//
// }  // namespace android
}