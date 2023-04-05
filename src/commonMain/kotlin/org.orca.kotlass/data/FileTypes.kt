package org.orca.kotlass.data

import kotlinx.serialization.Serializable
import org.orca.kotlass.utils.FileTypeSerializer

/**
 * List of currently known compass file types.
 */
@Serializable(with = FileTypeSerializer::class)
enum class FileType {
    Unknown,
    Folder,
    HTMLDocument,
    Document,
    Link,
}

///**
// * Match up a file to a type
// */
//fun getFileType(enumIndex: Int): FileType? {
//    return  if (enumIndex >= _fileTypes.size) null
//    else _fileTypes[enumIndex]
//}