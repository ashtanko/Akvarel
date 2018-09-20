package me.shtanko.akvarel.tools

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import me.shtanko.core.Logger
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class Ours @Inject constructor(
        private val context: Context
) : Logger {

    private var androidExternalFilesDir = (context.applicationContext.getExternalFilesDir(null)
            ?: context.applicationContext.cacheDir)

    private var logPath: String = androidExternalFilesDir.absolutePath

    companion object {
        private const val TAG: String = "AKVAREL"
        private const val SYSTEM_DUMP_FILE_NAME: String = "sys-info"
        private const val SYSTEM_DUMP_FILE_EXTENSION: String = ".log"
        private const val SYSTEM_DUMP_FULL_FILE_NAME: String =
                "$SYSTEM_DUMP_FILE_NAME$SYSTEM_DUMP_FILE_EXTENSION"
        private const val LOG_FILE_NAME: String = "log"
        private const val OLD_LOG_FILE_SUFFIX: String = ".old"
        private const val LOG_FILE_EXTENSION = ".log"
        private const val LOGS_DIR_NAME = "logs"
        private const val LOG_FULL_FILE_NAME = "$LOG_FILE_NAME$LOG_FILE_EXTENSION"
        private const val OLD_LOG_FULL_FILE_NAME = "$LOG_FILE_NAME$OLD_LOG_FILE_SUFFIX$LOG_FILE_EXTENSION"
        private const val BYTE = 1024
        private const val KB = 1024 * BYTE
        private const val MB = 1024 * KB
        private const val LOG_FILE_MAX_SIZE = 1 * BYTE // 1 kb
        private const val LOG_FILE_SIZE_LIMIT = MB // 1 mb
        private const val MAX_LOG_FILES = 5
        private val filesTable = HashMap<String, File>()
        private val logQueue = DispatchQueue("Log-Queue")
    }

    init {
    }

    private val checkpoint = "====== START_POINT ${date()} ======"
    private val deathpoint = "====== DEATH_POINT ${date()} ======"


    /**
     * if log max file is 4
     * log.log, log.old.log, log.old.1.log, log.old.2.log
     * if last log file <log.old.2.log> is full, start write to first file <log.log>
     *
     */
    private val logQueue = DispatchQueue("Log-Queue")

    init {
    }

    fun printLogFilePath() {
        Log.d(TAG, "$logPath/$LOGS_DIR_NAME/$LOG_FULL_FILE_NAME")
        Log.d(TAG, "$logPath/$SYSTEM_DUMP_FULL_FILE_NAME")
    }

    fun printDump() {
        Log.d(TAG, dump())
    }

    override fun startPoint() {
        try {
            logQueue.postRunnable(Runnable {
                try {
                    writeLogOrCreateNew(checkpoint)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            })
        } catch (ex: Exception) {
            error(ex)
        }
    }

    fun deathPoint() {
        try {
            logQueue.postRunnable(Runnable {
                try {
                    writeLogOrCreateNew(deathpoint)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            })
        } catch (ex: Exception) {
            error(ex)
        }
    }

    override fun d(vararg variables: Any) {
        val message = Arrays.toString(variables)
        Log.d(TAG, message)
//        try {
//            logQueue.postRunnable(Runnable {
//                try {
//                    writeLogOrCreateNew(message)
//                } catch (ex: Exception) {
//                    ex.printStackTrace()
//                }
//            })
//        } catch (ex: Exception) {
//            error(ex)
//        }
    }

    // region verbose level
    fun e(vararg objects: Any?) {
        val message = Arrays.toString(objects)
        Log.e(TAG, message)
//        try {
//            logQueue.postRunnable(Runnable {
//                try {
//                    writeLogOrCreateNew(message)
//                } catch (ex: Exception) {
//                    ex.printStackTrace()
//                }
//            })
//        } catch (ex: Exception) {
//            error(ex)
//        }
    }

    fun onDestroy(vararg objects: Any?) {
        val message = Arrays.toString(objects)
        val result = "$message ${date()}"
        Log.d(TAG, message)
        try {
            logQueue.postRunnable(Runnable {
                try {
                    writeLogOrCreateNew(result)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            })
        } catch (ex: Exception) {
            error(ex)
        }
    }

    fun v(vararg objects: Any?) {
        Log.v(TAG, Arrays.toString(objects))
    }

    // endregion

    fun error(error: Throwable) {
    }

    fun getFormattedSysFileSize(): String {
        val size = getSysFile().length().toFloat() / 1024.toFloat()
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(size)
    }

    fun getSysFile(): File {
        val path = "$logPath/$SYSTEM_DUMP_FULL_FILE_NAME"
        return File(path)
    }

    fun getLogFile(): File {
        val path = "$logPath/$LOGS_DIR_NAME/$LOG_FULL_FILE_NAME"
        return File(path)
    }

    fun getLogsDir(): File {
        val logsDir = "$logPath/$LOGS_DIR_NAME"
        return File(logsDir)
    }

    fun getAllLogsNames(): String {
        val stringBuilder = StringBuilder()
        for (log in getLogs()) {
            stringBuilder.append(log.name)
            stringBuilder.append(", ")
        }
        return stringBuilder.toString()
    }

    fun getLogsCount(): String {
        return "${getLogs().size}"
    }

    fun getAllLogsSize(): String {
        val decimalFormat = DecimalFormat("#.##")
        var size = 0.0
        for (log in getLogs()) {
            size += log.length()
                    .toFloat()
        }
        return decimalFormat.format(size)
    }

    fun getLogs(): List<File> {
        val dir = getLogsDir()
        val list = mutableListOf<File>()
        for (file in dir.listFiles()) {
            list.add(file)
        }
        return list
    }

    fun cleanUpLogs() {
        val logFile = getLogFile()
        cleanUp(logFile)
    }

    fun cleanUpSysInfo() {
        val sysInfoFile = getSysFile()
        cleanUp(sysInfoFile)
    }

    private fun cleanUp(file: File) {
        if (file.exists()) file.delete()
    }

    fun send() {
        val path = "$logPath/$SYSTEM_DUMP_FULL_FILE_NAME"
        val log = File(path)
        val uri = Uri.fromFile(log)
    }

    private fun dump(): String {
        val sb = StringBuilder()
        val date = date()
        val cpu = cpu()
        val info = info()
        sb.append("DATE: $date")
        sb.append("\n")
        sb.append("INFO: $info")
        sb.append("\n")
        sb.append("CPU: $cpu")
        sb.append("\n")
        sb.append("OTHER:")
        sb.append("\n")
        sb.append("log file path: $logPath/$SYSTEM_DUMP_FULL_FILE_NAME")
        return sb.toString()
    }

    private fun logsTree(
            file: File
    ): MutableList<File> {
        val children = file.listFiles()
        val all = mutableListOf<File>()
        for (child in children) {
            all.add(child)
            // logsTree(child)
        }
        return all
    }

    fun calculateFiles(
            value: String,
            log: File
    ) {
        val logsDir = File("$logPath/$LOGS_DIR_NAME")
        writeStringToFile(value, log)
    }

    fun printTable(table: Map<String, File>) {
        val stringBuilder = StringBuilder()
        table.entries.forEach {
            val fileName = it.key
            val fileSize = it.value.length()
            stringBuilder.append("name: $fileName size: $fileSize")
                    .append("\n")
        }
    }

    private fun fileInfo(file: File) {
        if (file.exists()) {
            val sb = StringBuilder()
            sb.append("name: ${file.name}")
            sb.append("\n")
            sb.append("length: ${file.length()}")
            sb.append("\n")
            sb.append("is file: ${file.isFile}")
            sb.append("\n")
        }
    }

    fun writeLogOrCreateNew(value: String) {
        val path = "$logPath/$LOGS_DIR_NAME/$LOG_FULL_FILE_NAME"
        val dirPath = "$logPath/$LOGS_DIR_NAME"
        val dir = File(dirPath)
        dir.mkdirs()
        val log = File(path)
        if (log.exists()) {
            calculateFiles(value, log)
        } else {
            log.createNewFile()
            writeStringToFile(value, log)
        }
    }

    fun writeSysFileOrCreateNew() {
        val path = "$logPath/$SYSTEM_DUMP_FULL_FILE_NAME"
        val log = File(path)
        if (log.exists()) {
            writeDump(log)
        } else {
            log.createNewFile()
            writeDump(log)
        }
    }

    private fun writeDump(dumpFile: File) {
        val dump = dump()
        writeStringToFile(dump, dumpFile)
    }

    fun writeStringToFile(
            value: String,
            file: File
    ) {
        val bufferedWriter = BufferedWriter(FileWriter(file, true))
        try {
            bufferedWriter.write(value)
            bufferedWriter.newLine()
            bufferedWriter.flush()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun date(): String {
        val datePattern = "EEE, d MMM yyyy HH:mm:ss Z"
        val dateTime = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat(datePattern, Locale.ENGLISH)
        val date = Date(dateTime)
        return timeFormat.format(date)
    }

    private fun cpu(): String {
        val stringBuilder = StringBuilder()
        val data = listOf("/system/bin/cat", "/proc/cpuinfo")
        try {
            val processBuilder = ProcessBuilder(data)
            val process = processBuilder.start()
            val inputStream = process.inputStream
            val byteArray = ByteArray(1024)
            while (inputStream.read(byteArray) != -1) {
                stringBuilder.append(String(byteArray))
            }
            inputStream.close()
        } catch (e: IOException) {
            // ignored
        }

        return stringBuilder.toString()
    }

    fun version(): String {
        val stringBuilder = StringBuilder()
        try {
            val processBuilder = ProcessBuilder(listOf("/proc/version"))
            val process = processBuilder.start()
            val inputStream = process.inputStream
            val byteArray = ByteArray(1024)
            while (inputStream.read(byteArray) != -1) {
                stringBuilder.append(String(byteArray))
            }
            inputStream.close()
        } catch (e: IOException) {
            // ignored
        }

        return stringBuilder.toString()
    }

    fun meminfo() {
        val data = listOf("/proc/meminfo")
    }

    fun info(): String {
        val brand = Build.BRAND
        val device = Build.DEVICE
        val hardware = Build.HARDWARE
        val model = Build.MODEL
        val user = Build.USER
        val version = Build.VERSION.SDK_INT
        val os = Build.VERSION.RELEASE
        val isRooted = isRooted()
        val userDeviceTime = Build.TIME

        val supported = if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            arrayOf(
                    Build.SUPPORTED_ABIS, Build.SUPPORTED_32_BIT_ABIS, Build.SUPPORTED_64_BIT_ABIS
            ).toString()
        } else {
            "VERSION.SDK_INT < LOLLIPOP"
        }

        val sb = StringBuilder()
        sb.append("User device time: $userDeviceTime")
        sb.append("\n")
        sb.append("brand: $brand")
        sb.append("\n")
        sb.append("device: $device")
        sb.append("\n")
        sb.append("hardware: $hardware")
        sb.append("\n")
        sb.append("model: $model")
        sb.append("\n")
        sb.append("user: $user")
        sb.append("\n")
        sb.append("version: $version")
        sb.append("\n")
        sb.append("os: $os")
        sb.append("\n")
        sb.append("rooted: $isRooted")
        return sb.toString()
    }

    private fun isRooted(): Boolean {
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true
        }

        try {
            val path = "/system/app/Superuser.apk"
            val file = File(path)
            if (file.exists()) return true
        } catch (e: Exception) {
            // ignored
        }
        val command1 = "/system/xbin/which su"
        val command2 = "/system/bin/which su"
        val command3 = "which su"
        return canExecuteCommand(command1) || canExecuteCommand(command2) || canExecuteCommand(command3)
    }

    private fun canExecuteCommand(command: String) = try {
        Runtime.getRuntime()
                .exec(command)
        true
    } catch (e: Exception) {
        false
    }
}