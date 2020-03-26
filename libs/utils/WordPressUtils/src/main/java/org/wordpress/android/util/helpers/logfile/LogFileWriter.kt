package org.wordpress.android.util.helpers.logfile

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.util.Date
import org.wordpress.android.util.DateTimeUtils

/**
 * A class that manages writing to a log file.
 *
 * This class creates and writes to a log file, and will typically persist for the entire lifecycle
 * of its host application.
 */
class LogFileWriter {
    private val fileWriter: FileWriter

    /**
     * A reference to the underlying {@link Java.IO.File} file.
     * Should only be used for testing.
     */
    internal val file: File

    /**
     * General-purpose constructor – given a log file directory, creates and prepares a log file for writing.
     * @param context: The Android [context] object
     */
    @JvmOverloads
    constructor(context: Context, id: String = DateTimeUtils.iso8601FromDate(Date())) {
        val logFileDirectory = LogFileHelpers.logFileDirectory(context)
        this.file = File(logFileDirectory, "$id.log")
        this.fileWriter = FileWriter(this.file)
    }

    /**
     * Writes the provided string to the log file synchronously
     */
    fun write(data: String) {
        this.fileWriter.write(data)
        this.fileWriter.flush()
    }
}
