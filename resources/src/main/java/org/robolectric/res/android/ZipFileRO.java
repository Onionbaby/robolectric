package org.robolectric.res.android;

import static org.robolectric.res.android.Errors.NAME_NOT_FOUND;
import static org.robolectric.res.android.Errors.NO_ERROR;
import static org.robolectric.res.android.Util.ALOGW;
import static org.robolectric.res.android.Util.isTruthy;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileRO {

  final int kCompressStored = 0;
  final int kCompressDeflated = 8;

  final ZipArchiveHandle mHandle;
  final String mFileName;

  ZipFileRO(ZipArchiveHandle handle, String fileName) {
    this.mHandle = handle;
    this.mFileName = fileName;
  }

  static class ZipEntryRO {
        ZipEntry entry;
    String name;
    Object cookie;

    ZipEntryRO() {
    }

    //    ~ZipEntryRO() {
    protected void finalize() {
//      EndIteration(cookie);
    }

//    private:
//    ZipEntryRO(final ZipEntryRO& other);
//    ZipEntryRO& operator=(final ZipEntryRO& other);
  };

//  ~ZipFileRO() {
  protected void finalize() {
    CloseArchive(mHandle);
//    free(mFileName);
  }

  private static int OpenArchive(String zipFileName, Ref<ZipArchiveHandle> mHandle) {
    try {
      mHandle.set(new ZipArchiveHandle(new ZipFile(zipFileName)));
      return NO_ERROR;
    } catch (IOException e) {
      return NAME_NOT_FOUND;
    }
  }

  private static void CloseArchive(ZipArchiveHandle mHandle) {
    throw new UnsupportedOperationException();
  }

  private static String ErrorCodeString(int error) {
    return "error " + error;
  }

  private int FindEntry(ZipArchiveHandle mHandle, String name, Ref<ZipEntry> zipEntryRef) {
    ZipEntry entry = mHandle.zipFile.getEntry(name);
    zipEntryRef.set(entry);
    if (entry == null) {
      return NAME_NOT_FOUND;
    }
    return NO_ERROR;
  }

  /*
 * Open the specified file read-only.  We memory-map the entire thing and
 * close the file before returning.
 */
/* static */
  static ZipFileRO open(final String zipFileName)
  {
    Ref<ZipArchiveHandle> handle = new Ref<>(null);
    final int error = OpenArchive(zipFileName, handle);
    if (isTruthy(error)) {
      ALOGW("Error opening archive %s: %s", zipFileName, ErrorCodeString(error));
      CloseArchive(handle.get());
      return null;
    }

    return new ZipFileRO(handle.get(), zipFileName);
  }

  org.robolectric.res.android.ZipFileRO.ZipEntryRO findEntryByName(final String entryName)
  {
    ZipEntryRO data = new ZipEntryRO();

    data.name = String(entryName);

    Ref<ZipEntry> zipEntryRef = new Ref<>(data.entry);
    final int error = FindEntry(mHandle, data.name, zipEntryRef);
    if (isTruthy(error)) {
      return null;
    }

    data.entry = zipEntryRef.get();
    return data;
  }

  /*
   * Get the useful fields from the zip entry.
   *
   * Returns "false" if the offsets to the fields or the contents of the fields
   * appear to be bogus.
   */
  boolean getEntryInfo(org.robolectric.res.android.ZipFileRO.ZipEntryRO entry, Ref<Short> pMethod,
      Ref<Long> pUncompLen, Ref<Long> pCompLen, Ref<Long> pOffset,
      Ref<Long> pModWhen, Ref<Long> pCrc32)
  {
    final ZipEntryRO zipEntry = /*reinterpret_cast<ZipEntryRO*>*/(entry);
    final ZipEntry ze = zipEntry.entry;

    if (pMethod != null) {
      pMethod.set((short) ze.getMethod());
    }
    if (pUncompLen != null) {
        pUncompLen.set(ze.getSize()); // uncompressed_length
    }
    if (pCompLen != null) {
        pCompLen.set(ze.getCompressedSize());
    }
    if (pOffset != null) {
      throw new UnsupportedOperationException("Figure out offset");
      //        pOffset = ze.offset;
    }
    if (pModWhen != null) {
        pModWhen.set(ze.getLastModifiedTime().toMillis());
    }
    if (pCrc32 != null) {
      pCrc32.set(ze.getCrc());
    }

    return true;
  }

  boolean startIteration(Object cookie) {
    return startIteration(cookie, null, null);
  }

  boolean startIteration(/* void** */ Object cookie, final String prefix, final String suffix)
  {
    throw new UnsupportedOperationException("Implememnt me");
//    ZipEntryRO* ze = new ZipEntryRO;
//    String pe(prefix ? prefix : "");
//    String se(suffix ? suffix : "");
//    int error = StartIteration(mHandle, &(ze.cookie),
//    prefix ? &pe : null,
//      suffix ? &se : null);
//    if (error) {
//      ALOGW("Could not start iteration over %s: %s", mFileName, ErrorCodeString(error));
//      delete ze;
//      return false;
//    }
//
//    *cookie = ze;
//    return true;
  }

  org.robolectric.res.android.ZipFileRO.ZipEntryRO nextEntry(/*void* */ Object cookie)
  {
    throw new UnsupportedOperationException("Implememnt me");
//    ZipEntryRO ze = /*reinterpret_cast<ZipEntryRO*>*/(ZipEntryRO) cookie;
//    int error = Next(ze.cookie, &(ze.entry), &(ze.name));
//    if (error) {
//      if (error != -1) {
//        ALOGW("Error iteration over %s: %s", mFileName, ErrorCodeString(error));
//      }
//      return null;
//    }
//
//    return &(ze.entry);
  }

  void endIteration(/*void**/ Object cookie)
  {
//    delete reinterpret_cast<ZipEntryRO*>(cookie);
  }

  void releaseEntry(org.robolectric.res.android.ZipFileRO.ZipEntryRO entry)
  {
//    delete reinterpret_cast<ZipEntryRO*>(entry);
  }

  /*
   * Copy the entry's filename to the buffer.
   */
  int getEntryFileName(org.robolectric.res.android.ZipFileRO.ZipEntryRO entry, String buffer, int bufLen)
  {
    throw new UnsupportedOperationException("Implememnt me");

//    final ZipEntryRO* zipEntry = reinterpret_cast<ZipEntryRO*>(entry);
//    final uint16_t requiredSize = zipEntry.name.name_length + 1;
//
//    if (bufLen < requiredSize) {
//      ALOGW("Buffer too short, requires %d bytes for entry name", requiredSize);
//      return requiredSize;
//    }
//
//    memcpy(buffer, zipEntry.name.name, requiredSize - 1);
//    buffer[requiredSize - 1] = '\0';
//
//    return 0;
  }

/*
 * Create a new FileMap object that spans the data in "entry".
 */
  /*FileMap*/ ZipFileRO(org.robolectric.res.android.ZipFileRO.ZipEntryRO entry)
  {
    throw new UnsupportedOperationException("Implememnt me");

//    final ZipEntryRO *zipEntry = reinterpret_cast<ZipEntryRO*>(entry);
//    final ZipEntry& ze = zipEntry.entry;
//    int fd = GetFileDescriptor(mHandle);
//    size_t actualLen = 0;
//
//    if (ze.method == kCompressStored) {
//      actualLen = ze.uncompressed_length;
//    } else {
//      actualLen = ze.compressed_length;
//    }
//
//    FileMap* newMap = new FileMap();
//    if (!newMap.create(mFileName, fd, ze.offset, actualLen, true)) {
//      delete newMap;
//      return null;
//    }
//
//    return newMap;
  }

  /*
   * Uncompress an entry, in its entirety, into the provided output buffer.
   *
   * This doesn't verify the data's CRC, which might be useful for
   * uncompressed data.  The caller should be able to manage it.
   */
  boolean uncompressEntry(org.robolectric.res.android.ZipFileRO.ZipEntryRO entry, Object buffer, int size)
  {
    throw new UnsupportedOperationException("Implememnt me");
//    ZipEntryRO *zipEntry = reinterpret_cast<ZipEntryRO*>(entry);
//    final int error = ExtractToMemory(mHandle, &(zipEntry.entry),
//    (uint8_t*) buffer, size);
//    if (error) {
//      ALOGW("ExtractToMemory failed with %s", ErrorCodeString(error));
//      return false;
//    }
//
//    return true;
  }

  /*
   * Uncompress an entry, in its entirety, to an open file descriptor.
   *
   * This doesn't verify the data's CRC, but probably should.
   */
  boolean uncompressEntry(org.robolectric.res.android.ZipFileRO.ZipEntryRO entry, int fd)
  {
    throw new UnsupportedOperationException("Implememnt me");
//    ZipEntryRO *zipEntry = reinterpret_cast<ZipEntryRO*>(entry);
//    final int error = ExtractEntryToFile(mHandle, &(zipEntry.entry), fd);
//    if (error) {
//      ALOGW("ExtractToMemory failed with %s", ErrorCodeString(error));
//      return false;
//    }
//
//    return true;
  }

  static String String(String string) {
    return string;
  }

  static class FileMap {
  }
}
