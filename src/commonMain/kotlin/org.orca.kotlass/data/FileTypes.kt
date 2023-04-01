package org.orca.kotlass.data

/**
 * List of currently known compass file types.
 */
enum class FileType {
    Folder,
    HTMLDocument,
    Document,
    Link,
}

// Order for the enum
private val _fileTypes = listOf(
    null,
    FileType.Folder,
    FileType.HTMLDocument,
    FileType.Document,
    FileType.Link
)

/**
 * Match up a file to a type
 */
fun getFileType(enumIndex: Int): FileType? {
    return  if (enumIndex >= _fileTypes.size) null
            else _fileTypes[enumIndex]
}