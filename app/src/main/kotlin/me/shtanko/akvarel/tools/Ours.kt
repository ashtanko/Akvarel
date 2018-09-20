package me.shtanko.akvarel.installed.tools

import android.content.Context
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import me.shtanko.core.Logger
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
        startPoint()
    }

    private val checkpoint = "====== START_POINT ${date()} ======"
    private val deathpoint = "====== DEATH_POINT ${date()} ======"


    private fun date(): String {
        val datePattern = "EEE, d MMM yyyy HH:mm:ss Z"
        val dateTime = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat(datePattern, Locale.ENGLISH)
        val date = Date(dateTime)
        return timeFormat.format(date)
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
        } catch (ignored: Exception) {
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
        } catch (ignored: Exception) {
        }
    }

    private fun calculateFiles(
            value: String,
            log: File
    ) {
        val logsDir = File("$logPath/$LOGS_DIR_NAME")
        writeStringToFile(value, log)
    }

    private fun writeLogOrCreateNew(value: String) {
        val path = "$logPath/$LOGS_DIR_NAME/$LOG_FULL_FILE_NAME"
        val dirPath = "$logPath/$LOGS_DIR_NAME"
        try {
            val dir = File(dirPath)
            dir.mkdirs()
            val log = File(path)
            if (log.exists()) {
                calculateFiles(value, log)
            } else {
                log.createNewFile()
                writeStringToFile(value, log)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    private fun writeSysFileOrCreateNew() {
        val path = "$logPath/$SYSTEM_DUMP_FULL_FILE_NAME"
        val log = File(path)
        when {
            log.length() > 0 -> return
            log.exists() -> writeDump(log)
            else -> {
                log.createNewFile()
                writeDump(log)
            }
        }
    }

    private fun writeDump(dumpFile: File) {
        val dump = dump()
        writeStringToFile(dump, dumpFile)
    }

    private fun writeStringToFile(
            value: String,
            file: File
    ) {
        val bufferedWriter = BufferedWriter(FileWriter(file, true))
        try {
            bufferedWriter.write(value)
            bufferedWriter.newLine()

        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            try {
                bufferedWriter.flush()
                bufferedWriter.close()
            } catch (ignored: Exception) {
            }
        }
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
            try {
                inputStream.close()
            } catch (ignored: Exception) {
            }
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
            try {
                inputStream.close()
            } catch (ignored: Exception) {
            }
        } catch (e: IOException) {
            // ignored
        }

        return stringBuilder.toString()
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

    fun meminfo() {
        val data = listOf("/proc/meminfo")
    }

    private fun info(): String {
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

    override fun d(vararg variables: Any) {
        Log.d(TAG, variables.contentToString())
    }
}