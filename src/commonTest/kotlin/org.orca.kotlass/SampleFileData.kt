package org.orca.kotlass

import org.orca.kotlass.data.AcademicGroup

interface SampleFileData {
    val testFileAssetId: String
    val testInstanceId: String
    val testActivityId: Int
    val testAcademicGroup: AcademicGroup?
}